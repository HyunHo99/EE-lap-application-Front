package com.example.myapplication.Adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_keyword, parent, false)
        return KeywordViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        val item = dataset[position]
        holder.keywordView.text = item.KeywordName

        holder.view.setOnClickListener {
            setPosition(position)
            val TAG: String = "로그"
            Log.d(TAG, "KeywordAdapter - onBindViewHolder() called")
            val context = holder.view.context
            val keywordName = item.KeywordName
            val keywordFreq = item.KeywordFreq

            val intent = Intent(context, SurfActivity::class.java)
            intent.putExtra("clickedKeywordName",keywordName)
            intent.putExtra("clickedKeywordFreq", keywordFreq)
            Log.d(TAG, "KeywordAdapter - onBindViewHolder() called, intent=$intent")
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity?).toBundle())

        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}