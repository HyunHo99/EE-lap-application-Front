package com.example.myapplication.Fragment

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.DivAdapter
import com.example.myapplication.adapter.LabAdapter
import com.example.myapplication.LabListLoader
import com.example.myapplication.R
import com.example.myapplication.activity.MyGlobal.Companion.globalVar
import com.example.myapplication.activity.ShowPostActivity
import com.example.myapplication.activity.SurfActivity
import com.example.myapplication.adapter.PostAdapter
import com.example.myapplication.data.DataDiv
import com.example.myapplication.data.Lab
import com.example.myapplication.model.Division
import com.google.android.gms.auth.api.signin.GoogleSignIn
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class FragLab : Fragment() {
    val TAG: String = "로그"

    @SuppressLint("SetJavaScriptEnabled")
    private var url = "http://192.249.18.134:80/lab/$globalVar/"
    private val idList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val divList: List<Division> = DataDiv().loadDivisionres()
        val TAG = "로그"
        Log.d(TAG, "FragLab - onViewCreated() called. divList=$divList")
        val recyclerViewDiv = requireView().findViewById<RecyclerView>(R.id.div_recycler)
        val dAdapter = DivAdapter(requireContext(), divList)
        recyclerViewDiv.adapter = dAdapter
        val layout = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewDiv.layoutManager = layout
        recyclerViewDiv.setHasFixedSize(true)


//        val rawLabList = LabListLoader().loadLabList(assetManager = resources.assets)
//        val idList: MutableList<String> = mutableListOf("cm1", "cm2", "cp5")
//        val favLabList = getFavLabListById(idList, rawLabList)
//
//        val recyclerViewFav = requireView().findViewById<RecyclerView>(R.id.favlab_recycler)
//        val flAdapter = LabAdapter(requireContext(), favLabList)
//        recyclerViewFav.adapter = flAdapter
//        val layoutF = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//        recyclerViewFav.layoutManager = layoutF
//        recyclerViewFav.setHasFixedSize(true)


        val searchBt: View = requireView().findViewById(R.id.search_btn)
        searchBt.setOnClickListener {
            val intent = Intent(activity, SurfActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
//            requireActivity().overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit)

        }

    }

    private fun getFavLabListById(
        idList: ArrayList<String>,
        rawLabList: ArrayList<Lab>
    ): ArrayList<Lab> {
        val favLabList: ArrayList<Lab> = ArrayList()
        for (id in idList) {
            for (lab in rawLabList) {
                if (id.contentEquals(lab.Id)) {
                    favLabList.add(lab)
                    break
                }
            }
        }
        return favLabList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lab, container, false)
        return view
    }


    private fun getFromDB() {
        idList.clear()
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "getFail")
            }
            override fun onResponse(call: Call, response: Response) {
                val test = response.body?.string()?.replace("\"","")
                idList.addAll(test?.split(",") as MutableList<String>)
                val rawLabList = LabListLoader().loadLabList(assetManager = resources.assets)
                val favLabList = getFavLabListById(idList, rawLabList)
                val recyclerViewFav = requireView().findViewById<RecyclerView>(R.id.favlab_recycler)
                val flAdapter = LabAdapter(requireContext(), favLabList)
                activity?.runOnUiThread {
                    recyclerViewFav.adapter = flAdapter
                    val layoutF = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                    recyclerViewFav.layoutManager = layoutF
                    recyclerViewFav.setHasFixedSize(true)
                    Log.d(TAG, "success")
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if(globalVar!="0") {
            url = "http://192.249.18.134:80/lab/$globalVar/"
            getFromDB()
        }
        else{
            Toast.makeText(context, "즐겨찾기 기능은 로그인 후 사용 가능합니다.", Toast.LENGTH_SHORT).show()
        }
    }
}





















