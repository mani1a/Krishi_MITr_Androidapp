package com.manila.fasaldoctor.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.adapter.RecentUsersAdapter
import com.manila.fasaldoctor.databinding.ActivityRecentChatBinding
import com.manila.fasaldoctor.model.Messages
import com.manila.fasaldoctor.model.User
import java.util.Objects

class RecentChatActivity : AppCompatActivity() {
    lateinit var binding : ActivityRecentChatBinding

    lateinit var recentRecyclerView: RecyclerView
    lateinit var recentUsersAdapter: RecentUsersAdapter
    lateinit var recentUserList : ArrayList<User>

    lateinit var firebaseDatabase: DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecentChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recentUserList = ArrayList()
        recentRecyclerView = findViewById(R.id.user_recent_recycler)
        recentUsersAdapter = RecentUsersAdapter(this,recentUserList)
        recentRecyclerView.layoutManager = LinearLayoutManager(this)
        recentRecyclerView.adapter = recentUsersAdapter

        firebaseDatabase = FirebaseDatabase.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()

        val senderuid = firebaseAuth.currentUser!!.uid


        firebaseDatabase.child("Users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                recentUserList.clear()

                for (postsnapshot in snapshot.children){
                    val recent = postsnapshot.getValue(User::class.java)
                    val receiveruid = postsnapshot.child("recent").value.toString()
                    val senderRoom = receiveruid + senderuid
                    val recentUser = postsnapshot.child(receiveruid).child("recent").value.toString()

                    if (receiveruid == "true"){
                        recentUserList.add(recent!!)
                    }

                    Toast.makeText(applicationContext,receiveruid,Toast.LENGTH_SHORT).show()


                    binding.userRecentRecycler.visibility = View.VISIBLE
                    binding.progbarr.visibility = View.GONE

                }






            }

            override fun onCancelled(error: DatabaseError) {


            }

        })















    }



















    }
