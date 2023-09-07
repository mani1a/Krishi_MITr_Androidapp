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


 class HomeActivity : AppCompatActivity() {

     private lateinit var binding: ActivityHomeBinding
     private lateinit var sharedPreferences: SharedPreferences
     private lateinit var firebaseStorageRefrence : FirebaseStorage
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



         val userName = intent.getStringExtra("userName")
         val email = intent.getStringExtra("email")
         val role = intent.getStringExtra("role")
         val userId = intent.getStringExtra("userId")
         val fcmToken = intent.getStringExtra("fcmToken")

         binding.txtView1.text = userName
         binding.txtView2.text = email
         binding.txtView3.text = role
         binding.txtView4.text = userId
         binding.txtView5.text = fcmToken

         val profileFragment = ProfileFragment()
         val bundle = Bundle()
         bundle.putString("userName",userName)
         bundle.putString("role",role)
         bundle.putString("email",email)
         bundle.putString("userId",userId)
         bundle.putString("fcmToken",fcmToken)
         profileFragment.arguments = bundle


//         intentTohome.putExtra("",email)
//         intentTohome.putExtra("",role)
//         intentTohome.putExtra("",userId)
//         intentTohome.putExtra("",fcmToken)


         binding.btnChat.setOnClickListener {
//             startActivity(Intent(this,ChatMainActivity::class.java))
             Toast.makeText(this,"Chat with AI is under Development",Toast.LENGTH_SHORT).show()
         }

//         sharedPreferences = getSharedPreferences(getString(R.string.prefrences_file_name), Context.MODE_PRIVATE)
//         val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

//         if(isLoggedIn){
//             startActivity(Intent(this,LoginActivity::class.java))
//         }else{
//
//         }
//
//
//         sharedPreferences = getSharedPreferences(getString(R.string.user_name),Context.MODE_PRIVATE)
//         var sharedUserName = sharedPreferences.getString("userName", null).toString()
//         Toast.makeText(this,sharedUserName,Toast.LENGTH_LONG).show()
//
//         sharedPreferences =getSharedPreferences("checkedRole",Context.MODE_PRIVATE)
//         var role = sharedPreferences.getString("checkedRole", null).toString()
//
//         firebaseStorageRefrence = FirebaseStorage.getInstance()
//
//         frameLayout = findViewById(R.id.frameLayout)
////         openCamera = findViewById(R.id.open_camera)
////         openGallery = findViewById(R.id.open_gallery)
//         imgView = findViewById(R.id.captured_image)

//         val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {
//             imgView.setImageURI(it)
//         }

//         val cloudContract = registerForActivityResult(ActivityResultContracts.GetContent()){
//             imgView.setImageURI(it)
//             if (it != null) {
//                 imageUri = it
//             }
//         }

//         binding.selectImage.setOnClickListener {
//             cloudContract.launch("image/*")
//             cloudContract.apply {
//                 showCustomDialogBox()
//             }
//         }
//
//         binding.btnFirebase.setOnClickListener {
//             uploadImage()
//         }


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
//         openCamera.setOnClickListener {
//             Toast.makeText(this, "open camera", Toast.LENGTH_SHORT).show()
//             val intent = Intent(this, CameraActivity::class.java)
//             startActivity(intent)
//         }

//         openCamera.setOnClickListener {
//             val takePicintent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//             try {
//                 startActivityForResult(takePicintent,REQUEST_CAMERA_CODE)
//             }catch (e : ActivityNotFoundException){
//                 Toast.makeText(this,"Error"+e.localizedMessage,Toast.LENGTH_LONG).show()
//             }
//
//         }

         val bottomNavigationView:NavigationBarView  = binding.bottomNavigationView
         bottomNavigationView.isItemActiveIndicatorEnabled
         bottomNavigationView.setOnItemSelectedListener{

             when (it.itemId) {
                 R.id.home -> replaceFragments(HomeFragment())

                 R.id.feed -> replaceFragments(FeedFragment())

                 R.id.profile -> {
                     replaceFragments(ProfileFragment())
//                     startActivity(Intent(this, ProfileActivity::class.java))
//                     overridePendingTransition(R.anim.bottomnav_animation_slidein,
//                         R.anim.bottomnav_animation_slideout)
//                     finish()
                 }
             }

             true
         }

//         binding.btnChat.setOnClickListener {
//             startActivity(Intent(this,ChatMainActivity::class.java))
////             Toast.makeText(this,"kaam chaalu hai .. sabar karo hahhahhahha",Toast.LENGTH_SHORT).show()
//         }
//
////         openGallery.setOnClickListener {
////             contract.launch("image/*")
////         }

//         sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()

//         Toast.makeText(this,intent2,Toast.LENGTH_SHORT).show()

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
//     }
//
//
//     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//         if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK){
//             val imagemap = data?.extras?.get("data") as Bitmap
//             imgView.setImageBitmap(imagemap)
//
//         }
//         else{
//             super.onActivityResult(requestCode, resultCode, data)
//
//         }
//
//     }

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

//     fun showCustomDialogBox(){
//         val dialog = Dialog(this)
//         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//         dialog.setCancelable(false)
//         dialog.setContentView(R.layout.layout_upload_cardview)
//         dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//         val image : ImageView = dialog.findViewById(R.id.showSelectedImg)
//         val uploadbtn : Button = dialog.findViewById(R.id.btn_upload)
//         val cancelbtn: Button = dialog.findViewById(R.id.btn_cancel)
////        image.setImageURI(photo)
//         uploadbtn.setOnClickListener {
//             uploadprofImg()
//         }
//         cancelbtn.setOnClickListener {
//             dialog.dismiss()
//         }
//         dialog.show()
//
//     }

//     private fun uploadprofImg() {
//         
//     }
     private fun replaceFragments(fragment: Fragment){
         val fragmentManager = supportFragmentManager
         val fragmentTransaction = fragmentManager.beginTransaction()
         fragmentTransaction.replace(R.id.frameLayout,fragment)
//         fragmentTransaction.addToBackStack("back")
         fragmentTransaction.commit()
     }


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
