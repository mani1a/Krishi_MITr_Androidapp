package com.manila.fasaldoctor.fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
//import
import com.bumptech.glide.Glide
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.manila.fasaldoctor.adapter.PostAdapter
import com.manila.fasaldoctor.databinding.FragmentFeedBinding
import com.manila.fasaldoctor.model.UserPost
import java.io.InputStream
import java.io.OutputStream

class FeedFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var progressBar: ProgressDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentFeedBinding
    private lateinit var usersArrayList: ArrayList<UserPost>
    private lateinit var postAdapter: PostAdapter
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    private lateinit var uid: String // Declare uid at the top
//     val s =
    override fun onStart() {
        super.onStart()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        val view = binding.root

        val buttonAddPost = binding.buttonAddPost
        val editTextQuestion = binding.editTextQuestion
        val postButton = binding.buttonPost
        imageView = binding.imageViewPost
        val button = binding.buttonLoadPicture


        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){
            print("here51")
            if(it!=null)
            {
                imageUri = it
                imageView.setImageURI(it)
            }
            else {
//                Toast.makeText(this,"image is not catch",Toast.LENGTH_SHORT).show()
            }
        }




        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserEmail = currentUser?.email

        firebaseStorage = FirebaseStorage.getInstance()
        val storageReference: StorageReference = firebaseStorage.reference

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        usersArrayList = arrayListOf()
        postAdapter = PostAdapter(usersArrayList, currentUserEmail.toString())

        recyclerView.adapter = postAdapter

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        buttonAddPost.setOnClickListener {
            // Toggle visibility of other elements
            if (editTextQuestion.visibility == View.GONE) {
                editTextQuestion.visibility = View.VISIBLE
                button.visibility = View.VISIBLE
                postButton.visibility = View.VISIBLE
                imageView.visibility = View.VISIBLE
                binding.description.visibility = View.VISIBLE
                buttonAddPost.text = " Slide up (post later) "
            } else {
                editTextQuestion.visibility = View.GONE
                button.visibility = View.GONE
                postButton.visibility = View.GONE
                imageView.visibility = View.GONE
                binding.description.visibility = View.GONE
                buttonAddPost.text = " Add Post + "
            }
        }

        if (user != null) {



            uid = user.uid // Assign the UID here

            button.setOnClickListener {
//                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//
                println("here2")
//                startActivityForResult(gallery, pickImage)
                getContent.launch("image/*")
            }


            postButton.setOnClickListener {
                uploadImageToStorage(imageUri)
            }

            val database = FirebaseDatabase.getInstance().getReference("posts")

            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    usersArrayList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val post = dataSnapshot.getValue(UserPost::class.java)

                        // Check if the post is not null and contains imageUrl, text, and postId
                        if (post != null && post.imageUrl != null && post.text != null && post.postId != null) {
                            val userPost = UserPost(
                                postId = post.postId,
                                imageUrl = post.imageUrl,
                                text = post.text,
                                email = post.email,
                                userImgUrl = post.userImgUrl,
                                description = post.description
                            )
                            usersArrayList.add(userPost)
                        }
                    }
                    postAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }

        return view
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK && requestCode == pickImage) {
//            imageUri = data?.data
//            println(imageUri)
//            imageView.setImageURI(imageUri)
//        }
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickImage) {
//
//    }

    private fun uploadImageToStorage(imageUri: Uri?) {
        if (imageUri != null) {
            progressBar = ProgressDialog(requireContext())
            progressBar.setTitle("Post uploading....")
            progressBar.show()
            val storageReference = firebaseStorage.reference
            val imagesRef = storageReference.child("images/$uid") // Define the path where the image will be stored

            // Upload the image
            val uploadTask = imagesRef.putFile(imageUri)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, now get the download URL
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()

                    // Once you have the imageUrl, you can use it to store data in the Realtime Database
                    storeData(imageUrl)
                }
            }.addOnFailureListener { e ->
                // Handle the error
                showToast("Image upload failed: ${e.message}")
            }
        }
    }

    private fun storeData(imageUrl: String) {
        var userImg: String = ""

        // Fetch user data from the Realtime Database
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Fetch the user's image URL
                    userImg = dataSnapshot.child("imageUrl").value.toString()

                    // Now, you can use the userImageUrl as needed
                    println(userImg)
                } else {
                    // Handle the case where user data does not exist
                    showToast("User data not found.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any database error
                showToast("Error: ${databaseError.message}")
            }
        })

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val role = dataSnapshot.child("role").value.toString()
                    val email = dataSnapshot.child("email").value.toString()

                    val data = UserPost(
                        email = email,
                        role = role,
                        uid = uid,
                        userImgUrl = userImg,
                        description = binding.description.text.toString(),
                        text = binding.editTextQuestion.text.toString(),
                        imageUrl = imageUrl, // Use the imageUrl parameter here
                        timestamp = System.currentTimeMillis(),
                        comments = listOf(),
                        postId = ""
                    )

                    val databaseReference = FirebaseDatabase.getInstance().getReference("posts")

                    // Automatically generate a unique key for each post
                    val postReference = databaseReference.push()
                    var postId = postReference.key // Get the generated key
                    data.postId = postId // Assign it as a child in the post data

                    // Update the post data with the postId as a child
                    postReference.setValue(data).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if (progressBar.isShowing) progressBar.dismiss()
                            showToast("Post successfully uploaded!")
                            binding.editTextQuestion.visibility = View.GONE
                            binding.buttonLoadPicture.visibility = View.GONE
                            binding.buttonPost.visibility = View.GONE
                            imageView.visibility = View.GONE
                            binding.buttonAddPost.text = " Add Post + "
                        } else {
                            showToast("Failed to upload post. Please try again.")
                        }
                    }
                } else {
                    println("User data does not exist in the database")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error: ${databaseError.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
