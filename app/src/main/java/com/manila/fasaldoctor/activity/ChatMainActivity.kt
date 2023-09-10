
package com.manila.fasaldoctor.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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


        // users list codes

        Layers.showProgressBar(this)
        fbDatabase.child("Users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()

                val role = snapshot.child(firebaseAuth.currentUser!!.uid).child("role").value.toString()

                if (role == "farmer") supportActionBar?.title = "Chat With Experts"
                else supportActionBar?.title = "Chat with Farmers"


//                Toast.makeText(applicationContext,role,Toast.LENGTH_SHORT).show()


                for (postSnapshot in snapshot.children){

                    val currentUser = postSnapshot.getValue(User::class.java)
                    val farmer = "farmer";
                    val expert = "expert";

                    if ((firebaseAuth.currentUser?.uid != currentUser?.uid) && (currentUser?.role != role)){

                        userList.add(currentUser!!)

                    }

                    Layers.hideProgressBar()

                }
                userrecyclerAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(applicationContext,"Some error Occurred , Please Restrat",Toast.LENGTH_LONG).show()

            }

        })
//
//        //Code to show Profile Images
//        val userId = firebaseAuth.currentUser?.uid
//        val storageReference : StorageReference = firebaseStorage.reference
//        val imageRef = storageReference.child("Users_Profile_Images/$userId")
//        val localFile = File.createTempFile("ProfileImg","jpeg")
//        imageRef.getFile(localFile).addOnSuccessListener {
//            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
////            binding?.profileImg?.setImageBitmap(bitmap)
//        }.addOnFailureListener {
//            Toast.makeText(this,"Some Error Occureed",Toast.LENGTH_SHORT).show()
//        }


//
//        binding.btnidback.setOnClickListener {
//            startActivity(Intent(this,HomeActivity::class.java))
//        }


    }
//        getuserList()
//        initializeRecycler()



//        supportActionBar?.setTitle("Chat")
//        supportActionBar?.show()

//        setActionBar(toolbar)
//        supportActionBar?.title = "Chat With Experts"

//        userrecyclerView.layoutManager = LinearLayoutManager

//        var userList = ArrayList<User>()
//        userList.add(User("Manila","manila@gmail.com","Farmer","badBoy"))

//    private fun initializeRecycler(){
//        userrecyclerView = findViewById(R.id.user_recycler)
//        layoutManager = GridLayoutManager(this,3)
//        userList = userList
//        userrecyclerAdapter= UsersAdapter(this, userList)
//        userrecyclerView.adapter = userrecyclerAdapter
//        userrecyclerView.layoutManager = layoutManager
//
//    }




//    private fun getuserList(){
////        var userList = ArrayList<User>()
////        userList.add(User("Manila", "manila@gmail.com", "Farmer", "badBoy"))
////        userList.add(User("Manila", "manila@gmail.com", "Farmer", "badBoy"))
////        userList.add(User("Manila", "manila@gmail.com", "Farmer", "badBoy"))
////        userList.add(User("Manila", "manila@gmail.com", "Farmer", "badBoy"))
////        userList.add(User("Manila", "manila@gmail.com", "Farmer", "badBoy"))
////        userList.add(User("Manila", "manila@gmail.com", "Farmer", "badBoy"))
////        userList.add(User("Manila", "manila@gmail.com", "Farmer", "badBoy"))
////        userList.add(User("Manila", "manila@gmail.com", "Farmer", "badBoy"))
////        userList.add(User("Manila", "manila@gmail.com", "Farmer", "badBoy"))
////        userList.add(User("Manila", "manila@gmail.com", "Farmer", "badBoy"))
////        userList.add(User("Manila", "manila@gmail.com", "Farmer", "badBoy"))
////        userList.add(User("Manila", "manila@gmail.com", "Farmer", "badBoy"))
////        userList.add(User("Manila", "manila@gmail.com", "Farmer", "badBoy"))
////
////
////        return userList
//
//        var firebaseUser : FirebaseUser = FirebaseAuth.getInstance().currentUser!!
//        var fbdatabaseref : DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
//
//        fbdatabaseref.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                userList.clear()
//
//                for (dataSnapShot : DataSnapshot in snapshot.children){
//                    val user = dataSnapShot.getValue(User::class.java)
//
//                    if (user!!.equals(firebaseUser.uid)){
//                        userList.add(user)
//
//                    }
//
//                }
////                userrecyclerAdapter= UsersAdapter(applicationContext , userList)
//
//
//
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
//
//
//            }
//        })
////        return getuserList()
//
////        initializeRecycler()
//
//
//
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this,HomeActivity::class.java))
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        finish()
        super.onStop()
    }



}