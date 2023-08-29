package com.manila.fasaldoctor.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    lateinit var binding:ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null){
            firebaseAuth.currentUser?.let {
                binding.profileEmail.text = it.email
            }
        }

        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home ->{
                    startActivity(Intent(this,
                        com.manila.fasaldoctor.activity.HomeActivity::class.java))
                    overridePendingTransition(R.anim.bottomnav_animation_slidein,
                        R.anim.bottomnav_animation_slideout)
                    finish()
                }
                R.id.feed -> Toast.makeText(this,"Under Development",Toast.LENGTH_SHORT).show()

                R.id.profile -> true
            }

            true
        }

        sharedPreferences = getSharedPreferences(getString(R.string.prefrences_file_name),Context.MODE_PRIVATE)


        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(this,"Logged Out",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
            sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
        }
    }


}




