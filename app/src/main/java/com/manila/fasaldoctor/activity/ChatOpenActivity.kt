package com.manila.fasaldoctor.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.adapter.MessagesAdapter
import com.manila.fasaldoctor.databinding.ActivityChatOpenBinding
import com.manila.fasaldoctor.model.Messages
import com.manila.fasaldoctor.model.User
import com.manila.fasaldoctor.notification.NotificationData
import com.manila.fasaldoctor.notification.PushNotification
import com.manila.fasaldoctor.notification.api.ApiUtilis
import com.manila.fasaldoctor.utils.Layers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

class ChatOpenActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatOpenBinding
    private lateinit var msgRecyclerView: RecyclerView
    private lateinit var msgRecyclerAdapter: MessagesAdapter
    private lateinit var messagesList : ArrayList<Messages>
    lateinit var sendButton : ImageButton
    lateinit var messageBox : EditText
    lateinit var message : String
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    var name : String? = null
    var receiverRoom : String? = null
    var senderRoom : String? = null
    var receiveruid : String? = null
    var senderuid : String? = null
    var imageUri: Uri? = null
    var time : String? = null
    var audioFilePath : String? = null
    var audioUri : Uri? = null

    var mediaPlayer : MediaPlayer? = null
    var mediaRecorder: MediaRecorder? = null
    var state : Boolean = false
    var playState : Boolean = false
    var recordingStopped : Boolean = false



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatOpenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        name = intent.getStringExtra("name")
        receiveruid = intent.getStringExtra("uid")
        senderuid = FirebaseAuth.getInstance().currentUser?.uid

        supportActionBar?.title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        databaseReference = FirebaseDatabase.getInstance().getReference()
        storageReference = FirebaseStorage.getInstance().reference

        senderRoom = receiveruid + senderuid
        receiverRoom = senderuid + receiveruid

//        binding.inboxTxtName.text = name
//        binding.inboxTxtUid.text = receiveruid

        msgRecyclerView = findViewById(R.id.recyclerView_inbox)
        sendButton = findViewById(R.id.btn_send)
        messageBox = findViewById(R.id.msg_box)

        messagesList = ArrayList()
        msgRecyclerAdapter = MessagesAdapter(this,messagesList)
        msgRecyclerView.layoutManager = LinearLayoutManager(this)
        msgRecyclerView.adapter = msgRecyclerAdapter

        var data : Intent?= null
        val imagelaunchContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if (result.resultCode == RESULT_OK && result.data != null){
                data = result.data!!
                imageUri = data!!.data

                if (imageUri != null){

                sendImg()

                }
            }
        }

        binding.btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagelaunchContract.launch(intent)
        }

        sendButton.setOnClickListener {
            sendmsg()
        }


        binding.btnRecordAudio.setOnClickListener {
            binding.sendtextmsg.visibility = View.GONE
            binding.sendaudiomsg.visibility = View.VISIBLE
        }

        binding.btnCancel.setOnClickListener {
            destroyAudio()
        }

        val permission = arrayOf(android.Manifest.permission.RECORD_AUDIO)

        binding.imgAudioStart.setOnClickListener{

            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,permission,101)
            }else{
            recordAudio()
            }

        }


        binding.imgAudioStop.setOnClickListener {
            stopRecordAudio()
        }

        binding.btnPlay.setOnClickListener {
            playAudio()
        }

        binding.btnPause.setOnClickListener {
            stopAudio()
        }

        binding.btnSendAudio.setOnClickListener {
            sendAudio()
        }



        // code for showing chats
        binding.progsbar.visibility = View.VISIBLE
        binding.recyclerViewInbox.visibility = View.GONE
        databaseReference.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messagesList.clear()

                    for (postmsgsnapshot in snapshot.children) {

                        val messege = postmsgsnapshot.getValue(Messages::class.java)

                        messagesList.add(messege!!)
                        
                    }
                    binding.progsbar.visibility = View.GONE
                    binding.recyclerViewInbox.visibility = View.VISIBLE

                    msgRecyclerAdapter.notifyDataSetChanged()



                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        val date = LocalDateTime.now().toString()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

        time = date.format(formatter)

//        audioUri = audioFilePath?.toUri()


    }



    @SuppressLint("SuspiciousIndentation")
    private fun sendmsg(){

        message = messageBox.text.toString()
//        time = System.currentTimeMillis()
        val messageObject = Messages(time!!,message,senderuid)

            databaseReference.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    databaseReference.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject).addOnCompleteListener {
                            if (it.isSuccessful) {
                                sendNotification(message)
                            }
                        }
                }
//        msgRecyclerView.setScrollingTouchSlop(1)
            messageBox.setText("")

