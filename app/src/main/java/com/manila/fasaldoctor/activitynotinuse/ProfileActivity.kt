// Not in use

package com.manila.fasaldoctor.activitynotinuse

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.activity.HomeActivity
import com.manila.fasaldoctor.activity.LoginActivity
import com.manila.fasaldoctor.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    lateinit var binding:ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseDatabaseRef: FirebaseDatabase
//    lateinit var intent: Intent
    lateinit var firebasestorageReference: FirebaseStorage
    lateinit var imageUri: Uri
    lateinit var sharedUserName: String
    lateinit var role: String
    lateinit var photo : Uri
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabaseRef = FirebaseDatabase.getInstance()
        firebasestorageReference = FirebaseStorage.getInstance()

//        firebaseDatabaseRef.getReference("Users")

        sharedPreferences = getSharedPreferences(getString(R.string.user_name),Context.MODE_PRIVATE)
        sharedUserName = sharedPreferences.getString("userName",null).toString()
        Toast.makeText(this,sharedUserName,Toast.LENGTH_LONG).show()

        sharedPreferences =getSharedPreferences("checkedRole",Context.MODE_PRIVATE)
        role = sharedPreferences.getString("checkedRole",null).toString()

        if (role != null) {
            if (sharedUserName != null) {
                firebaseDatabaseRef.getReference("Users").child(role).child(sharedUserName).get()
                    .addOnSuccessListener {
                        val name = it.child("name").value
                        val email = it.child("email").value
                        binding.profileUserName.text = name.toString()
                        binding.profileEmail.text = email.toString()
                    }
            }
        }


        val upprofilrImg = binding.uploadprofileimg
        var profileImg = binding.profileImg

        val contract = registerForActivityResult(ActivityResultContracts.GetContent()){
            profileImg.setImageURI(it)
            if (it != null){

                imageUri = it

            }
        }
//        photo = imageUri

        upprofilrImg.setOnClickListener {
            contract.launch("image/*")
            contract.apply {
            showCustomDialogBox()
            }


//            contract.launch("image/*")

//            if (imageUri !=null){
//
//            uploadprofImg()
//            }
//            else{
//                Toast.makeText(this,"Some Error Occured",Toast.LENGTH_SHORT).show()
//            }



        }

















//        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
//        println(uid)
//        Toast.makeText(this,uid,Toast.LENGTH_LONG).show()



//        if (userName != null) {
//            firebaseDatabaseRef.getReference("Users").child("farmer").child(userName).get()
//                .addOnSuccessListener {
//                val name = it.child("name").value
//                val email = it.child("email").value
//                binding.profileUserName.text = name.toString()
//                binding.profileEmail.text = email.toString()
////                Toast.makeText(this,"Error Occured",Toast.LENGTH_LONG).show()
//            }
//        }else Toast.makeText(this,"Error Occured",Toast.LENGTH_LONG).show()

//        if (firebaseAuth.currentUser != null){
//            firebaseAuth.currentUser?.let {
//                binding.profileEmail.text = it.email
//            }
//        }







//        val intent = intent.getStringExtra("user_name")
//        val userName = binding.profileUserName.apply {
//            text = intent
//        }
//        userName.text = intent.getStringExtra("UserName")
//        println(userName)

        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home ->{
                    startActivity(Intent(this,
                        HomeActivity::class.java))
                    overridePendingTransition(R.anim.bottomnav_animation_slidein,
                        R.anim.bottomnav_animation_slideout)
                    finish()
                }
                R.id.feed -> Toast.makeText(this,"Under Development",Toast.LENGTH_SHORT).show()

                R.id.profile -> true
            }

            true
        }

        //shared prefrences for Logout
        sharedPreferences = getSharedPreferences(getString(R.string.prefrences_file_name),Context.MODE_PRIVATE)
        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(this,"Logged Out",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
        }
    }

    fun uploadprofImg(){

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading")
        progressDialog.show()

        firebasestorageReference.getReference("Users_profile_images").child(role).child(sharedUserName)
            .putFile(imageUri).addOnSuccessListener {task ->
                task.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    val uploadImg = mapOf("url" to it.toString())

                    FirebaseDatabase.getInstance().getReference("Users").child(role).child(sharedUserName)
                        .child(userId!!).setValue(uploadImg).addOnSuccessListener {
                            Toast.makeText(this,"Profile Updated",Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(this,"Failed to Upload",Toast.LENGTH_SHORT).show()
                        }

                    if (progressDialog.isShowing)progressDialog.dismiss()

                }?.addOnFailureListener {
                    Toast.makeText(this,"Failed to upload",Toast.LENGTH_SHORT).show()
                    if (progressDialog.isShowing)progressDialog.dismiss()

                }

            }
    }

    fun showCustomDialogBox(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_upload_cardview)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val image : ImageView = dialog.findViewById(R.id.showSelectedImg)
        val uploadbtn : Button = dialog.findViewById(R.id.btn_upload)
        val cancelbtn: Button = dialog.findViewById(R.id.btn_cancel)

//        image.setImageURI(photo)

        uploadbtn.setOnClickListener {
            uploadprofImg()
        }

        cancelbtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }


}




