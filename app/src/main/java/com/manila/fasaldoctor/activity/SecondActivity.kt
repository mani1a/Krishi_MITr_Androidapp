package com.manila.fasaldoctor.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    lateinit var binding : ActivitySecondBinding
    lateinit var sharedPreferences : SharedPreferences


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySecondBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences(getString(R.string.prefrences_file_name), Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)

        if (isLoggedIn){
            startActivity(Intent(this,LoginActivity::class.java))
        }

        binding.btnNext.setOnClickListener {

                startActivity(Intent(this,RegisterActivity::class.java))

        }


    }


    override fun onStop() {
        super.onStop()
        finish()
    }
}