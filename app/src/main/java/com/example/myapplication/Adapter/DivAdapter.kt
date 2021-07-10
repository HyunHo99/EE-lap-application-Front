package com.example.myapplication.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.SurfActivity
import com.example.myapplication.data.DataDiv
import com.example.myapplication.model.Division

class DivAdapter (
    private val context: Context,
    private val dataset: List<Division>
    ): RecyclerView.Adapter<DivAdapter.DivViewHolder>() {

    class DivViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val divView: TextView = view.findViewById(R.id.div_name)
        val divButtonView: AppCompatImageButton = view.findViewById(R.id.div_button)
//        val divImg : ImageView = view.findViewById(R.id.)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DivViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_division, parent, false)
        return DivViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: DivViewHolder, position: Int) {
        val item = dataset[position]
        val TAG: String = "로그"

        holder.divView.setText(item.DivisionResourceId)
        Log.d(TAG, "DivAdapter - onBindViewHolder() called, position=$position, div=$holder.divView.text")

        holder.divButtonView.setOnClickListener{
            val context = holder.view.context
            val intent = Intent(context, SurfActivity::class.java)
            val bundle = Bundle()
            bundle.putString("clickedKeyword", holder.divView.toString())
            context.startActivity(intent, bundle)
            Log.d(TAG, "DivAdapter - onBindViewHolder() called, bundle=$bundle")
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}