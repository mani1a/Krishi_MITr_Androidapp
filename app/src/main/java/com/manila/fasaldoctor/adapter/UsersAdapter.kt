package com.manila.fasaldoctor.adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.activity.ChatOpenActivity
import com.manila.fasaldoctor.model.User
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class UsersAdapter (private val context: Context, private val userList: ArrayList<User>):
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    lateinit var firebaseAuth : FirebaseAuth
    lateinit var firebaseStorage: FirebaseStorage

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_users,parent,false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.userName.text = user.name
        holder.email.text = "Crop : - ${user.crop1} , ${user.crop2} , ${user.crop3} "
//        holder.role.text = user.status
        if (user.role == "farmer"){
        Glide.with(context).load(user.imageUrl).placeholder(R.drawable.farmer).into(holder.pic)
        }else{
        Glide.with(context).load(user.imageUrl).placeholder(R.drawable.expert).into(holder.pic)
        }



        holder.openChat.setOnClickListener {


            val intent = Intent(context,ChatOpenActivity::class.java)

            intent.putExtra("name",user.name)
            intent.putExtra("uid",user.uid)

            context.startActivity(intent)


        }


    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {

        val userName: TextView = view.findViewById(R.id.id_txt_name)
        val email : TextView = view.findViewById(R.id.id_txt_email)
        val role : TextView = view.findViewById(R.id.id_txt_role)
        val pic : CircleImageView = view.findViewById(R.id.id_img)
        val openChat : RelativeLayout = view.findViewById(R.id.btn_open_chat)
        val show : TextView = view.findViewById(R.id.id_show)

    }




}