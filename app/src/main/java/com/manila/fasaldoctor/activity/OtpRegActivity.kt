package com.manila.fasaldoctor.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityOtpRegBinding
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class OtpRegActivity : AppCompatActivity() {

    lateinit var binding: ActivityOtpRegBinding
    lateinit var userName : String
    lateinit var mobile : String
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var verificationID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOtpRegBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        binding.btnGetOtp.setOnClickListener {
            userName = binding.etUserName.text.toString()
            mobile = "+91${binding.editTextMobile.text.toString()}"
            if (userName.isNotEmpty() && mobile.isNotEmpty()){

                val option = PhoneAuthOptions.newBuilder()
                    .setPhoneNumber(mobile)
                    .setTimeout(120,TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                        override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                        }

                        override fun onVerificationFailed(p0: FirebaseException) {
                            Toast.makeText(applicationContext,"Please Try Again",Toast.LENGTH_SHORT).show()
                        }

                        override fun onCodeSent(
                            p0: String,
                            p1: PhoneAuthProvider.ForceResendingToken
                        ) {
                            verificationID = p0
                            super.onCodeSent(p0, p1)
                        }

                    }).build()

                PhoneAuthProvider.verifyPhoneNumber(option)

                binding.otpLayout.visibility = View.VISIBLE
                binding.dataLayout.visibility = View.GONE
                binding.btnLayout.visibility = View.GONE
                binding.btnRegister.visibility = View.VISIBLE
            }else{
                Toast.makeText(this,"Fill Required Fields",Toast.LENGTH_SHORT).show()
            }
        }

        val otp = binding.editTextMobileotp.text.toString()

        binding.btnRegister.setOnClickListener {
            if (otp.isNotEmpty()){

                val credential = PhoneAuthProvider.getCredential(verificationID,otp)

                firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful){
                        startActivity(Intent(this,HomeActivity::class.java))
                        finish()

                    }else{
                        Toast.makeText(this,"Error : ${it.exception}",Toast.LENGTH_SHORT).show()

                    }
                }
            }else{
                Toast.makeText(this,"Enter OTP",Toast.LENGTH_SHORT).show()

            }
        }

    }
}