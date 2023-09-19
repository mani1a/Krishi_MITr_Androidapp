package com.manila.fasaldoctor.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.wifi.WifiManager.SubsystemRestartTrackingCallback
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import com.manila.fasaldoctor.utils.Layers

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
    lateinit var userId : String
    lateinit var fcmToken : String
    lateinit var mobile : String
    lateinit var crop1 : String
    lateinit var crop2 : String
    lateinit var crop3 : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences(getString(R.string.prefrences_file_name),Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn",false)

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

            if (checkedId == R.id.farmer){
                role = farmer
                binding.imgGroup.visibility = View.GONE
                binding.imgFarmer.visibility = View.VISIBLE
                binding.imgExpert.visibility = View.GONE
                binding.dataLayout.visibility = View.VISIBLE
                binding.btnRegister.visibility = View.GONE
                binding.btnNext.visibility = View.VISIBLE

            }
            if (checkedId == R.id.expert){
                role = expert
                binding.imgGroup.visibility = View.GONE
                binding.imgFarmer.visibility = View.GONE
                binding.imgExpert.visibility = View.VISIBLE
                binding.dataLayout.visibility = View.VISIBLE
                binding.btnRegister.visibility = View.GONE
                binding.btnNext.visibility = View.VISIBLE

            }


            sharedPreferences = getSharedPreferences("checkedRole",Context.MODE_PRIVATE)
            sharedPreferences.getString("checkedRole", null)

            binding.btnNext.setOnClickListener {
                binding.dataLayout.visibility = View.GONE
                binding.selectcroplayout.visibility = View.VISIBLE
                binding.radioRoleGroup.visibility = View.GONE
                binding.btnNext.visibility = View.GONE
                binding.btnRegister.visibility = View.VISIBLE
                binding.rolechoosetext.text = "You are a : $role"

            }



            binding.checkPotato.setOnCheckedChangeListener { buttonView, isChecked ->
                crop1 = "Potato"

            }

            binding.checkTomato.setOnCheckedChangeListener { buttonView, isChecked ->
                crop2 = "Tomato"

            }



            binding.btnRegister.setOnClickListener {
                userName = binding.etUserName.text.toString()
                email = binding.editTextTextEmailAddress2.text.toString()
                mobile = binding.editTextMobile.text.toString()
                val userFarmer = Usersfarmer(userName, email, farmer)
                val userExpert = Usersexpert(userName, email, expert)
                crop3 = binding.edittextothercrop.text.toString()

                sharedPreferences = getSharedPreferences("userName",Context.MODE_PRIVATE)
                sharedPreferences.getString("userName",null)
                sharedPreferences.edit().putString("userName",userName).apply()

                sharedPreferences = getSharedPreferences("email",Context.MODE_PRIVATE)
                sharedPreferences.getString("email",null)
                sharedPreferences.edit().putString("email",email).apply()



                createUsers()


            }

        }


        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

     private fun createUsers(){

        val password = binding.editTextTextPassword2.text.toString()
        val confirmPassword = binding.editTextTextConfirmPassword2.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            if (password == confirmPassword) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { it ->
                        if (it.isSuccessful) {
                            storeData()

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

        //for storing data --- also store in profile fragment aLSO

        Layers.showProgressBar(this)

        val user : FirebaseUser? = firebaseAuth.currentUser
        userId = user!!.uid
//        val users = User(userName,email,role,userId,fcmToken,"","")
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            fcmToken = task.result
            users = User(userName,email,role,userId,fcmToken,"",mobile,"","",crop1,crop2,crop3)
            firebaseDatabaseReference.child(userId).setValue(users).addOnSuccessListener {
                Layers.hideProgressBar()
            }.addOnCompleteListener {
                if (it.isSuccessful) Toast.makeText(this,"Registered as $role",Toast.LENGTH_SHORT).show()
                else Toast.makeText(this,"failed ",Toast.LENGTH_SHORT).show()
            }

            // pass register data to profile fragment check krke dekhte hai
            intentTohome.putExtra("userName",userName)
            intentTohome.putExtra("email",email)
            intentTohome.putExtra("role",role)
            intentTohome.putExtra("userId",userId)
            intentTohome.putExtra("fcmToken",fcmToken)

        })

//        val profileFragment = ProfileFragment()
//        val bundle = Bundle()
////        bundle.putParcelable("userdata",users)
//        bundle.putString("userName",userName)
//        bundle.putString("email",email)
//        bundle.putString("role",role)
//        bundle.putString("userId",userId)
//        bundle.putString("fcmToken",fcmToken)
//        profileFragment.arguments = bundle

//        intentTohome.putExtra("usersdata", users as Serializable)
//
//        val profileFrag = ProfileFragment()
//        val bundleData = Bundle()
//        bundleData.putSerializable("userdata",users)
//        profileFrag.arguments = bundleData

//        firebaseDatabaseReference.child(userId).setValue(users).addOnCompleteListener {
//            if (it.isSuccessful) Toast.makeText(this,"updated",Toast.LENGTH_SHORT).show()
//            else Toast.makeText(this,"failed ",Toast.LENGTH_SHORT).show()
//        }



    }

    override fun onStop() {
        finish()
        super.onStop()
    }
}




