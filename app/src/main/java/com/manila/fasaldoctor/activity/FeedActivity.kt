package com.manila.fasaldoctor.activity

import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.adapter.PostAdapter
import com.manila.fasaldoctor.model.UserPost

class FeedActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var usersArrayList: ArrayList<UserPost>
    private lateinit var postAdapter: PostAdapter
    private lateinit var uid: String // Declare uid at the top

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        title = "KotlinApp"
        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.buttonLoadPicture)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        usersArrayList = arrayListOf()
        postAdapter = PostAdapter(usersArrayList)

        recyclerView.adapter = postAdapter

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            uid = user.uid // Assign the UID here

            button.setOnClickListener {
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, pickImage)
            }

            val postButton = findViewById<Button>(R.id.buttonPost)
            postButton.setOnClickListener {
                storeData(imageUri)
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
                                text = post.text
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

    private fun storeData(imageUri: Uri?) {
        // Fetch user data from the Realtime Database
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val role = dataSnapshot.child("role").value.toString()
                    val email = dataSnapshot.child("email").value.toString()

                    val data = UserPost(
                        email = email,
                        role = role,
                        uid = uid,
                        text = findViewById<EditText>(R.id.editTextQuestion).text.toString(),
                        imageUrl = imageUri.toString(),
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
                            showToast("Post successfully uploaded!")
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