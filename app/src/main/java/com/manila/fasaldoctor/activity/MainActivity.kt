 package com.manila.fasaldoctor.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityMainBinding
import com.manila.fasaldoctor.fragments.HomeFragment
import com.manila.fasaldoctor.fragments.ProfileFragment

 class MainActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainBinding
    lateinit var btnMain : ImageButton
    lateinit var bottomNav : BottomNavigationView
    lateinit var frameLayout: FrameLayout
    lateinit var constraintLayout: ConstraintLayout
    lateinit var imgView : ImageView
    lateinit var btnChange : ImageButton

    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()){
        imgView.setImageURI(it)

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        grantPermission()

        frameLayout = findViewById(R.id.frameLayout)
        bottomNav = findViewById(R.id.bottomNavigationView)
//        replaceFragments(HomeFragment())

        btnMain = findViewById(R.id.btnMain2)

        btnChange = findViewById(R.id.open_gallery)
        imgView = findViewById(R.id.captured_image)


        btnMain.setOnClickListener {
            Toast.makeText(this,"open camera",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){

                R.id.home -> {
                    Toast.makeText(this@MainActivity,"Home",Toast.LENGTH_SHORT).show()
                    replaceFragments(HomeFragment())
                }
//                R.id.btnMain -> replaceFragments(ProfileFragment())
                R.id.profile ->{
                    Toast.makeText(this@MainActivity,"Home",Toast.LENGTH_SHORT).show()
                replaceFragments(ProfileFragment())
                }

            }
            true
        }

        btnChange.setOnClickListener {
            contract.launch("image/*")
        }

    }


     private fun replaceFragments(fragment: Fragment){
         val fragmentManager = supportFragmentManager
         val fragmentTransaction = fragmentManager.beginTransaction()
         fragmentTransaction.replace(R.id.frameLayout,fragment)
         fragmentTransaction.commit()
     }


     @Deprecated("Deprecated in Java")
     override fun onBackPressed() {
         super.onBackPressed()
         finish()

     }



    // PERMISSION MANAGER/HANDLER

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun grantPermission(){
        val permissionList = mutableListOf<String>()

        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(android.Manifest.permission.CAMERA)
        }
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(android.Manifest.permission.READ_MEDIA_IMAGES)
        }

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
}