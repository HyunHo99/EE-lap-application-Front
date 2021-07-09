package com.example.myapplication.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class AddPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addpost)
        val submitBt : View=findViewById(R.id.bt_submit)
        val contentInput : View = findViewById(R.id.input_content)
        val subjectInput : View = findViewById(R.id.input_subject)

        submitBt.setOnClickListener{ view ->

        }


    }
}