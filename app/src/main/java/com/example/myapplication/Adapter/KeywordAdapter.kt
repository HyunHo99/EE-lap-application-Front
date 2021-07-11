package com.example.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Keyword

class KeywordAdapter(
    private val context: Context,
    private val dataset: ArrayList<Keyword>
    ): RecyclerView.Adapter<KeywordAdapter.KeywordViewHolder>() {

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
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}