package com.example.myapplication.Adapter

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataDiv
import com.example.myapplication.data.Lab
import com.example.myapplication.model.Division
import com.xiaweizi.marquee.MarqueeTextView

class LabAdapter (
    private val context: Context,
    private val dataset: ArrayList<Lab>
    ): RecyclerView.Adapter<LabAdapter.LabViewHolder>() {

    class LabViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val professorView: TextView = view.findViewById(R.id.text_professor)
        val labNameView: TextView = view.findViewById(R.id.text_lab)
        val labInitialView: TextView = view.findViewById(R.id.text_lab_initial)
        val keywordView: MarqueeTextView = view.findViewById(R.id.text_keyword)

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


        var keywordString:String = ""
        for (i in 0 until item.Keywords.size){
            keywordString += item.Keywords[i]
            keywordString += ", "
        }
        holder.keywordView.text = keywordString
        holder.keywordView.isSelected = true
        holder.keywordView.setHorizontallyScrolling(true)


        Log.d(TAG, "LabAdapter - onBindViewHolder() called, position=$position, div=$holder")

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}