package com.example.myapplication.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.LabListLoader
import com.example.myapplication.R
import com.example.myapplication.activity.MyGlobal
import com.example.myapplication.activity.ShowPostActivity
import com.example.myapplication.adapter.PostAdapter
import com.example.myapplication.data.Comments
import com.example.myapplication.data.Keyword
import com.example.myapplication.data.Lab
import com.example.myapplication.data.Posts
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class FragSurf3 : Fragment() {

    val TAG: String = "로그"
    var clickedKeyword: String ?= null
    private val url = "http://192.249.18.134:80/lab/getPosts/"
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle: Bundle? = arguments
        clickedKeyword = bundle?.getString("clickedKeywordName")
        return inflater.inflate(R.layout.fragment_surf_tab3, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rawLabList = LabListLoader().loadLabList(assetManager = resources.assets)
        val newLabList = clickedKeyword?.let { filterListByKeyword(it, rawLabList) }
        val labcodes  = newLabList!!.map { it -> it.Id }?.joinToString(",")
        val formBody: RequestBody =
            FormBody.Builder().add("labcodes", labcodes).build()
        postThis(formBody, url, view)
    }



    private fun filterListByKeyword(keyword: String, rawLabList: ArrayList<Lab>): ArrayList<Lab> {
        val newLabList: ArrayList<Lab> = ArrayList()
        val listOfDiv:List<String> = listOf("컴퓨터","신호","회로","통신","소자","전파")
        if (clickedKeyword in listOfDiv) {

            val division: String = when(keyword){
                "컴퓨터" -> "CP"
                "통신" -> "CM"
                "신호" -> "SN"
                "회로" -> "CC"
                "소자" -> "DV"
                else -> "WV"
            }
            for (i in 0 until rawLabList.size) {
                if (rawLabList[i].Division == division){
                    newLabList.add(rawLabList[i])
                    Log.d(TAG, "division: $division. newLabList=$newLabList")
                }
            }
        }
        else {
            for (i in 0 until rawLabList.size) {
                if (keyword in rawLabList[i].Keywords) newLabList.add(rawLabList[i])
            }
        }
        return newLabList
    }


    private fun postThis(formBody: RequestBody, url: String, view: View) {
        val postsList = ArrayList<Posts>()
        val request = Request.Builder().url(url).post(formBody).build()
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_tap3)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "postFail")
            }

            override fun onResponse(call: Call, response: Response) {
                val test = response.body?.string() ?:""
                val jsonArray = JSONObject(test).optJSONArray("data") ?: JSONArray()
                for(i in 0 until jsonArray.length()){
                    val k = jsonArray.getJSONObject(i)
                    postsList.add(
                        Posts(subject = k.get("subject").toString(), time = k.get("create_date").toString(),
                        postID = k.get("id").toString().toInt(), content = k.get("content").toString())
                    )
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
                    recyclerView?.layoutManager = lm
                    recyclerView?.setHasFixedSize(true)
                    Log.d(TAG, "getSuccess")

                }
            }
        })
    }
}

