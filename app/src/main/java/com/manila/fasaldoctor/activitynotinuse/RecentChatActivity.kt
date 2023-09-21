package com.manila.fasaldoctor.activitynotinuse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.manila.fasaldoctor.activity.HomeActivity
import com.manila.fasaldoctor.databinding.ActivityRecentChatBinding

class RecentChatActivity : AppCompatActivity() {
    lateinit var binding : ActivityRecentChatBinding

//    lateinit var recentRecyclerView: RecyclerView
//    lateinit var recentUsersAdapter: RecentUsersAdapter
//    lateinit var recentUserList : ArrayList<RecentUser>
//
//    lateinit var firebaseDatabase: DatabaseReference
//    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecentChatBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        recentUserList = ArrayList()
//        recentRecyclerView = findViewById(R.id.user_recent_recycler)
//        recentUsersAdapter = RecentUsersAdapter(this,recentUserList)
//        recentRecyclerView.layoutManager = LinearLayoutManager(this)
//        recentRecyclerView.adapter = recentUsersAdapter
//
//        firebaseDatabase = FirebaseDatabase.getInstance().reference
//        firebaseAuth = FirebaseAuth.getInstance()
//
//        val senderuid = firebaseAuth.currentUser!!.uid
//
//        supportActionBar?.title = "Recent Chats"
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//
//        firebaseDatabase.child("RecentUsers").child(senderuid).addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                recentUserList.clear()
//
//                for (postsnap in snapshot.children){
//                    val recentUser = postsnap.getValue(RecentUser::class.java)
//
//                    recentUserList.add(recentUser!!)
//
//                    binding.userRecentRecycler.visibility = View.VISIBLE
//                    binding.progbarr.visibility = View.GONE
//                }
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        })


















    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this, HomeActivity::class.java))
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        finish()
        super.onStop()
    }



















    }
