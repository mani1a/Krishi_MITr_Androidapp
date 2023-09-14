package com.manila.fasaldoctor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.model.Comment

class CommentAdapter(private val commentList: ArrayList<Comment>) :
    RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emailView: TextView = itemView.findViewById(R.id.textViewEmail)
        val commentView: TextView = itemView.findViewById(R.id.textViewCommentText)
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

        // Set the email and commentText values to the corresponding views
        holder.emailView.text = "Email: $email"
        holder.commentView.text = commentText
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
}
