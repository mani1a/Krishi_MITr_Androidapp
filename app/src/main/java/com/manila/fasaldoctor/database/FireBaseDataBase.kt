package com.manila.fasaldoctor.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FireBaseDataBase {

    lateinit var fireBaseDatabaseReference: DatabaseReference
    lateinit var firebaseStorage: StorageReference
    lateinit var firebaseAuth: FirebaseAuth

    fun fireBaseDataBasetoshowchatimages(){

        fireBaseDatabaseReference = FirebaseDatabase.getInstance().reference

        fireBaseDatabaseReference.child("chats").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}