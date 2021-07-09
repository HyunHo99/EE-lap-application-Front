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
import com.example.myapplication.adapter.PostAdapter
import com.example.myapplication.data.Posts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class FragPost : Fragment() {
    val TAG: String = "로그"
    @SuppressLint("SetJavaScriptEnabled")

    private val url = "http://192.249.18.134:80/post"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_post, container, false)
        val myDataset = getFromDB(v)
        val addPostBt : View = v.findViewById(R.id.bt_addPost)

        addPostBt.setOnClickListener{ view ->
            val intent = Intent(activity, AddPostActivity::class.java)
            startActivity(intent)
        }

        return v
    }

    private fun getFromDB(view:View){
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val postsList = ArrayList<Posts>()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_posts)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "postFail")
            }
            override fun onResponse(call: Call, response: Response) {
                val test = response.body?.string()
                val jsonObject = JSONObject(test)
                val p=jsonObject.length()
                for(i in 1..p){
                    val k = jsonObject.getJSONObject(i.toString())
                    postsList.add(Posts(subject = k.get("subject").toString(), time = k.get("create_date").toString()))
                }
                val pAdapter = PostAdapter(requireContext(), postsList)
                requireActivity().runOnUiThread{
                    recyclerView?.adapter=pAdapter
                    val lm = LinearLayoutManager(requireContext())
                    recyclerView.layoutManager = lm
                    recyclerView?.setHasFixedSize(true)
                    Log.d(TAG, "postSuccess")
                }
            }
        })
    }


}