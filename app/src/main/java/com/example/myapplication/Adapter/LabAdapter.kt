package com.example.myapplication.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataDiv
import com.example.myapplication.data.Lab
import com.example.myapplication.model.Division

class LabAdapter (
    private val context: Context,
    private val dataset: ArrayList<Lab>
    ): RecyclerView.Adapter<LabAdapter.LabViewHolder>() {

    class LabViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val professorView: TextView = view.findViewById(R.id.text_professor)
        val labNameView: TextView = view.findViewById(R.id.text_lab)
        val labInitialView: TextView = view.findViewById(R.id.text_lab_initial)
        val keyword1View: TextView = view.findViewById(R.id.keyword1)
        val keyword2View: TextView = view.findViewById(R.id.keyword2)
        val keyword3View: TextView = view.findViewById(R.id.keyword3)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_lab, parent, false)
        return LabViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: LabViewHolder, position: Int) {
        val item = dataset[position]
        val TAG: String = "로그"

        holder.professorView.text = item.Professor
        holder.labNameView.text = item.LabName
        holder.labInitialView.text = item.LabInitial
        Log.d(TAG,"keywords: ${item.Keywords}")
        if (item.Keywords.size>2){
        holder.keyword1View.text = item.Keywords[0]
        holder.keyword2View.text = item.Keywords[1]
        holder.keyword3View.text = item.Keywords[2]
        } else {
            holder.keyword1View.text = "정보 없음"
            holder.keyword2View.text = "정보 없음"
            holder.keyword3View.text = "정보 없음"
        }

        Log.d(TAG, "LabAdapter - onBindViewHolder() called, position=$position, div=$holder")

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}