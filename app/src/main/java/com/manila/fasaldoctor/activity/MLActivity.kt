package com.manila.fasaldoctor.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityMlactivityBinding

class MLActivity : AppCompatActivity() {
    lateinit var binding: ActivityMlactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMlactivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()


        binding.openCamera.setOnClickListener {
            val takePicintent =Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try {
                startActivityForResult(takePicintent, 100)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Error" + e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }

        val contract = registerForActivityResult(
            ActivityResultContracts
            .GetContent()){
            binding.imgView.setImageURI(it)
        }

        binding.selectImage.setOnClickListener {
            contract.launch("image/*")

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK){
            val imagemap = data?.extras?.get("data") as Bitmap
            binding.imgView.setImageBitmap(imagemap)

        }
        else{
            super.onActivityResult(requestCode, resultCode, data)

        }
    }
}