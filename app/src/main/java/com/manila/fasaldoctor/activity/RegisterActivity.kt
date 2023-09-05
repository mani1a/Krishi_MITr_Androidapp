package com.manila.fasaldoctor.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityRegisterBinding
import com.manila.fasaldoctor.model.User
import com.manila.fasaldoctor.model.Usersexpert
import com.manila.fasaldoctor.model.Usersfarmer

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabaseReference: DatabaseReference
    lateinit var email : String
    lateinit var userName : String
    lateinit var sharedPreferences: SharedPreferences
//    lateinit var bundle: Bundle
    lateinit var intentTohome :Intent
    lateinit var users : User
    lateinit var role:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val radioRoleGroup = binding.radioRoleGroup
//        val fragment = ProfileFragment()

        firebaseAuth = FirebaseAuth.getInstance()
//        var userId = FirebaseAuth.getInstance().currentUser?.uid
        firebaseDatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        intentTohome = Intent(this,HomeActivity::class.java)
        role = null.toString()


        radioRoleGroup.setOnCheckedChangeListener { group, checkedId ->

            val farmer = "farmer"
            val expert = "expert"
            sharedPreferences = getSharedPreferences("checkedRole",Context.MODE_PRIVATE)
            sharedPreferences.getString("checkedRole", null)

            binding.btnRegister.setOnClickListener {
                userName = binding.etUserName.text.toString()
                email = binding.editTextTextEmailAddress2.text.toString()
                val userFarmer = Usersfarmer(userName, email, farmer)
                val userExpert = Usersexpert(userName, email, expert)
//                val users = User(userName,email)
                

//                intentTohome.putExtra("userName",userName)
//            val uid = FirebaseAuth.getInstance().uid.toString()
//            println(uid)
//            Toast.makeText(this,uid,Toast.LENGTH_LONG).show()

                if (checkedId == R.id.farmer){
                    role = farmer

//                    users = User(userName,email,farmer)

//                    if (userName.isNotEmpty()){
//                        firebaseDatabaseReference.child(farmer).child(email).setValue(userFarmer)
//                            .addOnCompleteListener {
//                                if (it.isSuccessful) Toast.makeText(this,"Updated as Farmer",Toast.LENGTH_SHORT).show()
//                                else Toast.makeText(this,"failed to update", Toast.LENGTH_SHORT).show()
//                            }
////                        firebaseDatabaseReference.child("farmer").setValue(userFarmer).addOnCompleteListener {
////                            if (it.isSuccessful) Toast.makeText(this,"Updated as Farmer",Toast.LENGTH_SHORT).show()
////                                else Toast.makeText(this,"failed to update", Toast.LENGTH_SHORT).show()
////
////                        }
//
//                    }else{
//                        Toast.makeText(this,"Fill", Toast.LENGTH_SHORT).show()
//
//                    }
//                    intentTohome.putExtra("role",farmer)


//                Toast.makeText(this,"farmer",Toast.LENGTH_SHORT).show()
//                sharedPreferences = getSharedPreferences("farmer",Context.MODE_PRIVATE)
//                sharedPreferences.getString("farmer", null)
//                sharedPreferences.edit().putString("checkedRole",farmer).apply()
////                bundle.putString("checkedRole",farmer)
////                fragment.arguments = bundle
//
                }
                if (checkedId == R.id.expert){
                    role = expert
//                    users = User(userName,email,expert)

//                    if (userName.isNotEmpty()){
//                        firebaseDatabaseReference.child(expert).child(email).setValue(userExpert)
//                            .addOnCompleteListener {
//                                if (it.isSuccessful) Toast.makeText(this,"updated as expert",Toast.LENGTH_SHORT).show()
//                                else Toast.makeText(this,"failed ",Toast.LENGTH_SHORT).show()
//                            }
////                        firebaseDatabaseReference.child("expert").setValue(userExpert).addOnCompleteListener {
////                            if (it.isSuccessful) Toast.makeText(this,"Updated as Farmer",Toast.LENGTH_SHORT).show()
////                            else Toast.makeText(this,"failed to update", Toast.LENGTH_SHORT).show()
////
////                        }
//
//                    }
//                    else{
//                        Toast.makeText(this,"Fill", Toast.LENGTH_SHORT).show()
//
//                    }
////                    intentTohome.putExtra("role",expert)
//
//
////                Toast.makeText(this,"expert",Toast.LENGTH_SHORT).show()
////                sharedPreferences = getSharedPreferences("expert",Context.MODE_PRIVATE)
////                sharedPreferences.getString("expert",null)
//                sharedPreferences.edit().putString("checkedRole",expert).apply()
////                bundle.putString("checkedRole",expert)
////                fragment.arguments = bundle

                }
                sharedPreferences = getSharedPreferences("userName",Context.MODE_PRIVATE)
                sharedPreferences.getString("userName",null)
                sharedPreferences.edit().putString("userName",userName).apply()

                sharedPreferences = getSharedPreferences("email",Context.MODE_PRIVATE)
                sharedPreferences.getString("email",null)
                sharedPreferences.edit().putString("email",email).apply()
//            bundle.putString("userName",userName)
                createUsers()
//                role = rola


            }
//            intentTohome.putExtra("userName",userName)
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
                    .addOnCompleteListener { it ->
                        if (it.isSuccessful) {
                            storeData()

//                            val user : FirebaseUser? = firebaseAuth.currentUser
//                            val userId = user!!.uid
//                            val users = User(userName,email,role,userId,"","")
//
//                            firebaseDatabaseReference.child(userId).setValue(users).addOnCompleteListener {
//                                if (it.isSuccessful) Toast.makeText(this,"updated",Toast.LENGTH_SHORT).show()
//                                else Toast.makeText(this,"failed ",Toast.LENGTH_SHORT).show()
//                            }
//                            val intentToLogin = Intent(this, HomeActivity::class.java)
                            startActivity(intentTohome)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Password and Confirm Password should be same", Toast.LENGTH_SHORT).show()
            }
        }
        else {Toast.makeText(this, "Email and Password should not be blank", Toast.LENGTH_SHORT).show()
        }
    }

    private fun storeData(){
        var fcmToken :String?= null
        val user : FirebaseUser? = firebaseAuth.currentUser
        val userId = user!!.uid
//        val users = User(userName,email,role,userId,fcmToken,"","")

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {

                return@OnCompleteListener
            }

            // Get new FCM registration token
            fcmToken = task.result
            val users = User(userName,email,role,userId,fcmToken,"")

            firebaseDatabaseReference.child(userId).setValue(users).addOnCompleteListener {
                if (it.isSuccessful) Toast.makeText(this,"updated",Toast.LENGTH_SHORT).show()
                else Toast.makeText(this,"failed ",Toast.LENGTH_SHORT).show()
            }



        })

//        firebaseDatabaseReference.child(userId).setValue(users).addOnCompleteListener {
//            if (it.isSuccessful) Toast.makeText(this,"updated",Toast.LENGTH_SHORT).show()
//            else Toast.makeText(this,"failed ",Toast.LENGTH_SHORT).show()
//        }



    }
}
