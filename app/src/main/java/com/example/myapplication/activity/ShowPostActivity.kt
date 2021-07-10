package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ContentView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.CommentAdapter
import com.example.myapplication.adapter.PostAdapter
import com.example.myapplication.data.Comments
import com.example.myapplication.data.Posts
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ShowPostActivity : AppCompatActivity(){
    val TAG: String = "로그"
    private val commentsList = ArrayList<Comments>()
    private lateinit var recyclerView : RecyclerView
    private lateinit var postTitle : TextView
    private lateinit var postContent :TextView





    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val v=LayoutInflater.from(this).inflate(R.layout.activity_showpost,null)
        setContentView(v)
        val postID = intent.getIntExtra("postID", -1)
        val url="http://192.249.18.134:80/post/$postID/"
        val submitBt=findViewById<View>(R.id.comment_submit)
        val commentInput = findViewById<EditText>(R.id.comment_Input)
        postTitle = findViewById<TextView>(R.id.showpost_title)
        postContent = findViewById<TextView>(R.id.showpost_content)
        recyclerView = v.findViewById<RecyclerView>(R.id.showpost_recycler)

        getFromDB(v,url)

        submitBt.setOnClickListener { view->
            val content = commentInput.text.toString()
            if(content!="") {
                val formBody: RequestBody = FormBody.Builder().add("content", content).build()
                postThis(formBody, url)
            }
        }


    }

    private fun postThis(formBody: RequestBody, url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).post(formBody).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "postFail")
            }

            override fun onResponse(call: Call, response: Response) {
                val test = response.body?.string() ?: return
                val k=JSONObject(test)
                runOnUiThread{
                    commentsList.add(Comments(comments = k.get("content").toString(),time = k.get("create_date").toString()))
                    val newAdapter = CommentAdapter(applicationContext, commentsList)
                    recyclerView.adapter=newAdapter
                }
                Log.d(TAG, "postSuccess")
            }
        })
    }


    private fun getFromDB(view:View, url:String){
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "getFail")
            }
            override fun onResponse(call: Call, response: Response) {
                val test = response.body?.string() ?: return
                val jsonArray = JSONObject(test).optJSONArray("data") ?: JSONArray()
                val post = JSONObject(test).getJSONObject("post")
                for(i in 0 until jsonArray.length()){
                    val k = jsonArray.getJSONObject(i)
                    commentsList.add(Comments(comments = k.get("content").toString(), time = k.get("create_date").toString()))
                }
                val cAdapter = CommentAdapter(applicationContext, commentsList)
                runOnUiThread{
                    recyclerView.adapter=cAdapter
                    val lm = LinearLayoutManager(applicationContext)
                    recyclerView.layoutManager = lm
                    recyclerView.setHasFixedSize(true)
                    postContent.text=post.get("content").toString()
                    postTitle.text=post.get("subject").toString()
                    Log.d(TAG, "getSuccess")
                }
            }
        })
    }
}