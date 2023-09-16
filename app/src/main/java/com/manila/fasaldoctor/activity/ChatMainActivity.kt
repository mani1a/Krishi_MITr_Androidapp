
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
import com.manila.fasaldoctor.adapter.UsersAdapter
import com.manila.fasaldoctor.databinding.ActivityChatMainBinding
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

        firebaseAuth = FirebaseAuth.getInstance()
        fbDatabase = FirebaseDatabase.getInstance().getReference()

        //recycler view codes

        userList = ArrayList()
        userrecyclerAdapter = UsersAdapter(this,userList)
        userrecyclerView = findViewById(R.id.user_recycler)
        userrecyclerView.layoutManager = LinearLayoutManager(this)
        userrecyclerView.adapter = userrecyclerAdapter


        // users list ---- codes

        fbDatabase.child("Users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()

                val role = snapshot.child(firebaseAuth.currentUser!!.uid).child("role").value.toString()

                if (role == "farmer") supportActionBar?.title = "Chat With Experts"
                else supportActionBar?.title = "Chat with Farmers"

                for (postSnapshot in snapshot.children){

                    val currentUser = postSnapshot.getValue(User::class.java)
                    val farmer = "farmer";
                    val expert = "expert";

                    if ((firebaseAuth.currentUser?.uid != currentUser?.uid) && (currentUser?.role != role)){

                        userList.add(currentUser!!)

                        binding.userRecycler.visibility = View.VISIBLE
                        binding.progbarr.visibility = View.GONE

                    }

                }
                userrecyclerAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(applicationContext,"Some error Occurred , Please Restart the App",Toast.LENGTH_LONG).show()

            }

        })

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