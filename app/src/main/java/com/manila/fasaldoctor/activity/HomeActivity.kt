 package com.manila.fasaldoctor.activity

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.storage.FirebaseStorage
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityHomeBinding
import com.manila.fasaldoctor.fragments.HomeFragment
import com.manila.fasaldoctor.fragments.ProfileFragment
import com.manila.fasaldoctor.fragments.FeedFragment
import android.content.Intent
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
//import com.manila.fasaldoctor.fragments.Feed2Fragment
//import com.manila.fasaldoctor.fragments.FeedFragment
import com.manila.fasaldoctor.fragments.MoreFragment
import com.manila.fasaldoctor.utils.Layers


 class HomeActivity : AppCompatActivity() {

     private lateinit var binding: ActivityHomeBinding
     private lateinit var sharedPreferences: SharedPreferences
     private lateinit var firebaseStorageRefrence : FirebaseStorage
     private lateinit var databaseReference: DatabaseReference

     lateinit var progressDialog: ProgressDialog

//     lateinit var openCamera: Button
//     lateinit var frameLayout: FrameLayout
     lateinit var imgView: ImageView
//     lateinit var openGallery: Button
     lateinit var imageUri: Uri
     val REQUEST_CAMERA_CODE = 100
     lateinit var userName : String
     lateinit var role : String
     lateinit var email: String

     private var status : String? = null

     @RequiresApi(Build.VERSION_CODES.TIRAMISU)
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         binding = ActivityHomeBinding.inflate(layoutInflater)
         setContentView(binding.root)

         supportActionBar?.hide()
         replaceFragments(HomeFragment())

//         sharedPreferences= getSharedPreferences("checkedRole",Context.MODE_PRIVATE)
//         role = sharedPreferences.getString("checkedRole",null).toString()
//
//         sharedPreferences= getSharedPreferences("userName",Context.MODE_PRIVATE)
//         userName = sharedPreferences.getString("userName",null).toString()
//
//         sharedPreferences = getSharedPreferences("email",Context.MODE_PRIVATE)
//         email = sharedPreferences.getString("email",null).toString()

//         userName = intent.getStringExtra("userName").toString()
//         role = intent.getStringExtra("role").toString()
//         Toast.makeText(this,intent2,Toast.LENGTH_SHORT).show()
//         binding.txtView.text = email

//         val userName = intent.getStringExtra("userName")
//         val email = intent.getStringExtra("email")
//         val role = intent.getStringExtra("role")
//         val userId = intent.getStringExtra("userId")
//         val fcmToken = intent.getStringExtra("fcmToken")
//
//         val profileFragment = ProfileFragment()
//         val bundle = Bundle()
//         bundle.putString("userName",userName)
//         bundle.putString("role",role)
//         bundle.putString("email",email)
//         bundle.putString("userId",userId)
//         bundle.putString("fcmToken",fcmToken)
//         profileFragment.arguments = bundle


//         intentTohome.putExtra("",email)
//         intentTohome.putExtra("",role)
//         intentTohome.putExtra("",userId)
//         intentTohome.putExtra("",fcmToken)


//         binding.btnChat.setOnClickListener {
//             startActivity(Intent(this,RecentChatActivity::class.java))
////             Toast.makeText(this,"Chat with AI is under Development",Toast.LENGTH_SHORT).show()
//         }




         val bottomNavigationView:NavigationBarView  = binding.bottomNavigationView
         bottomNavigationView.isItemActiveIndicatorEnabled
         bottomNavigationView.setOnItemSelectedListener{

             when (it.itemId) {
                 R.id.home ->{
                     replaceFragments(HomeFragment())
//                     binding.btnChat.visibility = View.VISIBLE
                 }


                 R.id.feed -> {

                     replaceFragments(FeedFragment())
//                     val intent = Intent(this, FeedActivity::class.java)
//                     startActivity(intent)
////                     true // Return true to indicate that the item click is handled
//
////                     replaceFragments(Feed2Fragment())
////                     binding.btnChat.visibility = View.GONE
// =======
//                      val intent = Intent(this, FeedActivity::class.java)
//                      startActivity(intent)
//                      finish()
// //                     true // Return true to indicate that the item click is handled

// //                     replaceFragments(Feed2Fragment())
// //                     binding.btnChat.visibility = View.GONE
// >>>>>>> master
                 }
                 // Handle other menu items if needed

                 R.id.More -> {
                     replaceFragments(MoreFragment())
//                     binding.btnChat.visibility = View.VISIBLE
//                     startActivity(Intent(this, ProfileActivity::class.java))
//                     overridePendingTransition(R.anim.bottomnav_animation_slidein,
//                         R.anim.bottomnav_animation_slideout)
//                     finish()
                 }
             }

             true
         }

         databaseReference = FirebaseDatabase.getInstance().reference

         binding.fabChatAI.setOnClickListener {
             Toast.makeText(this,"Chat wit AI",Toast.LENGTH_SHORT).show()
         }





     }

     private fun replaceFragments(fragment: Fragment){
         val fragmentManager = supportFragmentManager
         val fragmentTransaction = fragmentManager.beginTransaction()
         fragmentTransaction.replace(R.id.frameLayout,fragment)
//         fragmentTransaction.addToBackStack("back")
         fragmentTransaction.commit()
     }

     override fun onStop() {
         finish()
         super.onStop()
     }











 }




















//     private fun uploadImage() {
//
//         progressDialog = ProgressDialog(this)
//         progressDialog.setTitle("Uploading ...")
//         progressDialog.show()

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

//         firebaseStorageRefrence.getReference("Images").child(System.currentTimeMillis().toString())
//             .putFile(imageUri).addOnSuccessListener{ task ->
//                 task.metadata?.reference?.downloadUrl?.addOnSuccessListener {
//                     if (true){
//                     val userId = FirebaseAuth.getInstance().currentUser?.uid
//                     val mapImage = mapOf("url" to it.toString())
//
//                         val dataBaseRef =  FirebaseDatabase.getInstance().getReference("Images Upload By User")
//                             .child(userId!!).setValue(mapImage).addOnSuccessListener {
//                                 Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show()
//                             }.addOnFailureListener{ _ ->
//                                 Toast.makeText(this,"Failed to upload",Toast.LENGTH_SHORT).show()
//                             }
//
//                         if (progressDialog.isShowing)progressDialog.dismiss()
//
//                     }
//                     else Toast.makeText(this,"Select Image First",Toast.LENGTH_SHORT).show()
//
//
//
//                 }?.addOnFailureListener{
//                     Toast.makeText(this,"Failed to upload",Toast.LENGTH_SHORT).show()
//                     if (progressDialog.isShowing)progressDialog.dismiss()
//
//
//                 }
//             }
//

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
