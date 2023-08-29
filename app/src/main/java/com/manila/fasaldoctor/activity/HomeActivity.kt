 package com.manila.fasaldoctor.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityHomeBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


 class HomeActivity : AppCompatActivity() {


     private lateinit var binding: ActivityHomeBinding

     private lateinit var sharedPreferences: SharedPreferences

     private lateinit var firebaseStorageRefrence : FirebaseStorage

//     lateinit var imageNameFormatter : SimpleDateFormat

     lateinit var progressDialog: ProgressDialog
     lateinit var openCamera: Button
     lateinit var frameLayout: FrameLayout
     lateinit var imgView: ImageView
     lateinit var openGallery: Button
     lateinit var imageUri: Uri




     @RequiresApi(Build.VERSION_CODES.TIRAMISU)
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         binding = ActivityHomeBinding.inflate(layoutInflater)
         setContentView(binding.root)

         firebaseStorageRefrence = FirebaseStorage.getInstance()

         val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {
             imgView.setImageURI(it)
         }

         val cloudContract = registerForActivityResult(ActivityResultContracts.GetContent()){
             imgView.setImageURI(it)
             if (it != null) {
                 imageUri = it
             }
         }

         sharedPreferences = getSharedPreferences(getString(R.string.prefrences_file_name), Context.MODE_PRIVATE)
         val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

         binding.selectImage.setOnClickListener {
             cloudContract.launch("image/*")
         }

         binding.btnFirebase.setOnClickListener {
             uploadImage()
         }

         //donot move it from here



//        grantPermission()
//        replaceFragments(HomeFragment())

//         binding.btnLogout.setOnClickListener {
//             if (isLoggedIn) {
//                 sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
//                 startActivity(Intent(this, LoginActivity::class.java))
////                Toast.makeText(this,"LoggedIn",Toast.LENGTH_SHORT).show()
//             } else {
////                sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
////                startActivity(Intent(this,LoginActivity::class.java))
//                 Toast.makeText(this, "LoggedIn", Toast.LENGTH_SHORT).show()
//             }
//         }

         frameLayout = findViewById(R.id.frameLayout)
         openCamera = findViewById(R.id.open_camera)
         openGallery = findViewById(R.id.open_gallery)
         imgView = findViewById(R.id.captured_image)


         openCamera.setOnClickListener {
             Toast.makeText(this, "open camera", Toast.LENGTH_SHORT).show()
             val intent = Intent(this, CameraActivity::class.java)
             startActivity(intent)
         }

         val bottomNavigationView:NavigationBarView  = binding.bottomNavigationView
         bottomNavigationView.isItemActiveIndicatorEnabled
         bottomNavigationView.setOnItemSelectedListener{

             when (it.itemId) {
                 R.id.home -> true

                 R.id.feed -> Toast.makeText(this, "Under Development", Toast.LENGTH_SHORT).show()

                 R.id.profile -> {
                     startActivity(Intent(this, ProfileActivity::class.java))
                     overridePendingTransition(R.anim.bottomnav_animation_slidein,
                         R.anim.bottomnav_animation_slideout)
                     finish()
                 }
             }

             true
         }

         binding.btnChat.setOnClickListener {
             Toast.makeText(this,"Under Development",Toast.LENGTH_SHORT).show()
         }

         openGallery.setOnClickListener {
             contract.launch("image/*")
         }

         sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()

     }

     private fun uploadImage() {

         progressDialog = ProgressDialog(this)
         progressDialog.setTitle("Uploading ...")
         progressDialog.show()

//         imageNameFormatter = SimpleDateFormat("FD_yyyy_MM_dd_HH_mm_ss", Locale.US)
//         val dateNow = Date()


//         val imagename = imageNameFormatter.format(dateNow)

//         firebaseStorageRefrence = FirebaseStorage.getInstance().getReference("Images/$imagename")
//         firebaseStorageRefrence.putFile(imageUri).
//         addOnSuccessListener(OnSuccessListener {
//             binding.capturedImage.setImageURI(null)
//             Toast.makeText(this,"Image Uploaded Succesfully",Toast.LENGTH_SHORT).show()
//             if (progressDialog.isShowing)progressDialog.dismiss()
//
//         },).addOnFailureListener(OnFailureListener {
//             if (progressDialog.isShowing)progressDialog.dismiss()
//             Toast.makeText(this,"Failed to upload",Toast.LENGTH_SHORT).show()
//
//         })

         firebaseStorageRefrence.getReference("Images").child(System.currentTimeMillis().toString())
             .putFile(imageUri).addOnSuccessListener{ task ->
                 task.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                     var userId = FirebaseAuth.getInstance().currentUser?.uid

                     val mapImage = mapOf("url" to it.toString())

                     val dataBaseRef =  FirebaseDatabase.getInstance().getReference("user Images")
                         .child(userId!!).setValue(mapImage).addOnSuccessListener {
                             Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show()
                         }.addOnFailureListener{error ->
                             Toast.makeText(this,"Failed to upload",Toast.LENGTH_SHORT).show()
                         }

                     if (progressDialog.isShowing)progressDialog.dismiss()

                 }?.addOnFailureListener{
                     Toast.makeText(this,"Failed to upload",Toast.LENGTH_SHORT).show()
                     if (progressDialog.isShowing)progressDialog.dismiss()


                 }
             }

     }

//     private fun selectImage() {
//         val intent = Intent()
//         intent.setType("image/*")
//         intent.setAction(Intent.ACTION_GET_CONTENT)
//         startActivityForResult(intent,100)
//     }
//
//     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//         super.onActivityResult(requestCode, resultCode, data)
//         if(resultCode == 100 && data != null){
//
//             imageUri = data.data!!
//             binding.capturedImage.setImageURI(imageUri)
//
//         }
//     }

//     @Deprecated("Deprecated in Java")
//     override fun onBackPressed() {
//         super.onBackPressed()
//         finish()
//
//     }

//     override fun onStop() {
//         super.onStop()
//         finish()
//     }


 }





















//        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//
//                R.id.home -> startActivity(Intent(this,HomeActivity::class.java))
//
//                R.id.feed -> {
//                    Toast.makeText(this@HomeActivity,"Home",Toast.LENGTH_SHORT).show()
////                    replaceFragments(HomeFragment())
//                }
////                R.id.btnMain -> replaceFragments(ProfileFragment())
//                R.id.profile ->{
//
//                    startActivity(Intent(this,ProfileActivity::class.java))
////                replaceFragments(ProfileFragment())
//                }
//
//            }
//            true

//        }


//     private fun replaceFragments(fragment: Fragment){
//         val fragmentManager = supportFragmentManager
//         val fragmentTransaction = fragmentManager.beginTransaction()
//         fragmentTransaction.replace(R.id.frameLayout,fragment)
//         fragmentTransaction.commit()
//     }

//    // PERMISSION MANAGER/HANDLER
//
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    fun grantPermission(){
//        val permissionList = mutableListOf<String>()
//
//        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//            permissionList.add(android.Manifest.permission.CAMERA)
//        }
//        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        }
//        if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
//            permissionList.add(android.Manifest.permission.READ_MEDIA_IMAGES)
//        }
//
//        if (permissionList.size > 0){
//            requestPermissions(permissionList.toTypedArray(),101)
//        }
//
//    }
//
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        grantResults.forEach {
//            if (it != PackageManager.PERMISSION_GRANTED) grantPermission()
//        }
//    }
