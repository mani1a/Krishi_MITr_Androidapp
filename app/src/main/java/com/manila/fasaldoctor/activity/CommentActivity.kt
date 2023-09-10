package com.manila.fasaldoctor.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.model.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.manila.fasaldoctor.model.UserPost
import android.widget.EditText
import android.widget.Toast
import android.widget.Button
import android.content.Intent

class CommentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val editTextComment = findViewById<EditText>(R.id.editTextComment)
        val buttonPostComment = findViewById<Button>(R.id.buttonPostComment)

        // Retrieve the userId from the intent extras
        val postId = intent.getStringExtra("postId")

        println("postId : $postId")
//        Toast.makeText(this@CommentActivity, "comment page , Toast.LENGTH_SHORT).show()
        buttonPostComment.setOnClickListener {
            if (postId != null) {
                val commentText = editTextComment.text.toString() // Move this line inside the click listener

                val databaseReference = FirebaseDatabase.getInstance().getReference("posts").child(postId)

                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val userPost = dataSnapshot.getValue(UserPost::class.java)

                            if (userPost != null) {
                                val updatedComments = userPost.comments.toMutableList()
                                updatedComments.add(Comment(email = "user@email.com", commentText)) // Use commentText here

                                userPost.comments = updatedComments

                                databaseReference.setValue(userPost).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            this@CommentActivity,
                                            "Comment added successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this@CommentActivity,
                                            "Failed to add comment",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@CommentActivity,
                                "Data snapshot doesn't exist",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle the error
                        Toast.makeText(
                            this@CommentActivity,
                            "Database error: ${databaseError.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } else {
                // Handle the case where postId is null
                Toast.makeText(
                    this@CommentActivity,
                    "Post ID is missing",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}
