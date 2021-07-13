package com.example.myapplication.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.LabDetailActivity
import com.example.myapplication.data.Lab
import com.xiaweizi.marquee.MarqueeTextView

class LabAdapter (
    private val context: Context,
    private val dataset: ArrayList<Lab>
    ): RecyclerView.Adapter<LabAdapter.LabViewHolder>() {

    var mPosition = 0
    val TAG: String = "로그"

    fun setPosition(position: Int) {
        mPosition = position
    }

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

        holder.professorView.text = item.Professor
        holder.labNameView.text = item.LabName
        holder.labInitialView.text = item.LabInitial


        var keywordString:String = ""
        for (i in 0 until item.Keywords.size){
            keywordString += item.Keywords[i]
            if (i!=item.Keywords.size-1) {keywordString += ", "}
        }
        holder.keywordView.text = keywordString
        holder.keywordView.isSelected = true
        holder.keywordView.setHorizontallyScrolling(true)

        holder.view.setOnClickListener {
            setPosition(position)
            val context = holder.view.context
            val intent = Intent(context, LabDetailActivity::class.java)
            intent.putExtra("clickedLab",item)
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity?).toBundle())

        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}