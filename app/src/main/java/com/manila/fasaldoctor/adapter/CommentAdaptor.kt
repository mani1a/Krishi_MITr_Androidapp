package com.manila.fasaldoctor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.model.Comment
import com.google.firebase.database.*
class CommentAdapter(private val commentList: ArrayList<Comment>, private val postId: String) :
    RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emailView: TextView = itemView.findViewById(R.id.textViewEmail)
        val commentView: TextView = itemView.findViewById(R.id.textViewCommentText)
        val upvoteButton: Button = itemView.findViewById(R.id.buttonUpvote)
        val textViewUpvoteCount: TextView = itemView.findViewById(R.id.textViewUpvoteCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_items, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = commentList[position]

        val email = comment.email // Access the email value
        val commentText = comment.commentText // Access the commentText value
        var upvotes = comment.upvotes

        // Set the email and commentText values to the corresponding views
        holder.emailView.text = "@${email.substringBefore("@")}"
        holder.commentView.text = commentText

        holder.upvoteButton.setOnClickListener {
            val commentId = comment.com_id
            val commentRef = FirebaseDatabase.getInstance().getReference("posts")
                .child(postId)
                .child("comments")

            // Increment or decrement the upvote count for the specific comment based on IsUpvoted
            commentRef.runTransaction(object : Transaction.Handler {
                override fun doTransaction(currentData: MutableData): Transaction.Result {
                    val commentsList = currentData.getValue(object : GenericTypeIndicator<ArrayList<Comment>>() {})
                    commentsList?.let { comments ->
                        val commentToUpdate = comments.find { it.com_id == commentId }
                        commentToUpdate?.let {
                            val currentUpvotes = it.upvotes
                            if (!it.IsUpvoted) {
                                // If not upvoted, increment upvotes
                                it.upvotes = currentUpvotes - 1
                                it.IsUpvoted = true // Set IsUpvoted to true
                            } else {
                                // If already upvoted, decrement upvotes
                                it.upvotes = currentUpvotes + 1
                                it.IsUpvoted = false // Set IsUpvoted to false
                            }
                        }
                    }
                    currentData.value = commentsList
                    return Transaction.success(currentData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    currentData: DataSnapshot?
                ) {
                    if (error == null && committed && currentData != null) {
                        // Update the UI (you may need to notify the adapter to refresh the data)
                        holder.textViewUpvoteCount.text = comment.upvotes.toString()
                    } else {
                        // Handle the error
                        // Log.e("CommentAdapter", "Upvote transaction failed", error?.toException())
                    }
                }
            })
        }



    }

    override fun getItemCount(): Int {
        return commentList.size
    }
}
