package com.example.myapplication.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import android.widget.TextView
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.AddPostActivity

import com.example.myapplication.activity.MyGlobal.Companion.globalVar
import com.example.myapplication.activity.SearchPostActivity
import com.example.myapplication.activity.ShowPostActivity
import com.example.myapplication.adapter.PostAdapter
import com.example.myapplication.data.Posts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.math.max

class FragPost : Fragment() {
    val TAG: String = "로그"
    @SuppressLint("SetJavaScriptEnabled")

    private val url = "http://192.249.18.134:80/post/main"
    private lateinit var v : View
    private lateinit var totalPageText : TextView
    private val adminUserID = listOf<String>("105137147577786092785", "117433474764757616762")

    private var maxPageNum = 1
    private var pageNum = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_post, container, false)

        val addPostBt : View = v.findViewById(R.id.bt_addPost)
        //getFromDB(v)
        val toSearchBt : View = v.findViewById(R.id.bt_toSearch)
        val toFrontBt : View = v.findViewById(R.id.bt_toFrontPage)
        val toBackBt : View = v.findViewById(R.id.bt_toBackPage)
        val inputPage : EditText = v.findViewById(R.id.input_page)
        val toInputBt : View = v.findViewById(R.id.bt_toInputPage)
        totalPageText = v.findViewById(R.id.txt_totalpage)
        inputPage.setText(pageNum.toString())


        toFrontBt.setOnClickListener {
            if(pageNum>1){
                pageNum-=1
                inputPage.setText(pageNum.toString())
                getFromDB(v)
            }
        }
        toBackBt.setOnClickListener {
            if(pageNum+1<=maxPageNum){
                pageNum+=1
                inputPage.setText(pageNum.toString())
                getFromDB(v)
            }
        }
        toInputBt.setOnClickListener {
            val inputed = inputPage.text.toString().toInt()
            if(inputed in 1..maxPageNum){
                pageNum = inputed
                getFromDB(v)
            }
        }
        toSearchBt.setOnClickListener {
            val searchPostActivity = Intent(activity, SearchPostActivity::class.java)
            startActivity(searchPostActivity)
        }


        addPostBt.setOnClickListener{ view ->
            if(!adminUserID.contains(globalVar)){
                Toast.makeText(activity, "게시물 등록은 관리자만 가능합니다", Toast.LENGTH_SHORT).show()
            }
            else {
                val intent = Intent(activity, AddPostActivity::class.java)
                startActivity(intent)
            }

        }

        return v
    }

    private fun getFromDB(view:View){
        val client = OkHttpClient()
        val postsList = ArrayList<Posts>()
        val request = Request.Builder().url("$url/$pageNum/").build()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_posts)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "getFail")
            }
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call, response: Response) {
                val test = response.body?.string() ?:""
                val jsonArray = JSONObject(test).optJSONArray("data") ?: JSONArray()
                for(i in 0 until jsonArray.length()){
                    val k = jsonArray.getJSONObject(i)
                    postsList.add(Posts(subject = k.get("subject").toString(), time = k.get("create_date").toString(),
                        postID = k.get("id").toString().toInt(), content = k.get("content").toString()))
                }
                val totalPostNum = JSONObject(test).get("totalNum").toString().toInt()
                if(totalPostNum%10!=0){
                    maxPageNum = totalPostNum/10+1
                }
                else{
                    maxPageNum = totalPostNum/10
                }
                val pAdapter = PostAdapter(requireContext(), postsList)
                pAdapter.itemClick = object : PostAdapter.ItemClick{
                    override fun onClick(view: View, position: Int) {
                        val intent = Intent(context, ShowPostActivity::class.java).apply {
                            putExtra("postID", postsList[position].postID)
                        }
                        context!!.startActivity(intent)
                    }
                }
                requireActivity().runOnUiThread{
                    totalPageText.text = "/${maxPageNum}"
                    recyclerView?.adapter=pAdapter
                    val lm = LinearLayoutManager(requireContext())
                    recyclerView.layoutManager = lm
                    recyclerView?.setHasFixedSize(true)
                    Log.d(TAG, "getSuccess")
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        getFromDB(v)
    }
}