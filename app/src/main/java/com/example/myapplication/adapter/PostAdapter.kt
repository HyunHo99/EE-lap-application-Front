package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.format.DateUtils
import android.text.format.DateUtils.MINUTE_IN_MILLIS
import android.text.format.DateUtils.WEEK_IN_MILLIS
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.AddPostActivity
import com.example.myapplication.activity.ShowPostActivity
import com.example.myapplication.data.Posts
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class PostAdapter (private val context: Context, private val dataset: ArrayList<Posts>): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, HH:mm:ss")

    val TAG: String = "로그"
    interface ItemClick{
        fun onClick(view: View,position: Int)
    }

    var itemClick: ItemClick? = null


    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val subjectView: TextView = view.findViewById(R.id.recycler_subject)
        val timeView: TextView = view.findViewById(R.id.recycler_time)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        Log.d(TAG, "ContactAdapter - onCreateViewHolder() called")
        val adapterLayout = LayoutInflater.from(context).inflate(R.layout.recycler_post, parent, false)
        return PostViewHolder(adapterLayout)
    }



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val posts= dataset[position]
        holder.subjectView.text = posts.subject
        val k = ZonedDateTime.of(LocalDateTime.parse(posts.time, formatter), ZoneId.systemDefault()).toInstant().toEpochMilli()
        val test = DateUtils.getRelativeDateTimeString(context, k,
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.WEEK_IN_MILLIS, 1).split(" ")
        holder.timeView.text =test[0]+" "+test[1]
        if(itemClick!=null) {
            holder.itemView.setOnClickListener { v->
                itemClick?.onClick(v, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}





























