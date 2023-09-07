package com.manila.fasaldoctor.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.adapter.PostAdapter
import com.manila.fasaldoctor.model.UserPost
import com.manila.fasaldoctor.R.layout.activity_feed

class FeedActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var usersArrayList: ArrayList<UserPost>
    private lateinit var postAdapter: PostAdapter // Assuming you have a PostAdapter class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        title = "KotlinApp"
        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.buttonLoadPicture)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        usersArrayList = arrayListOf()
        postAdapter = PostAdapter(usersArrayList) // Initialize your adapter

        recyclerView.adapter = postAdapter // Set the adapter to the RecyclerView

        button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        // Add click listener for the "Post" button
        val postButton = findViewById<Button>(R.id.buttonPost)
        postButton.setOnClickListener {
            // Call the function to upload the data to Firebase
            storeData(imageUri)
        }

        val database = FirebaseDatabase.getInstance().getReference("posts")
//         var tag = "YourActivityName"
//
//        Log.d(tag, "Database reference: $database")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersArrayList.clear()
                for (dataSnapshot in snapshot.children) {
                    val post = dataSnapshot.getValue(UserPost::class.java)
                    post?.let { usersArrayList.add(it) }
                }
                postAdapter.notifyDataSetChanged() // Notify adapter when data changes
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FeedActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun storeData(imageUri: Uri?) {
        val data = UserPost(
            text = findViewById<EditText>(R.id.editTextQuestion).text.toString(),
            imageUrl = imageUri.toString(),
            timestamp = System.currentTimeMillis()
        )

        val databaseReference = FirebaseDatabase.getInstance().getReference("posts")
        val postReference = databaseReference.push()

        postReference.setValue(data).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                showToast("Post successfully uploaded!")
            } else {
                showToast("Failed to upload post. Please try again.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@FeedActivity, message, Toast.LENGTH_SHORT).show()
    }
}
