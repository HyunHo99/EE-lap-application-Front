package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.LabListLoader
import com.example.myapplication.R
import com.example.myapplication.activity.MyGlobal.Companion.globalVar
import com.example.myapplication.adapter.CommentAdapter
import com.example.myapplication.adapter.LabAdapter
import com.example.myapplication.data.Comments
import com.example.myapplication.data.Lab
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class ShowPostActivity : AppCompatActivity(){
    val TAG: String = "로그"
    private var commentsList = ArrayList<Comments>()
    private lateinit var recyclerView : RecyclerView
    private lateinit var postTitle : TextView
    private lateinit var postContent :TextView
    private lateinit var submitBt : View
    private lateinit var commentInput :EditText
    private lateinit var deleteBt :View
    private val client = OkHttpClient()
    private var userID = "0"
    private lateinit var labRecyclerView :RecyclerView
    private lateinit var rawLabList : ArrayList<Lab>
    private val adminUserID = listOf<String>("105137147577786092785", "117433474764757616762")








    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val v=LayoutInflater.from(this).inflate(R.layout.activity_showpost,null)
        setContentView(v)
        val postID = intent.getIntExtra("postID", -1)
        val url="http://192.249.18.134:80/post/$postID/"
        submitBt=findViewById<View>(R.id.comment_submit)
        commentInput = findViewById<EditText>(R.id.comment_Input)
        deleteBt=findViewById<View>(R.id.bt_postDelete)
        postTitle = findViewById<TextView>(R.id.showpost_title)
        postContent = findViewById<TextView>(R.id.showpost_content)
        recyclerView = v.findViewById<RecyclerView>(R.id.showpost_recycler)
        val backBt = v.findViewById<View>(R.id.bt_back)


        labRecyclerView = v.findViewById<RecyclerView>(R.id.recycler_showpostlabs)
        rawLabList = LabListLoader().loadLabList(assetManager = resources.assets)

        backBt.setOnClickListener {
            finish()
        }

        getFromDB(url)






    }


    private fun postThis(formBody: RequestBody, url: String) {

        val request = Request.Builder().url(url).post(formBody).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "postFail")
            }

            override fun onResponse(call: Call, response: Response) {
                val test = response.body?.string() ?: return
                val k=JSONObject(test)
                runOnUiThread{
                    commentsList.add(Comments(comments = k.get("content").toString(), time = k.get("create_date").toString(),
                        commentID = k.get("id").toString().toInt(), user = k.get("user").toString()))
                    val newAdapter = setDeleteFunAdapter()
                    recyclerView.adapter=newAdapter
                }
                Log.d(TAG, "postSuccess")
            }
        })
    }


    private fun getFromDB(url:String){
        val request = Request.Builder().url(url).build()
        commentsList.clear()
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
                    commentsList.add(Comments(comments = k.get("content").toString(), time = k.get("create_date").toString(),
                        commentID = k.get("id").toString().toInt(), user = k.get("user").toString()))
                }
                val cAdapter = setDeleteFunAdapter()
                val forOneLabList = ArrayList<Lab>()
                val labcode = post.get("labcode").toString()
                for(i in rawLabList){
                    if(i.Id == labcode){
                        forOneLabList.add(i)
                    }
                }
                val lAdapter = LabAdapter(applicationContext, forOneLabList)
                runOnUiThread{
                    if(forOneLabList.size!=1){
                        labRecyclerView.visibility=View.GONE
                    }
                    labRecyclerView.adapter=lAdapter
                    val lm = LinearLayoutManager(applicationContext)
                    labRecyclerView.layoutManager = lm
                    labRecyclerView.setHasFixedSize(true)
                    recyclerView.adapter=cAdapter
                    val lm2 = LinearLayoutManager(applicationContext)
                    recyclerView.layoutManager = lm2
                    recyclerView.setHasFixedSize(true)
                    postContent.text=post.get("content").toString()
                    postTitle.text=post.get("subject").toString()
                    userID = post.get("user").toString()
                    if(adminUserID.contains(globalVar)) {
                        deleteBt.visibility = View.VISIBLE
                        deleteBt.setOnClickListener { view ->
                            deleteThis(url)
                        }
                    }
                    submitBt.setOnClickListener { view->
                        if(!globalVar.equals("0")) {
                            val content = commentInput.text.toString()
                            if (content != "") {
                                val formBody: RequestBody = FormBody.Builder().add("content", content).add("user",
                                    globalVar).build()
                                commentInput.setText("")
                                postThis(formBody, url)
                            }
                        }
                        else{
                            Toast.makeText(applicationContext, "로그인 후 등록할 수 있습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    Log.d(TAG, "getSuccess")
                }
            }
        })
    }

    private fun deleteComment(commentID: Int) {
        val url = "http://192.249.18.134:80/post/delete/$commentID/"
        val request = Request.Builder().delete().url(url).build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "removeFail")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "removeSuccess")
            }
        })
    }

    private fun deleteThis(url: String){
        val request = Request.Builder().delete().url(url).build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "removeFail")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "removeSuccess")
                finish()
            }
        })
    }

    private fun setDeleteFunAdapter() : CommentAdapter {
        val newAdapter = CommentAdapter(applicationContext, commentsList)
        newAdapter.itemClick = object : CommentAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                if(commentsList[position].user == globalVar) {
                    deleteComment(commentsList[position].commentID)
                    commentsList.removeAt(position)
                    recyclerView.adapter = setDeleteFunAdapter()
                }
            }
        }
        return newAdapter
    }

}