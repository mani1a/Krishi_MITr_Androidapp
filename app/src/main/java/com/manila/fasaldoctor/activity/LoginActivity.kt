package com.manila.fasaldoctor.activity

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityLoginBinding
import com.manila.fasaldoctor.utils.Layers

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()

        sharedPreferences = getSharedPreferences("Login",Context.MODE_PRIVATE)
        sharedPreferences.getBoolean("isLoggedIn",false)

        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            Layers.showProgressBar(this,"Logging In")

            if (email.isNotEmpty() && password.isNotEmpty()){

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        Layers.hideProgressBar()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                    else{
                        Layers.hideProgressBar()
                        Toast.makeText(this,it.exception.toString(), Toast.LENGTH_LONG).show()
                    }
                }

            }
            else{
                Toast.makeText(this,"Email and Password should not be blank", Toast.LENGTH_SHORT).show()
            }
        }



        val user = firebaseAuth.currentUser
        if (user != null ){
            startActivity(Intent(this,HomeActivity::class.java))
        }

        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()



    }

    override fun onStop() {
        finish()
        super.onStop()
    }

}