package com.example.myapplication.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.LabAdapter
import com.example.myapplication.LabListLoader
import com.example.myapplication.R

class FragSurf1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_surf_tab1, container, false)
    }

    fun newInstance(): FragSurf1 {
        val args = Bundle()
        val fragment = FragSurf1()
        fragment.arguments = args
        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.lab_recycler)
        val lAdapter = LabAdapter(requireContext(), LabListLoader().loadLabList(assetManager = resources.assets))
        recyclerView.adapter = lAdapter

        val layout = LinearLayoutManager(activity)
        recyclerView.layoutManager = layout
        recyclerView.setHasFixedSize(true)



    }

}
