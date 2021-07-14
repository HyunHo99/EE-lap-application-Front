package com.example.myapplication.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.activity.SurfActivity
import com.example.myapplication.data.Keyword

class KeywordAdapter(
    private val context: Context,
    private val dataset: ArrayList<Keyword>
    ): RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder>() {

    var mPosition = 0

    fun setPosition(position: Int) {
        mPosition = position
    }

    class KeywordViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val keywordView: TextView = view.findViewById(R.id.text_keyword_only)
        val keywordImg: AppCompatImageView = view.findViewById(R.id.keyword_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_keyword, parent, false)
        return KeywordViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        val item = dataset[position]
        holder.keywordView.text = item.KeywordName

        var imageid = 0
        val listOfDiv:List<String> = listOf("컴퓨터","신호","회로","통신","소자","전파")
        if (item.KeywordName in listOfDiv){
            imageid = when(item.KeywordName){
                "컴퓨터" -> R.drawable.ic_div_cp
                "통신" -> R.drawable.ic_div_cm
                "신호" -> R.drawable.ic_div_sn
                "회로" -> R.drawable.ic_div_cc
                "소자" -> R.drawable.ic_div_dv
                else -> R.drawable.ic_div_wv
            }
        } else {
            imageid = when ((1..12).random()){
                1 -> R.drawable.ic_dm1
                2 -> R.drawable.ic_dm2
                3 -> R.drawable.ic_dm3
                4 -> R.drawable.ic_dm4
                5 -> R.drawable.ic_dm5
                6 -> R.drawable.ic_dm6
                7 -> R.drawable.ic_dm7
                8 -> R.drawable.ic_dm8
                9 -> R.drawable.ic_dm9
                10 -> R.drawable.ic_dm10
                11 -> R.drawable.ic_dm11
                12 -> R.drawable.ic_dm12
                else -> R.drawable.ic_lightning
            }
        }

        Glide.with(holder.view.context)
            .load(imageid)
            .fitCenter()
            .into(holder.keywordImg)

        holder.view.setOnClickListener {
            setPosition(position)
            val TAG: String = "로그"
            Log.d(TAG, "KeywordAdapter - onBindViewHolder() called")
            //val context = holder.view.context
            val keywordName = item.KeywordName
            val keywordFreq = item.KeywordFreq

            val intent = Intent(context, SurfActivity::class.java)
            intent.putExtra("clickedKeywordName",keywordName)
            intent.putExtra("clickedKeywordFreq", keywordFreq)
            Log.d(TAG, "KeywordAdapter - onBindViewHolder() called, intent=$intent")
//            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity?).toBundle())
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}