//        MessagesAdapter.SentMsgViewholder().imgsend

    }

    private fun sendImg() {

        Layers.showProgressBar(this)

        storageReference.child("Users_chat_Img").child(senderuid!!).child(time+"image.jpeg")
            .putFile(imageUri!!).addOnSuccessListener {
            task ->
            task.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                val img = it.toString()
                val messageObject = Messages(time!!,"photo",senderuid,img,"")
//                messageObject.msg = "photo"
                databaseReference.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        databaseReference.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Layers.hideProgressBar()
//                                    sendNotification(message)
                                }
                            }
                    }
            }
        }
    }

    private fun sendAudio(){

        audioUri = Uri.fromFile(File(audioFilePath!!))

        Layers.showProgressBar(this)
        storageReference.child("Users_Chat_Audio").child(senderuid!!).child(time+"audio.3gp")
            .putFile(audioUri!!).addOnSuccessListener {
            task->
            task.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                val audio = it.toString()
                val messageObject = Messages(time!!,"audio",senderuid,"",audio)
                databaseReference.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        databaseReference.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject).addOnCompleteListener {
                                if (it.isSuccessful){
                                    Layers.hideProgressBar()
                                }
                            }
                    }
            }
        }
    }

    private fun sendNotification(msg: Any) {

        databaseReference.child("Users").child(receiveruid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        val data = snapshot.getValue(User::class.java)

                        val notificationData = PushNotification(NotificationData(name,"MESSAGES YOU")
                        ,data!!.fcmtoken
                            )

                        ApiUtilis.getInstance().sendNotification(notificationData).enqueue(object : Callback<PushNotification>{
                            override fun onResponse(
                                call: Call<PushNotification>,
                                response: Response<PushNotification>
                            ) {

                                Toast.makeText(applicationContext,"Notification Sent",Toast.LENGTH_SHORT).show()

                            }

                            override fun onFailure(call: Call<PushNotification>, t: Throwable) {

                                Toast.makeText(applicationContext,"Something Went Wrong",Toast.LENGTH_SHORT).show()
                            }

                        })


                    }


                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this,ChatMainActivity::class.java))
        return super.onOptionsItemSelected(item)
    }

    private fun recordAudio() {
        binding.txtviewRecording.text = "Click Icon To Stop"
        mediaRecorder = MediaRecorder()
        audioFilePath = "${externalCacheDir?.absolutePath}/audiorecordtest.m4a"
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setOutputFile(audioFilePath)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

     try {
         mediaRecorder?.prepare()
         state = true
     }catch (e : Exception){
         e.printStackTrace()
     }

        mediaRecorder?.start()
        Toast.makeText(this,"Recording Started",Toast.LENGTH_SHORT).show()

        binding.imgAudioStart.visibility = View.GONE
        binding.imgAudioStop.visibility = View.VISIBLE


    }

    private fun stopRecordAudio(){
        binding.btnPlay.visibility = View.VISIBLE
        if (state) {

            mediaRecorder?.stop()
            mediaRecorder?.release()
            recordingStopped = true
            Toast.makeText(this, "Recording Stopped", Toast.LENGTH_SHORT).show()
            binding.btnPause.visibility = View.GONE
            binding.btnSendAudio.visibility = View.VISIBLE

        }

        binding.txtviewRecording.text = "Click To Play"
        state = false

    }

    private fun playAudio(){
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(audioFilePath)
        mediaPlayer?.prepare()
        mediaPlayer?.start()

        if (mediaPlayer!!.isPlaying){
            binding.btnPlay.visibility = View.GONE
            binding.btnPause.visibility = View.VISIBLE


        }
        else {
            binding.btnPlay.visibility = View.VISIBLE
            binding.btnPause.visibility = View.GONE

        }

        playState = true

        mediaPlayer?.setOnCompletionListener {

            binding.btnPause.visibility = View.GONE
            binding.btnPlay.visibility = View.VISIBLE


        }

    }

    private fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        binding.btnPause.visibility = View.GONE
        binding.btnPlay.visibility = View.VISIBLE

        playState = false

    }

    private fun destroyAudio(){

        if (playState){
            mediaPlayer?.stop()
            mediaPlayer?.reset()
        }

        if (state){

        mediaRecorder?.stop()
        mediaRecorder?.reset()
        }

        binding.btnSendAudio.visibility = View.GONE
        binding.btnPlay.visibility = View.GONE
        binding.imgAudioStop.visibility = View.GONE
        binding.imgAudioStart.visibility = View.VISIBLE
        binding.txtviewRecording.text = "Click To Record"
        binding.sendtextmsg.visibility = View.VISIBLE
        binding.sendaudiomsg.visibility = View.GONE



    }




}
