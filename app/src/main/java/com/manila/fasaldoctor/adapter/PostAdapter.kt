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
import com.google.firebase.database.*
import android.util.Log
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.model.UserPost
import com.manila.fasaldoctor.R.layout.post_item
import com.manila.fasaldoctor.R.layout.activity_feed
import android.content.Intent
import com.manila.fasaldoctor.activity.CommentActivity

class PostAdapter(private val userList: ArrayList<UserPost>) :
    RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewPostImage)
        val textView: TextView = itemView.findViewById(R.id.textViewPostText)
        val commentButton: Button = itemView.findViewById(R.id.buttonComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        //
        Glide.with(holder.itemView.context).load(userList[position].imageUrl).into(holder.imageView)
        holder.textView.text = userList[position].text

        //
        holder.commentButton.setOnClickListener {
            // Create an Intent to start the CommentActivity
            val context = holder.itemView.context
            val intent = Intent(context, CommentActivity::class.java)

            intent.putExtra("postId", userList[position].postId)
            intent.putExtra("email", userList[position].email)

            // Start the CommentActivity
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }


}
