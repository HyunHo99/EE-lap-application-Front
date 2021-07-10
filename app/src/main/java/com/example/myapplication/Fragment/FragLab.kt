package com.example.myapplication.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.DivAdapter
import com.example.myapplication.R
import com.example.myapplication.activity.SurfActivity
import com.example.myapplication.data.DataDiv
import com.example.myapplication.model.Division

class FragLab : Fragment() {
    val TAG: String = "로그"
    @SuppressLint("SetJavaScriptEnabled")

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

        val searchBt : View = requireView().findViewById(R.id.search_btn)
        searchBt.setOnClickListener{
            val intent = Intent(activity, SurfActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lab, container, false)


        return view

    }
}