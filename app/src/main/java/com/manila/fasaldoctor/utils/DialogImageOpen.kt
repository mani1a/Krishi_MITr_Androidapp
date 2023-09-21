package com.manila.fasaldoctor.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import android.view.Window
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.manila.fasaldoctor.R

object DialogImageOpen {

    fun showDialogBox(context: Context,image : String){
        val dialog : Dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_dialog_image_open)
        dialog.findViewById<ImageView>(R.id.dialog_IV)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val imageView : ImageView  = dialog.findViewById(R.id.dialog_IV)
        Glide.with(context).load(image).into(imageView)

        dialog.show()




    }
}