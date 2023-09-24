
package com.manila.fasaldoctor.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.adapter.ChatViewPagerAdapter
import com.manila.fasaldoctor.adapter.UsersAdapter
import com.manila.fasaldoctor.database.FireBaseDataBase
import com.manila.fasaldoctor.databinding.ActivityChatMainBinding
import com.manila.fasaldoctor.fragments.ChatFragment
import com.manila.fasaldoctor.fragments.HomeFragment
import com.manila.fasaldoctor.fragments.ProfileFragment
import com.manila.fasaldoctor.fragments.RecentChatsFragment
import com.manila.fasaldoctor.model.User
import com.manila.fasaldoctor.utils.Layers
import java.io.File

class ChatMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatMainBinding
    lateinit var toolbar: Toolbar
    lateinit var userrecyclerView : RecyclerView
    lateinit var userrecyclerAdapter: UsersAdapter
    lateinit var layoutManager: GridLayoutManager
    lateinit var userList : ArrayList<User>
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var fbDatabase : DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var currentRole : String
    lateinit var role : String



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityChatMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "CHAT"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val fragmentArrayList = ArrayList<Fragment>()
        fragmentArrayList.add(ChatFragment())
        fragmentArrayList.add(RecentChatsFragment())
        val adapter = ChatViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)
        binding.viewPager.adapter = adapter
        binding.tab.setupWithViewPager(binding.viewPager)

        fbDatabase = FirebaseDatabase.getInstance().reference
        fbDatabase.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("role")
            .get().addOnSuccessListener {
                role = it.value.toString()

                if(role == "farmer") supportActionBar?.title = "Chat with Expert"
                else supportActionBar?.title = "Chat with Farmer"

            }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this,HomeActivity::class.java))
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        finish()
        super.onStop()
    }

}