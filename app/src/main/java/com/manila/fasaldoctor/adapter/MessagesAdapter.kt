package com.manila.fasaldoctor.adapter

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.opengl.Visibility
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.model.Messages

class MessagesAdapter(val context: Context, val messagesList: ArrayList<Messages>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val Item_receive = 1;
    val Item_send = 2;
    var mediaPlayer : MediaPlayer? = null
    var audioPath : String? = null
    lateinit var currentMsg : Messages
    var state : Boolean = false



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1){
            // it will inflate and bind with receiver msg

            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_receive_msg,parent,false)
//            val view2 = LayoutInflater.from(parent.context).inflate(R.layout.layout_receive_img,parent,false)

//            return ReceiveMsgViewholder(view)
            return ReceiveMsgViewholder(view)

        }

        else {
            // it will inflate and bind with sender msg

            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_send_msg,parent,false)
//            val view2 = LayoutInflater.from(parent.context).inflate(R.layout.layout_send_img,parent,false)

            return SentMsgViewholder(view)

        }


    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

         currentMsg = messagesList[position]



        if(holder.javaClass == SentMsgViewholder::class.java){
            //for sending msgs

            val viewholder = holder as SentMsgViewholder
            holder.setTime.text = currentMsg.time.toString()

            holder.sendmsg.text = currentMsg.msg


            if (currentMsg.msg.equals("photo")){

                holder.sendmsg.visibility = View.GONE
                holder.imgsend.visibility = View.VISIBLE
                holder.txtLayout.visibility = View.GONE

                Glide.with(context).load(currentMsg.image).into(holder.imgsend)

            }


            if (currentMsg.msg.equals("audio")){

                var audioPath = currentMsg.audio

                holder.sendmsg.visibility = View.GONE
                holder.imgsend.visibility = View.GONE
                holder.txtLayout.visibility = View.GONE
                holder.audioLayout.visibility = View.VISIBLE
                holder.btnPlay.visibility = View.VISIBLE

                holder.btnPlay.setOnClickListener {

                    mediaPlayer = MediaPlayer()
                    mediaPlayer?.setDataSource(audioPath)
                    mediaPlayer?.prepare()
                    mediaPlayer?.start()
                    mediaPlayer?.setOnCompletionListener {
                        holder.btnPlay.visibility = View.VISIBLE
                        holder.btnPause.visibility = View.GONE
                    }

//                    Toast.makeText(context,"play",Toast.LENGTH_SHORT).show()

                    holder.btnPause.visibility = View.VISIBLE

                }

                holder.btnPause.setOnClickListener {
//                    Toast.makeText(context,"pause",Toast.LENGTH_SHORT).show()
                    holder.btnPlay.visibility = View.VISIBLE
                    holder.btnPause.visibility = View.GONE
                    mediaPlayer?.stop()
                    mediaPlayer?.release()

                }


            }
        }

        else {

            val viewholder = holder as ReceiveMsgViewholder
            holder.setTime.text = currentMsg.time.toString()

            holder.receivemsg.text = currentMsg.msg


            if (currentMsg.msg.equals("photo")){
                holder.imgreceive.visibility = View.VISIBLE
                holder.receivemsg.visibility = View.GONE
                holder.txtLayout.visibility = View.GONE
                Glide.with(context).load(currentMsg.image).into(holder.imgreceive)
            }


            if (currentMsg.msg.equals("audio")){

                var audioPath = currentMsg.audio

                holder.receivemsg.visibility = View.GONE
                holder.imgreceive.visibility = View.GONE
                holder.txtLayout.visibility = View.GONE

                holder.audioLayout.visibility = View.VISIBLE
                holder.btnPlay.visibility = View.VISIBLE

                holder.btnPlay.setOnClickListener {


                    mediaPlayer = MediaPlayer()
                    mediaPlayer?.setDataSource(audioPath)
                    mediaPlayer?.prepare()
                    mediaPlayer?.start()
                    mediaPlayer?.setOnCompletionListener {
                        holder.btnPlay.visibility = View.VISIBLE
                        holder.btnPause.visibility = View.GONE
                    }

//                    Toast.makeText(context,"play",Toast.LENGTH_SHORT).show()
                    holder.btnPause.visibility = View.VISIBLE

                }

                holder.btnPause.setOnClickListener {
//                    Toast.makeText(context,"pause",Toast.LENGTH_SHORT).show()

                    holder.btnPlay.visibility = View.VISIBLE
                    holder.btnPause.visibility = View.GONE

                    mediaPlayer?.stop()
                    mediaPlayer?.release()

                }

            }

        }

    }

    override fun getItemViewType(position: Int): Int {

        val currentMsg = messagesList[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMsg.senderID)){

            return Item_send

        }
        else{

            return Item_receive

        }

        return super.getItemViewType(position)

    }


    override fun getItemCount(): Int {

        return messagesList.size

    }

    class SentMsgViewholder(itemView : View ): RecyclerView.ViewHolder(itemView){

        val txtLayout : RelativeLayout = itemView.findViewById(R.id.txtLayout)

        val sendmsg : TextView = itemView.findViewById(R.id.txtsendmsg)

        val imgsend : ImageView = itemView.findViewById(R.id.imgsendmsg)

        val setTime : TextView = itemView.findViewById(R.id.setTime)

        val audioLayout : LinearLayout = itemView.findViewById(R.id.sendaudiolayout)

        val btnPlay : ImageButton = itemView.findViewById(R.id.play)

        val btnPause : ImageButton = itemView.findViewById(R.id.pause)

        val txt : TextView = itemView.findViewById(R.id.audiomsg)


    }

    class ReceiveMsgViewholder(itemView : View ): RecyclerView.ViewHolder(itemView){

        val txtLayout : RelativeLayout = itemView.findViewById(R.id.txtLayout)

        val receivemsg : TextView = itemView.findViewById(R.id.txtreceivemsg)

        val imgreceive : ImageView = itemView.findViewById(R.id.imgreceivemsg)

        val setTime : TextView = itemView.findViewById(R.id.setTime)

        val audioLayout : LinearLayout = itemView.findViewById(R.id.receiveaudiolayout)

        val btnPlay : ImageButton = itemView.findViewById(R.id.play)

        val btnPause : ImageButton = itemView.findViewById(R.id.pause)

        val txt : TextView = itemView.findViewById(R.id.audiomsg)

    }


}