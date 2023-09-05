package com.manila.fasaldoctor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.model.Messages

class MessagesAdapter(val context: Context, val messagesList: ArrayList<Messages>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val Item_receive = 1;
    val Item_send = 2;



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1){
            // it will inflate and bind with receiver msg

            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_receive_msg,parent,false)

            return ReceiveMsgViewholder(view)

        }
        else{
            // it will inflate and bind with sender msg

            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_send_msg,parent,false)

            return SentMsgViewholder(view)


        }




    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val currentMsg = messagesList[position]
        if(holder.javaClass == SentMsgViewholder::class.java){
            //for sending msgs

            val viewholder = holder as SentMsgViewholder
            holder.sendmsg.text = currentMsg.msg


        }
        else{
            // for receiving msgs

            val viewholder = holder as ReceiveMsgViewholder
            holder.receivemsg.text = currentMsg.msg

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

        val sendmsg : TextView = itemView.findViewById(R.id.txtsendmsg)

    }

    class ReceiveMsgViewholder(itemView : View ): RecyclerView.ViewHolder(itemView){
        val receivemsg : TextView = itemView.findViewById(R.id.txtreceivemsg)

    }

}