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

class PostAdapter(private val userList: ArrayList<UserPost>) :
    RecyclerView.Adapter<PostAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewPostImage)
        val textView: TextView = itemView.findViewById(R.id.textViewPostText)
        val commentButton: Button = itemView.findViewById(R.id.buttonComment)
    }
//    private val posts: MutableList<UserPost> = mutableListOf()

    // Function to fetch data from Firebase
//    fun fetchData() {
//        val databaseReference = FirebaseDatabase.getInstance().getReference("posts")
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                posts.clear()
//                for (postSnapshot in dataSnapshot.children) {
//                    val post = postSnapshot.getValue(UserPost::class.java)
//                    post?.let { posts.add(it) }
//                }
//                Log.d(TAG, "Data retrieved successfully from Firebase")
//                notifyDataSetChanged()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle the error
//                Log.e(TAG, "Error fetching data from Firebase: ${databaseError.message}")
//            }
//        })
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val post = posts[position]

        // Bind the data to the views
        Glide.with(holder.itemView.context).load(userList[position].imageUrl).into(holder.imageView)
        holder.textView.text = userList[position].text

        // Add a click listener to the comment button
        holder.commentButton.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }


}
