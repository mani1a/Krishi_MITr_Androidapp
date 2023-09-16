package com.manila.fasaldoctor.adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.model.PostData

class FeedAdapter(private val context: Context, private val postList: ArrayList<PostData>):
    RecyclerView.Adapter<FeedAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_feed_fragments,parent,false)

        return ViewHolder(view)



    }

    override fun getItemCount(): Int {
        return postList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val post = postList[position]
        holder.userNameFeed.text = post.posterId
        holder.role.text = post.post

        Glide.with(context).load(post.imagePost).into(holder.postImgView)





    }


    class ViewHolder(view : View):RecyclerView.ViewHolder(view){

        val postImgView : ImageView = view.findViewById(R.id.feed_img)
        val postProfileImg : ImageView = view.findViewById(R.id.feed_prof_img)
        val userNameFeed : TextView = view.findViewById(R.id.feed_username)
        val role : TextView = view.findViewById(R.id.feed_txt_role)

    }
}