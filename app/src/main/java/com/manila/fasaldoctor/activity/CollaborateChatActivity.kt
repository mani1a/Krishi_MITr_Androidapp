package com.manila.fasaldoctor.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.adapter.UsersAdapter
import com.manila.fasaldoctor.databinding.ActivityCollaborateChatBinding
import com.manila.fasaldoctor.databinding.FragmentGovtShemesBinding
import com.manila.fasaldoctor.model.User

class CollaborateChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityCollaborateChatBinding
    lateinit var userrecyclerView : RecyclerView
    lateinit var userrecyclerAdapter: UsersAdapter
    lateinit var layoutManager: GridLayoutManager
    lateinit var userList : ArrayList<User>
    lateinit var fbDatabase : DatabaseReference
    lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollaborateChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.title = getString(R.string.collaborate_with_expert)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //recycler view codes
        userList = ArrayList()
        userrecyclerAdapter = UsersAdapter(this,userList)
//        userrecyclerView = view?.findViewById(R.id.user_recycler)!!
        userrecyclerView = binding.userRecycler
        userrecyclerView.layoutManager = LinearLayoutManager(this)
        userrecyclerView.adapter = userrecyclerAdapter
        fbDatabase = FirebaseDatabase.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()

        fbDatabase.child("Users").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()

                val role = snapshot.child(firebaseAuth.currentUser!!.uid).child("role").value.toString()

                for (postSnapshot in snapshot.children){

                    val currentUser = postSnapshot.getValue(User::class.java)

                    if ((firebaseAuth.currentUser?.uid != currentUser?.uid) && (currentUser?.role == role)){

                        userList.add(currentUser)

                        binding.userRecycler.visibility = View.VISIBLE
                        binding.progbarr.visibility = View.GONE

                    }




                }
                userrecyclerAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(applicationContext,"Some error Occurred , Please Restart the App",
                    Toast.LENGTH_LONG).show()

            }

        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this,HomeActivity::class.java))
        return super.onOptionsItemSelected(item)
    }
}