package com.manila.fasaldoctor.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.adapter.MessagesAdapter
import com.manila.fasaldoctor.databinding.ActivityChatOpenBinding
import com.manila.fasaldoctor.model.Messages
import com.manila.fasaldoctor.model.User
import com.manila.fasaldoctor.notification.NotificationData
import com.manila.fasaldoctor.notification.PushNotification
import com.manila.fasaldoctor.notification.api.ApiUtilis

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatOpenActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatOpenBinding

    private lateinit var msgRecyclerView: RecyclerView
    private lateinit var msgRecyclerAdapter: MessagesAdapter
    private lateinit var messagesList : ArrayList<Messages>
    lateinit var sendButton : ImageButton
    lateinit var messageBox : EditText

    lateinit var message : String

    private lateinit var databaseReference: DatabaseReference

    var receiverRoom : String? = null
    var senderRoom : String? = null
    var receiveruid : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatOpenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.inboxBtnBack.setOnClickListener {
            startActivity(Intent(this,ChatMainActivity::class.java))
        }



//        val intent = Intent()
        val name = intent.getStringExtra("name")
        receiveruid = intent.getStringExtra("uid")
        val senderuid = FirebaseAuth.getInstance().currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiveruid + senderuid
        receiverRoom = senderuid + receiveruid


        binding.inboxTxtName.text = name
        binding.inboxTxtUid.text = receiveruid


        msgRecyclerView = findViewById(R.id.recyclerView_inbox)
        sendButton = findViewById(R.id.btn_send)
        messageBox = findViewById(R.id.msg_box)

        messagesList = ArrayList()
        msgRecyclerAdapter = MessagesAdapter(this,messagesList)

        msgRecyclerView.layoutManager = LinearLayoutManager(this)
        msgRecyclerView.adapter = msgRecyclerAdapter

        sendButton.setOnClickListener {

            message = messageBox.text.toString()
            val messageObject = Messages(message,senderuid)

            databaseReference.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    databaseReference.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject).addOnCompleteListener {
                            if (it.isSuccessful){




                                sendNotification(message)

                            }
                        }
                }

            messageBox.setText("")




        }

        // code for showing chats

        databaseReference.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messagesList.clear()

                    for (postmsgsnapshot in snapshot.children){

                        val messege = postmsgsnapshot.getValue(Messages::class.java)

                        messagesList.add(messege!!)

                    }
                    msgRecyclerAdapter.notifyDataSetChanged()



                }

                override fun onCancelled(error: DatabaseError) {

                }

            })






    }

    private fun sendNotification(msg: Any) {

        databaseReference.child("Users").child(receiveruid!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        val data = snapshot.getValue(User::class.java)

                        val notificationData = PushNotification(NotificationData("New Message",message)
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
}