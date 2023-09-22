package com.manila.fasaldoctor.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
//import com.google.mlkit.vision.common.InputImage
//import com.google.mlkit.vision.label.ImageLabeling
//import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityMlactivityBinding
import com.manila.fasaldoctor.ml.PotatoDisease
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException

class MLActivity : AppCompatActivity() {

    lateinit var binding: ActivityMlactivityBinding

    private var imageMap : Bitmap? = null
    lateinit var imageUri : Uri
    lateinit var imgView: ImageView

    lateinit var firebaseDatabase: DatabaseReference
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMlactivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "Heal Your Crops"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imgView = binding.imgView
        firebaseDatabase = FirebaseDatabase.getInstance().reference

        val permission = arrayOf(android.Manifest.permission.CAMERA)

        binding.openCamera.setOnClickListener {
            val takePicintent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,permission,101)

            }else{

                try {
                    startActivityForResult(takePicintent, 100)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, "Error" + e.localizedMessage, Toast.LENGTH_LONG).show()
                }

            }
        }

        binding.selectImage.setOnClickListener {
            val pic = Intent(MediaStore.ACTION_PICK_IMAGES)

            try {
                startActivityForResult(pic,105)
            } catch (e : ActivityNotFoundException){
                Toast.makeText(this, "Error" + e.localizedMessage, Toast.LENGTH_LONG).show()

            }

        }

        binding.btnSeeresult.setOnClickListener {

            if (imageMap != null){
                tflitePotatoDisease()
                binding.MLmoreinfo.visibility = View.VISIBLE
                binding.MLchatwithExpert.visibility = View.VISIBLE

            }else{
                Toast.makeText(this,"Please Select Image",Toast.LENGTH_SHORT).show()
            }

        }

        binding.MLchatwithExpert.setOnClickListener {
            startActivity(Intent(this,ChatMainActivity::class.java))
            finish()
        }



    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK){

            imageMap = data?.extras?.get("data") as Bitmap
            imgView.setImageBitmap(imageMap)

        }
        else if(requestCode == 105 && resultCode == RESULT_OK){

           val selectedImage = data?.data

            imageMap = selectedImage?.let { getBitmapFromUri(it) }!!
            imgView.setImageBitmap(imageMap)

        }
        else{

            super.onActivityResult(requestCode, resultCode, data)

        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {

        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this,HomeActivity::class.java))
        return super.onOptionsItemSelected(item)
    }

    // ML PART
    private fun tflitePotatoDisease(){

        val labels = application.assets.open("labels.txt").bufferedReader().readLines()

        val imageProcessor = ImageProcessor.Builder()
//            .add(NormalizeOp(0.0f,255.0f))
            .add(
                ResizeOp(256,256,ResizeOp.ResizeMethod.BILINEAR)
            ).build()

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(imageMap)

        tensorImage = imageProcessor.process(tensorImage)

        val model = PotatoDisease.newInstance(this)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

        var maxId = 0
        var confidence : Float = 0.00f
        outputFeature0.forEachIndexed{index, fl ->

            if (outputFeature0[maxId] < fl){
                maxId = index
                confidence = fl
            }
        }

        binding.tvResult.text = "Disease : ${labels[maxId]}"
        binding.tvResultConfidence.text = confidence.toString()

        firebaseDatabase.child("Diseases").child("Potato").child(labels[2]).get()
            .addOnSuccessListener {
                val cure = it.child("cure").value.toString()
                val symptoms = it.child("symptoms").value.toString()

                binding.tvResultInfo.text = "Symptoms : $symptoms \n Cure : $cure\n "
        }

        // Releases model resources if no longer used.
        model.close()

    }

}








//firebase ML part

//    private fun processImage(){
//        val imageProcess = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
//        val imageInput = InputImage.fromBitmap(imageMap,0)
//        var imageUr : InputImage? = null
//        try {
//            imageUr = InputImage.fromFilePath(this,imageUri)
//        }catch (e : Exception){
//            e.printStackTrace()
//        }
//
//
//        imageProcess.process(imageInput).addOnSuccessListener {imageLabels ->
//
//            var results = ""
//            for (imageLabel in imageLabels){
//
//                val stringLabel = imageLabel.text
//                val floatConfidence = imageLabel.confidence
//                val index = imageLabel.index
//
//                results += "Index: $index\n Confidence: $floatConfidence\n Name:$stringLabel\n"
//                binding.tvResult.text = results
//
//            }
//
//        }
////
////        imageProcess.process(imageUr!!).addOnSuccessListener {imageLabels ->
////
////            var results = ""
////            for (imageLabel in imageLabels){
////
////                val stringLabel = imageLabel.text
////                val floatConfidence = imageLabel.confidence
////                val index = imageLabel.index
////
////                results += "$index\n $floatConfidence \n $stringLabel"
////                binding.tvResult.text = results
////
////            }
////
////        }
//
//
//    }






/// Contract launcher

//        val contract = registerForActivityResult(
//            ActivityResultContracts
//            .GetContent()){
//            if (it != null) {
//                imageUri = it
//            }
//            binding.imgView.setImageURI(it)
//        }
//
//        val contractbitmap = registerForActivityResult(ActivityResultContracts.GetContent()){
//
//        }
//
//        binding.selectImage.setOnClickListener {
//            contract.launch("image/*")
//
//        }
