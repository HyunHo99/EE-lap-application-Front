package com.example.myapplication.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.SurfActivity
import com.example.myapplication.data.Division

class DivAdapter (
    private val context: Context,
    private val dataset: List<Division>
    ): RecyclerView.Adapter<DivAdapter.DivViewHolder>() {

    class DivViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val divNameKor: TextView = view.findViewById(R.id.div_name)
        val divNameEng: TextView = view.findViewById(R.id.div_name_eng)
        val divImg: ImageView = view.findViewById(R.id.div_img)
        val divButtonView: AppCompatImageButton = view.findViewById(R.id.div_button)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DivViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_division, parent, false)
        return DivViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: DivViewHolder, position: Int) {
        val item = dataset[position]
        val TAG: String = "로그"

        val colorBack = context.getColor(item.DivColorBackId)
        val colorTitle = context.getColor(item.DivColorTitleId)
        val colorSubTitle = context.getColor(item.DivColorSubtitleId)

        holder.divNameKor.setText(item.DivisionResourceId)
        holder.divNameKor.setTextColor(colorTitle)
        holder.divNameEng.setText(item.DivisionEngResourceId)
        holder.divNameEng.setTextColor(colorSubTitle)
        holder.divImg.setImageResource(item.DivImgId)

        holder.divButtonView.setBackgroundColor(colorBack)

        Log.d(TAG, "DivAdapter - onBindViewHolder() called, position=$position, div=$holder.divView.text")

        holder.divButtonView.setOnClickListener{
            val context = holder.view.context
            val divisionFreq: Int = item.DivisionFreqId
            val intent = Intent(context, SurfActivity::class.java)

            intent.putExtra("clickedKeywordFreq", divisionFreq)
            Log.d(TAG, "DivAdapter - onBindViewHolder() called, intent=$intent")
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity?).toBundle())
        }
    }


    override fun getItemCount(): Int {
        return dataset.size
    }

}