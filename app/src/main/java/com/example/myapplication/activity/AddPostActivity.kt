package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.content.Intent
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
    lateinit var labIDInput :EditText
    val CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addpost)
        val submitBt : View=findViewById(R.id.bt_submit)
        val contentInput : EditText = findViewById(R.id.input_content)
        val subjectInput : EditText = findViewById(R.id.input_subject)
        labIDInput  = findViewById(R.id.input_labid)
        val searchBt : View = findViewById(R.id.bt_toSearchLab)
        val backBt  = findViewById<View>(R.id.search_btn)

        submitBt.setOnClickListener{ view ->
            val content = contentInput.text.toString()
            val subject = subjectInput.text.toString()
            val labID = labIDInput.text.toString()
            if(content!=""&&subject!="") {
                val formBody: RequestBody =
                    FormBody.Builder().add("subject", subject).add("content", content).add("user",globalVar).add("labcode",labID).build()
                postThis(formBody)
                finish()
            }
        }
        searchBt.setOnClickListener {
            val intent = Intent(applicationContext, LabSearchActivity::class.java)
            startActivityForResult(intent, CODE)
        }
        backBt.setOnClickListener {
            finish()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == resultCode && requestCode==CODE){
            runOnUiThread {
                labIDInput.setText(data?.getStringExtra("code").toString())
            }
        }
    }
}