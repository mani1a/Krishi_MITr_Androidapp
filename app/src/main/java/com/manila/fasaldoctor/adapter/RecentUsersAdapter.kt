package com.manila.fasaldoctor.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.activity.ChatOpenActivity
import com.manila.fasaldoctor.model.RecentUser
import com.manila.fasaldoctor.model.User
import de.hdodenhof.circleimageview.CircleImageView

class RecentUsersAdapter (private var  context: Context, private var recentUserList : ArrayList<RecentUser>):
    RecyclerView.Adapter<RecentUsersAdapter.Viewholder>(){

    class Viewholder(view : View):RecyclerView.ViewHolder(view) {

        val userName: TextView = view.findViewById(R.id.id_txt_name)
        val email : TextView = view.findViewById(R.id.id_txt_email)
        val role : TextView = view.findViewById(R.id.id_txt_role)
        val pic : CircleImageView = view.findViewById(R.id.id_img)
        val openChat : RelativeLayout = view.findViewById(R.id.btn_open_chat)




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_recent_users,parent,false)

        return Viewholder(view)

    }

    override fun getItemCount(): Int {
       return recentUserList.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        val user = recentUserList[position]
        holder.userName.text = user.name
        holder.email.text = user.email
        holder.role.text = user.role
        if (user.role == "farmer"){
            Glide.with(context).load(user.imageUrl).placeholder(R.drawable.farmer).into(holder.pic)
        }else{
            Glide.with(context).load(user.imageUrl).placeholder(R.drawable.expert).into(holder.pic)
        }


        holder.openChat.setOnClickListener {

            val intent = Intent(context, ChatOpenActivity::class.java)

            intent.putExtra("name",user.name)
            intent.putExtra("uid",user.uid)

            context.startActivity(intent)


        }



    }

}
