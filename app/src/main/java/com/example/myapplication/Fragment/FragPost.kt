package com.example.myapplication.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.activity.AddPostActivity
import okhttp3.*
import java.io.IOException

class FragPost : Fragment() {
    val TAG: String = "로그"
    @SuppressLint("SetJavaScriptEnabled")

    private val url = "http://192.249.18.134:80/post"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_post, container, false)
        val addPostBt : View = v.findViewById(R.id.bt_addPost)
        addPostBt.setOnClickListener{ view ->
            val intent = Intent(activity, AddPostActivity::class.java)
            startActivity(intent)
        }

        //val formBody: RequestBody = FormBody.Builder().add("subject", "test").add("content", "test").build()
        // postThis(formBody)


        return v
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