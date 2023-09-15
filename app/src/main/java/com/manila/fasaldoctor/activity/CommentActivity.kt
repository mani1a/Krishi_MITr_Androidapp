package com.manila.fasaldoctor.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.adapter.CommentAdapter
import com.manila.fasaldoctor.model.Comment
import com.manila.fasaldoctor.model.UserPost

class CommentActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var commentList: ArrayList<Comment>
    private lateinit var commentAdapter: CommentAdapter
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        val editTextComment = findViewById<EditText>(R.id.editTextComment)
        val buttonPostComment = findViewById<Button>(R.id.buttonPostComment)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        commentList = arrayListOf()
        commentAdapter = CommentAdapter(commentList)

        recyclerView.adapter = commentAdapter

        // Retrieve the postId from the intent extras
        val postId = intent.getStringExtra("postId")
        println("post id : $postId")
        if (postId != null) {
            val commentsReference = database.child("posts").child(postId).child("comments")

            // Add a listener to retrieve comments
            commentsReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    commentList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val comment = dataSnapshot.getValue(Comment::class.java)
                        comment?.let { commentList.add(it) }
                    }
                    commentAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@CommentActivity, error.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            })

            buttonPostComment.setOnClickListener {
                if (postId != null) {
                    val commentText = editTextComment.text.toString()

                    val databaseReference =
                        FirebaseDatabase.getInstance().getReference("posts").child(postId)

                    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val userPost = dataSnapshot.getValue(UserPost::class.java)

                                if (userPost != null) {
                                    val updatedComments = userPost.comments.toMutableList()
                                    updatedComments.add(
                                        Comment(
                                            email = "user@email.com",
                                            commentText
                                        )
                                    )

                                    userPost.comments = updatedComments

                                    databaseReference.setValue(userPost)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(
                                                    this@CommentActivity,
                                                    "Comment added successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                editTextComment.text.clear() // Clear the comment text field
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
}
