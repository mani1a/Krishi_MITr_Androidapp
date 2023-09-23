package com.manila.fasaldoctor.activity

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.adapter.PostAdapter
import com.manila.fasaldoctor.model.UserPost
import com.google.android.material.navigation.NavigationBarView
import androidx.fragment.app.Fragment
import com.manila.fasaldoctor.databinding.ActivityFeedBinding
import android.content.Intent
import android.app.ProgressDialog
import android.view.View

class FeedActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var progressBar : ProgressDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var usersArrayList: ArrayList<UserPost>
    private lateinit var postAdapter: PostAdapter
    private lateinit var uid: String // Declare uid at the top


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        val buttonAddPost = findViewById<Button>(R.id.buttonAddPost)
        val editTextQuestion = findViewById<EditText>(R.id.editTextQuestion)
        val buttonLoadPicture = findViewById<Button>(R.id.buttonLoadPicture)
        val postButton = findViewById<Button>(R.id.buttonPost)
        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.buttonLoadPicture)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserEmail = currentUser?.email

        firebaseStorage = FirebaseStorage.getInstance()
        val storageReference: StorageReference = firebaseStorage.reference
        title = "KotlinApp"

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        usersArrayList = arrayListOf()
        postAdapter = PostAdapter(usersArrayList, currentUserEmail.toString())

        recyclerView.adapter = postAdapter



        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        buttonAddPost.setOnClickListener {
            // Toggle visibility of other elements
            if (editTextQuestion.visibility == View.GONE) {
                editTextQuestion.visibility = View.VISIBLE
                buttonLoadPicture.visibility = View.VISIBLE
                postButton.visibility = View.VISIBLE
                imageView.visibility = View.VISIBLE
                findViewById<EditText>(R.id.description).visibility = View.VISIBLE
                buttonAddPost.text = " Slide up (post later) "
            } else {
                editTextQuestion.visibility = View.GONE
                buttonLoadPicture.visibility = View.GONE
                postButton.visibility = View.GONE
                imageView.visibility = View.GONE
                findViewById<EditText>(R.id.description).visibility = View.GONE
                buttonAddPost.text = " Add Post + "
            }
        }


        if (user != null) {
            uid = user.uid // Assign the UID here

            button.setOnClickListener {
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, pickImage)
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
                                imageUrl = post.imageUrl ,
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
                    Toast.makeText(this@FeedActivity, error.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun uploadImageToStorage(imageUri: Uri?) {
        if (imageUri != null) {
            progressBar = ProgressDialog(this@FeedActivity)
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
        val buttonAddPost = findViewById<Button>(R.id.buttonAddPost)
        val editTextQuestion = findViewById<EditText>(R.id.editTextQuestion)
        val buttonLoadPicture = findViewById<Button>(R.id.buttonLoadPicture)
        val postButton = findViewById<Button>(R.id.buttonPost)
        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.buttonLoadPicture)
        // Change the parameter type here
        // Fetch user data from the Realtime Database
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
                        userImgUrl=userImg,
                        description = findViewById<EditText>(R.id.description).text.toString(),
                        text = findViewById<EditText>(R.id.editTextQuestion).text.toString(),
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

                            if (progressBar.isShowing)progressBar.dismiss()
                            showToast("Post successfully uploaded!")
                            editTextQuestion.visibility = View.GONE
                            buttonLoadPicture.visibility = View.GONE
                            postButton.visibility = View.GONE
                            imageView.visibility = View.GONE
                            buttonAddPost.text = " Add Post + "
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
        Toast.makeText(this@FeedActivity, message, Toast.LENGTH_SHORT).show()
    }
}
