package com.manila.fasaldoctor.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.activity.ChatOpenActivity
import com.manila.fasaldoctor.model.User
import de.hdodenhof.circleimageview.CircleImageView

class UsersAdapter(private val context: Context, private val userList: ArrayList<User>):
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {



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
        holder.email.text = user.email
        holder.role.text = user.role

        holder.openChat.setOnClickListener {
            Toast.makeText(context,"Hmmmmmm",Toast.LENGTH_SHORT).show()

            val intent = Intent(context,ChatOpenActivity::class.java)

            intent.putExtra("name",user.name)
            intent.putExtra("uid",user.uid)

            context.startActivity(intent)


//            val fragmentManager =
        }

    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {

        val userName: TextView = view.findViewById(R.id.id_txt_name)
        val email : TextView = view.findViewById(R.id.id_txt_email)
        val role : TextView = view.findViewById(R.id.id_txt_role)
        val pic : CircleImageView = view.findViewById(R.id.id_img)
        val openChat : RelativeLayout = view.findViewById(R.id.btn_open_chat)
    }




}