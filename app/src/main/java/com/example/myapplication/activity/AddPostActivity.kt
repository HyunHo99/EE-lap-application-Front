package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.activity.MyGlobal.Companion.globalVar
import okhttp3.*
import java.io.IOException

class AddPostActivity : AppCompatActivity() {

    val TAG: String = "로그"
    @SuppressLint("SetJavaScriptEnabled")
    private val url = "http://192.249.18.134:80/post/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addpost)
        val submitBt : View=findViewById(R.id.bt_submit)
        val contentInput : EditText = findViewById(R.id.input_content)
        val subjectInput : EditText = findViewById(R.id.input_subject)

        submitBt.setOnClickListener{ view ->
            val content = contentInput.text.toString()
            val subject = subjectInput.text.toString()
            if(content!=""&&subject!="") {
                val formBody: RequestBody =
                    FormBody.Builder().add("subject", subject).add("content", content).add("user",globalVar).build()
                postThis(formBody)
                finish()
            }
        }


    }

    private fun postThis(formBody: RequestBody) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).post(formBody).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "postFail")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "postSuccess")
            }
        })
    }
}