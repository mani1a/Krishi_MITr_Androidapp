package com.manila.fasaldoctor.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityRegisterBinding
import com.manila.fasaldoctor.fragments.ProfileFragment
import com.manila.fasaldoctor.usersdata.Usersexpert
import com.manila.fasaldoctor.usersdata.Usersfarmer

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabaseReference: DatabaseReference
    lateinit var email : String
    lateinit var userName : String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var bundle: Bundle
    lateinit var intentTohome :Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val radioRoleGroup = binding.radioRoleGroup
        val fragment = ProfileFragment()

        firebaseAuth = FirebaseAuth.getInstance()
//        var userId = FirebaseAuth.getInstance().currentUser?.uid
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        intentTohome = Intent(this,HomeActivity::class.java)


        radioRoleGroup.setOnCheckedChangeListener { group, checkedId ->

            val farmer = "farmer"
            val expert = "expert"
//            sharedPreferences = getSharedPreferences("checkedRole",Context.MODE_PRIVATE)
//            sharedPreferences.getString("checkedRole", null)

            binding.btnRegister.setOnClickListener {
                userName = binding.etUserName.text.toString()
                email = binding.editTextTextEmailAddress2.text.toString()
                val userFarmer = Usersfarmer(userName, email)
                val userExpert = Usersexpert(userName, email)
                createUsers()

                intentTohome.putExtra("userName",userName)

//            val uid = FirebaseAuth.getInstance().uid.toString()
//            println(uid)
//            Toast.makeText(this,uid,Toast.LENGTH_LONG).show()



                if (checkedId == R.id.farmer){

                    if (userName.isNotEmpty()){
                        firebaseDatabaseReference.child(farmer).child(userName).setValue(userFarmer)
                            .addOnCompleteListener {
                                if (it.isSuccessful) Toast.makeText(this,"Updated as Farmer",Toast.LENGTH_SHORT).show()
                                else Toast.makeText(this,"failed to update", Toast.LENGTH_SHORT).show()
                            }

                    }else{
                        Toast.makeText(this,"Fill", Toast.LENGTH_SHORT).show()

                    }
                    intentTohome.putExtra("role",farmer)


//                Toast.makeText(this,"farmer",Toast.LENGTH_SHORT).show()
//                sharedPreferences = getSharedPreferences("farmer",Context.MODE_PRIVATE)
//                sharedPreferences.getString("farmer", null)
//                sharedPreferences.edit().putString("checkedRole",farmer).apply()
//                bundle.putString("checkedRole",farmer)
//                fragment.arguments = bundle

                }

                if (checkedId == R.id.expert){

                    if (userName.isNotEmpty()){
                        firebaseDatabaseReference.child(expert).child(userName).setValue(userExpert)
                            .addOnCompleteListener {
                                if (it.isSuccessful) Toast.makeText(this,"updated as expert",Toast.LENGTH_SHORT).show()
                                else Toast.makeText(this,"failed ",Toast.LENGTH_SHORT).show()
                            }

                    }
                    else{
                        Toast.makeText(this,"Fill", Toast.LENGTH_SHORT).show()

                    }
                    intentTohome.putExtra("role",expert)


//                Toast.makeText(this,"expert",Toast.LENGTH_SHORT).show()
//                sharedPreferences = getSharedPreferences("expert",Context.MODE_PRIVATE)
//                sharedPreferences.getString("expert",null)
//                sharedPreferences.edit().putString("checkedRole",expert).apply()
//                bundle.putString("checkedRole",expert)
//                fragment.arguments = bundle

                }

//                sharedPreferences = getSharedPreferences(getString(R.string.user_name),Context.MODE_PRIVATE)
//                sharedPreferences.getString("userName",null)
//                sharedPreferences.edit().putString("userName",userName).apply()
//            bundle.putString("userName",userName)

            }






        }

//            fragment.arguments = bundle




        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }






    }

     private fun createUsers(){
//        userName = binding.etUserName.text.toString()
//        email = binding.editTextTextEmailAddress2.text.toString()
        val password = binding.editTextTextPassword2.text.toString()
        val confirmPassword = binding.editTextTextConfirmPassword2.text.toString()


        if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if (password == confirmPassword) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
//                            val intentToLogin = Intent(this, HomeActivity::class.java)
                            startActivity(intentTohome)

                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            } else {
                Toast.makeText(
                    this, "Password and Confirm Password should be same",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        else {
            Toast.makeText(
                this,
                "Email and Password should not be blank",
                Toast.LENGTH_SHORT
            )
                .show()
        }



    }
}
