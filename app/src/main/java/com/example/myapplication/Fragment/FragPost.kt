package com.example.myapplication.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.AddPostActivity
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

class FragPost : Fragment() {
    val TAG: String = "로그"
    @SuppressLint("SetJavaScriptEnabled")

    private val url = "http://192.249.18.134:80/post"
    private lateinit var v : View;
    private val postsList = ArrayList<Posts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_post, container, false)
        val addPostBt : View = v.findViewById(R.id.bt_addPost)
        getFromDB(v)


        addPostBt.setOnClickListener{ view ->
            val intent = Intent(activity, AddPostActivity::class.java)
            startActivityForResult(intent, 100)

        }

        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==100){
            getFromDB(v)
        }
    }

    private fun getFromDB(view:View){
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_posts)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "getFail")
            }
            override fun onResponse(call: Call, response: Response) {
                val test = response.body?.string() ?:""
                val jsonArray = JSONObject(test).optJSONArray("data") ?: JSONArray()
                for(i in 0 until jsonArray.length()){
                    val k = jsonArray.getJSONObject(i)
                    postsList.add(Posts(subject = k.get("subject").toString(), time = k.get("create_date").toString(),
                        postID = k.get("id").toString().toInt()))
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
                    recyclerView?.adapter=pAdapter
                    val lm = LinearLayoutManager(requireContext())
                    recyclerView.layoutManager = lm
                    recyclerView?.setHasFixedSize(true)
                    Log.d(TAG, "getSuccess")
                }
            }
        })
    }


}