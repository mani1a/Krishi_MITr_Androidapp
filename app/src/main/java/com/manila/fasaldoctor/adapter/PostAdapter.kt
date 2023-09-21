package com.manila.fasaldoctor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.model.UserPost
import com.manila.fasaldoctor.activity.CommentActivity
import android.content.Intent

class PostAdapter(private val userList: ArrayList<UserPost>, private val currentUserEmail: String) :
    RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewPostImage)
        val textView: TextView = itemView.findViewById(R.id.textViewPostText)
        val commentButton: Button = itemView.findViewById(R.id.buttonComment)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = userList[position]

        // Load the post image using Glide
        Glide.with(holder.itemView.context)
            .load(post.imageUrl)
            .placeholder(R.drawable.progress)
            .into(holder.imageView)

        // Set the post text
        holder.textView.text = post.text

        // Set a click listener for the comment button
        holder.commentButton.setOnClickListener {
            // Create an Intent to start the CommentActivity
            val context = holder.itemView.context
            val intent = Intent(context, CommentActivity::class.java)

            intent.putExtra("postId", post.postId)
            // Add more data to the intent if needed

            // Start the CommentActivity
            context.startActivity(intent)
        }

        // Check if the post belongs to the currently logged-in user
        if (currentUserEmail == post.email) {
            // Show the delete button for the user's own posts
            holder.deleteButton.visibility = View.VISIBLE
            holder.deleteButton.setOnClickListener {
                // Delete the post here
                val postId = post.postId.toString()
                deletePost(postId)
            }
        } else {
            // Hide the delete button for other users' posts
            holder.deleteButton.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    // Function to delete a post by its postId
    private fun deletePost(postId: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("posts").child(postId)

        databaseReference.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Handle successful deletion, e.g., show a toast
            } else {
                // Handle deletion failure, e.g., show a toast
            }
        }
    }
}
