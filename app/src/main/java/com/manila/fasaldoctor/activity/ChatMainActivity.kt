
package com.manila.fasaldoctor.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.manila.fasaldoctor.R
import com.manila.fasaldoctor.databinding.ActivityChatMainBinding

class ChatMainActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatMainBinding
    lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_chat_main)

//        setActionBar(toolbar)
        supportActionBar?.title = "Chat With Experts"
    }
}