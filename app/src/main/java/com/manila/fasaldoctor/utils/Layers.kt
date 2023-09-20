package com.manila.fasaldoctor.utils

import android.app.ProgressDialog
import android.content.Context
import com.manila.fasaldoctor.R

object Layers {

    lateinit var progressBar : ProgressDialog

    fun showProgressBar(context: Context , text : String){
        progressBar = ProgressDialog(context)
        progressBar.setCancelable(false)
        progressBar.setContentView(R.layout.progressbar)
        progressBar.setTitle(text)
        progressBar.show()
    }

    fun hideProgressBar(){
        progressBar.dismiss()
    }
}