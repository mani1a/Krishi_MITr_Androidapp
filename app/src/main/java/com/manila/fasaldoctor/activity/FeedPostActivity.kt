package com.manila.fasaldoctor.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityFeedPostBinding
import com.manila.fasaldoctor.databinding.FragmentFeedBinding
import com.manila.fasaldoctor.fragments.Feed2Fragment
import com.manila.fasaldoctor.model.PostData

class FeedPostActivity : AppCompatActivity() {
    lateinit var binding: ActivityFeedPostBinding
    lateinit var imageStored : Uri
    lateinit var postData : PostData
    lateinit var fireBaseDatabaseReference: DatabaseReference
    lateinit var fireBaseStorageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireBaseDatabaseReference = FirebaseDatabase.getInstance().reference
        fireBaseStorageReference = FirebaseStorage.getInstance().reference

        binding.openCameraPost.setOnClickListener {
            val picIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(picIntent,100)
            }catch (e:Exception){
                Toast.makeText(this,"Error" + e.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }


        val imageCon = registerForActivityResult(ActivityResultContracts.GetContent()){
            if (it != null) {
                imageStored = it
            }
            binding.imgViewFeedPost.setImageURI(imageStored)
        }
        binding.selectImagePost.setOnClickListener {
            imageCon.launch("image/*")

        }

        binding.btnSendPost.setOnClickListener {
            postData()
        }





    }


    private fun postData(){

        val post = binding.postTextQ.text.toString()
        val description = binding.postTextDesc.text.toString()
        val image : Uri = imageStored


        val posterId = FirebaseAuth.getInstance().currentUser!!.uid
        val time : Long = System.currentTimeMillis()

        fireBaseStorageReference.child("User_Posted_Images").child(posterId+time).putFile(image).addOnSuccessListener {
            task ->
            task.metadata?.reference?.downloadUrl?.addOnSuccessListener {

                val imageSend = it.toString()
                val postSend = PostData(posterId,post,description,imageSend)

                fireBaseDatabaseReference.child("PostsSend").child(posterId).setValue(postSend)
                    .addOnSuccessListener {
                        binding.btnSendPost.visibility = View.GONE
                        binding.feedPostProgressBar.visibility = View.VISIBLE
                        startActivity(Intent(this,Feed2Fragment::class.java))

                    Toast.makeText(this,"Post Send", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this,"Post not Send", Toast.LENGTH_SHORT).show()
                }
            }
        }








    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == RESULT_OK){

            val imageMap = data?.extras?.get("data") as Bitmap
            binding.imgViewFeedPost.setImageBitmap(imageMap)
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
}




