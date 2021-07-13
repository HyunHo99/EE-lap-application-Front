package com.example.myapplication.activity

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.PostAdapter
import com.example.myapplication.data.Posts
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class SearchPostActivity : AppCompatActivity() {
    private val url = "http://192.249.18.134:80/post/search/"
    val TAG: String = "로그"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchpost)
        val searchInput = findViewById<TextView>(R.id.input_search)
        val searchButton = findViewById<View>(R.id.bt_search)

        searchButton.setOnClickListener {
            val inputText = searchInput.text.toString()
            if((inputText.split(" ").size==1) && inputText.length <=10 && inputText.length>=2){
                searchThisAndSetRecycler(inputText)
                }
            else{
                Toast.makeText(this, "공백없이 2글자 이상, 10글자 이하로 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun searchThisAndSetRecycler(inputText: String) {
        val URL = "$url$inputText/"
        val client = OkHttpClient()
        val postsList = ArrayList<Posts>()
        val request = Request.Builder().url(URL).build()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_searched)
        val noImg = findViewById<ImageView>(R.id.img_noimg)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "searchFail")
            }

            override fun onResponse(call: Call, response: Response) {
                val test = response.body?.string() ?: ""
                val jsonArray = JSONObject(test).optJSONArray("data") ?: JSONArray()
                for (i in 0 until jsonArray.length()) {
                    val k = jsonArray.getJSONObject(i)
                    postsList.add(
                        Posts(
                            subject = k.get("subject").toString(),
                            time = k.get("create_date").toString(),
                            postID = k.get("id").toString().toInt(),
                            content = k.get("content").toString()
                        )
                    )
                }
                if(postsList.size==0){
                    runOnUiThread {
                        noImg.visibility = View.VISIBLE
                    }
                }
                else{
                    runOnUiThread {
                        noImg.visibility = View.GONE
                    }
                }
                val pAdapter = PostAdapter(applicationContext, postsList)
                pAdapter.itemClick = object : PostAdapter.ItemClick {
                    override fun onClick(view: View, position: Int) {
                        val intent =
                            Intent(applicationContext, ShowPostActivity::class.java).apply {
                                putExtra("postID", postsList[position].postID)
                            }
                        startActivity(intent)
                    }
                }
                runOnUiThread {
                    recyclerView?.adapter = pAdapter
                    val lm = LinearLayoutManager(applicationContext)
                    recyclerView.layoutManager = lm
                    recyclerView?.setHasFixedSize(true)
                    Log.d(TAG, "getSuccess")
                }
            }
        })

    }
}
