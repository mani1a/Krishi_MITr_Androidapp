package com.manila.fasaldoctor.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var progressDialog: ProgressDialog

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        grantPermission()
        setContentView(binding.root)

        supportActionBar?.hide()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Are intezar karo thora load ho raha h -- Nhi ho raha to ek baar click kr lo")


        binding.btnGetStarted.setOnClickListener {
            progressDialog.show()
            startActivity(Intent(this,LoginActivity::class.java))
            if (progressDialog.isShowing)progressDialog.dismiss()
            finish()
        }

        sharedPreferences = getSharedPreferences(getString(R.string.prefrences_file_name),Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)
//
        if (isLoggedIn){
            startActivity(Intent(this,LoginActivity::class.java))
        }
//        else{
////            Toast.makeText(this,"Something Error Occurred",Toast.LENGTH_SHORT).show()
//
//        }


//        val user = firebaseAuth.currentUser
//
//        if ( user!= null ){
//            startActivity(Intent(this,HomeActivity::class.java))
//        }

        }



        // PERMISSION MANAGER/HANDLER
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun grantPermission(){
            val permissionList = mutableListOf<String>()

            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                permissionList.add(android.Manifest.permission.CAMERA)
            }
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                permissionList.add(android.Manifest.permission.READ_MEDIA_IMAGES)
            }

            if (checkSelfPermission(android.Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED){
                permissionList.add(android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
            }

            if (permissionList.size > 0){
                requestPermissions(permissionList.toTypedArray(),101)
            }

        }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            grantResults.forEach {
                if (it != PackageManager.PERMISSION_GRANTED) grantPermission()
            }
        }

    override fun onStop() {
        super.onStop()
        finish()
    }

}