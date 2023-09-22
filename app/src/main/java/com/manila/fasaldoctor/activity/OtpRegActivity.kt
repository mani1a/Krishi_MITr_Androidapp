package com.manila.fasaldoctor.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityOtpRegBinding
import kotlin.properties.Delegates

class OtpRegActivity : AppCompatActivity() {

    lateinit var binding: ActivityOtpRegBinding
    lateinit var userName : String
    lateinit var mobile : String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOtpRegBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        userName = binding.etUserName.text.toString()
        mobile = binding.editTextMobile.text.toString()

        binding.btnGetOtp.setOnClickListener {
//            if (userName.isNotEmpty() && mobile.isNotEmpty()){
                binding.otpLayout.visibility = View.VISIBLE
                binding.dataLayout.visibility = View.GONE
                binding.btnLayout.visibility = View.GONE
                binding.btnRegister.visibility = View.VISIBLE
//            }
        }




    }
}