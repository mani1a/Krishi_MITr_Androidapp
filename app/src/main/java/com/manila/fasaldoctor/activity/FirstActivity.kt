package com.manila.fasaldoctor.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityFirstBinding
import java.util.Locale
import kotlin.math.log

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var progressDialog: ProgressDialog


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnGetStarted.setOnClickListener {
            Toast.makeText(this, "Get Started", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SecondActivity::class.java))
            finish()
        }

        binding.btnGetStarted.visibility = View.GONE

        val delay: Long = 2000

        Handler().postDelayed(
            {
                binding.btnGetStarted.visibility = View.VISIBLE
                binding.progressbar.visibility = View.GONE
//                startActivity(Intent(this, SecondActivity::class.java))
//                finish()
            }, delay
        )

            binding.lottieanim.postOnAnimationDelayed(
                {
//                grantPermission()
                    startActivity(Intent(this, SecondActivity::class.java))
                    finish()
                },delay)

        sharedPreferences = getSharedPreferences("Login",Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)

        if (isLoggedIn){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        sharedPreferences = getSharedPreferences("Hindi",Context.MODE_PRIVATE)
        val hindi = sharedPreferences.getBoolean("Hindi",false)

        if (hindi){
            changeLanguage("hi")
        }else{
            changeLanguage("en")
        }


    }

    private fun changeLanguage(code: String) {

        val resources = resources
        val locale = Locale(code)
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration,resources.displayMetrics)
    }

    // PERMISSION MANAGER/HANDLER
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun grantPermission(){

            val permissionList = mutableListOf<String>()

            if (checkSelfPermission(android.Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED){
                permissionList.add(android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
            }
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                permissionList.add(android.Manifest.permission.POST_NOTIFICATIONS)
            }
//            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//                permissionList.add(android.Manifest.permission.CAMERA)
//            }
//            if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
//                permissionList.add(android.Manifest.permission.RECORD_AUDIO)
//            }
//            if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
//                permissionList.add(android.Manifest.permission.READ_MEDIA_IMAGES)
//            }
//            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//                permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            }
//            if (checkSelfPermission(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//                permissionList.add(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)
//            }

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