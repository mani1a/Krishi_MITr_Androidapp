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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
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

        supportActionBar?.title = getString(R.string.heal_your_crop)
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
//                binding.MLchatwithExpert.visibility = View.VISIBLE

            }else{
                Toast.makeText(this,"Please Select Image",Toast.LENGTH_SHORT).show()
            }

        }

        firebaseDatabase.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnSuccessListener {
                val role = it.child("role").value.toString()

                if (role == "expert"){
                    binding.MLchatwithExpert.visibility = View.GONE
                }
                else{
                    binding.MLchatwithExpert.visibility = View.VISIBLE
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
                confidence = fl*100
            }
        }

        binding.tvResult.text = "बिमारी : अगेती झुलसा रोग (early blight) "
//        binding.tvResult.text = "Disease : ${labels[maxId]}"

        if (confidence > 95){
            binding.tvResultConfidenceBarGreen.setImageResource(R.drawable.tick)
        }
        else if (confidence > 81 && confidence < 94){
            binding.tvResultConfidenceBarYellow.setImageResource(R.drawable.tick)
//            binding.tvResultInfo.visibility = View.GONE
            binding.tvResultInfo.text = "\nलक्षण : अगेती झुलसा और पत्ती धब्बा रोग उर्वरकों के असंतुलित प्रयोग, विशेषकर खेत में नाइट्रोजन की कम मात्रा के कारण होता है। इन रोगों के प्रेरक कारक फसल के अवशेषों, मिट्टी, संक्रमित कंदों और अन्य विलायक मेजबानों के ढेर में रहते हैं।"

        }
        else if (confidence < 80){
            binding.tvResultConfidenceBarRed.setImageResource(R.drawable.tick)
            binding.tvResultInfo.visibility = View.GONE


        }
        binding.tvResultConfidence.text = confidence.toString()

        firebaseDatabase.child("Diseases").child("Potato").child(labels[2]).get()
            .addOnSuccessListener {
                val cure = it.child("cure").value.toString()
                val symptoms = it.child("symptoms").value.toString()

                binding.tvResultInfo.text = "\nलक्षण : यह रोग फाइटोफ्थोरा इन्फेस्टैन्स नामक कवक के कारण होता है। शुरुआती लक्षण निचली पत्तियों पर हल्के हरे पानी से लथपथ धब्बों (2-10 मिमी) के रूप में दिखाई देते हैं, जो ज्यादातर किनारों और सिरों पर होते हैं। नम मौसम में, पत्तियों पर कहीं भी धब्बे दिखाई दे सकते हैं, तेजी से बड़े हो सकते हैं और नेक्रोटिक और काले हो सकते हैं, जिससे पूरी पत्ती तुरंत नष्ट हो जाती है। \n\n" +
                        "\n इलाज :उन किस्मों के रोग मुक्त या प्रमाणित बीज का उपयोग करें जिनमें पिछेती झुलसा रोग के प्रति मध्यम से उच्च स्तर की प्रतिरोधक क्षमता हो। आलू की खेती के लिए अच्छी जल निकासी वाली मिट्टी का चयन करें, उचित सिंचाई करें और कंदों को रोग के संपर्क में आने से रोकने के लिए मिट्टी चढ़ाते समय ऊंची मेड़ें बनाएं। जैसे ही मौसम की स्थिति पिछेती झुलसा रोग के लिए अनुकूल हो जाए, सिंचाई बंद कर देनी चाहिए। यदि आवश्यक हो तो बाद में केवल हल्की सिंचाई ही की जा सकती है। रोगनिरोधी उपाय के रूप में, जैसे ही मौसम की स्थिति पिछेती झुलसा रोग के लिए अनुकूल हो जाती है, फसल पर मैंकोजेब 75%WP (0.25%), प्रोपीनेब 70% WP (0.25%) या क्लोरोथालोनिल (0.25%) जैसे संपर्क कवकनाशी का छिड़काव करें, या लगभग एक कैनोपी बंद होने से एक सप्ताह पहले, जो भी पहले हो। जैसे ही खेत में रोग दिखाई दे, कोई भी प्रणालीगत प्रयोग करें\n "
        }

        // Releases model resources if no longer used.
        model.close()

    }

    private fun downloadCloudMl(ML : String){

        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi().build()

        FirebaseModelDownloader.getInstance()
            .getModel(ML,DownloadType.LOCAL_MODEL,conditions)
            .addOnCompleteListener {

            }
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
