package com.example.myapplication.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.activity.LabDetailActivity
import com.example.myapplication.data.Lab
import com.xiaweizi.marquee.MarqueeTextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class LabAdapter (
    private val context: Context,
    private val dataset: ArrayList<Lab>
    ): RecyclerView.Adapter<LabAdapter.LabViewHolder>(), Filterable {

    interface ItemClick{
        fun onClick(view: View,position: Int)
    }
    var itemClick: LabAdapter.ItemClick? = null
    private var LabSearchList: List<Lab>? = null

    init {
        this.LabSearchList = dataset
    }

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
        val labImg: AppCompatImageView = view.findViewById(R.id.lab_img)
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
        Glide.with(holder.view.context).load(item.LabBitmap).into(holder.labImg)

        var keywordString:String = ""
        for (i in 0 until item.Keywords.size){
            keywordString += item.Keywords[i]
            if (i!=item.Keywords.size-1) {keywordString += ", "}
        }
        holder.keywordView.text = keywordString
        holder.keywordView.isSelected = true
        holder.keywordView.setHorizontallyScrolling(true)
        if(itemClick==null) {
            holder.view.setOnClickListener {
                setPosition(position)
                val context = holder.view.context
                val intent = Intent(context, LabDetailActivity::class.java)
                intent.putExtra("clickedLab", item)
                context.startActivity(
                    intent,
                    ActivityOptions.makeSceneTransitionAnimation(context as Activity?).toBundle()
                )
            }
        }
        else{
            holder.view.setOnClickListener{
                v-> itemClick?.onClick(v, position)
            }
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    LabSearchList = dataset
                } else {
                    val filteredList = ArrayList<Lab>()
                    //이부분에서 원하는 데이터를 검색할 수 있음
                    for (row in dataset) {
                        if (row.Professor.lowercase(Locale.getDefault()).contains(charString.lowercase(
                                Locale.getDefault()
                            )) || row.Id.lowercase(Locale.getDefault()).contains(charString.lowercase(
                                Locale.getDefault()
                            ))
                            || row.Division.lowercase(Locale.getDefault()).contains(charString.lowercase(
                                Locale.getDefault()
                            )) || row.LabName.lowercase(Locale.getDefault()).contains(charString.lowercase(
                                Locale.getDefault()
                            ))) {
                            filteredList.add(row)
                        }
                    }
                    LabSearchList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = LabSearchList
                return filterResults
            }
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                LabSearchList = filterResults.values as ArrayList<Lab>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return LabSearchList!!.size
    }


}