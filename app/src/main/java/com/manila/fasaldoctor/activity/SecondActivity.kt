package com.manila.fasaldoctor.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivitySecondBinding
import java.util.Locale

class SecondActivity : AppCompatActivity() {

    lateinit var binding : ActivitySecondBinding
    lateinit var sharedPreferences : SharedPreferences

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySecondBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)

        sharedPreferences = getSharedPreferences("Hindi",Context.MODE_PRIVATE)
        val hindi = sharedPreferences.getBoolean("Hindi",false)

        if (isLoggedIn){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        grantPermission()

        var language :  String = ""
        val radioLanguage = binding.languagerolegroup

        radioLanguage.setOnCheckedChangeListener { group, checkedId ->

            if (checkedId == R.id.lang_eng){
                language = "English"
            }
            if (checkedId == R.id.lang_hindi){
                language = "Hindi"
            }

        }

//        val languageCode = "hi" // Replace with code to retrieve user's selected language (e.g., from SharedPreferences)
//        val resources = getResources()
//        val locale = Locale(languageCode)
//        val configuration = Configuration(resources.configuration)
//        configuration.setLocale(locale)
//        resources.updateConfiguration(configuration, resources.displayMetrics)


        binding.btnNext.setOnClickListener {


            if (language == "English"){

                changeLanguage("en")
                startActivity(Intent(this,RegisterActivity::class.java))
                finish()

            }
            else if (language == "Hindi"){

                changeLanguage("hi")
                sharedPreferences.edit().putBoolean("Hindi",true).apply()
                startActivity(Intent(this,RegisterActivity::class.java))
                finish()

            }

        }

    }

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

    private fun changeLanguage(code : String){
        val resources = resources
        val locale = Locale(code)
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

    }



    override fun onStop() {
        super.onStop()
        finish()
    }
}