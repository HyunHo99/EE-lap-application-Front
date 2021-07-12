package com.example.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Comments
import com.example.myapplication.data.Posts
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class CommentAdapter (private val context: Context, private val dataset: ArrayList<Comments>): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    val TAG: String = "로그"

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, HH:mm:ss")

    interface ItemClick{
        fun onClick(view: View,position: Int)
    }
    var itemClick: CommentAdapter.ItemClick? = null


    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val commentView : TextView = view.findViewById(R.id.txt_comment)
        val commentTimeView : TextView = view.findViewById(R.id.txt_commentTime)
        val deleteButton : View = view.findViewById(R.id.bt_deletecomment)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        Log.d(TAG, "ContactAdapter - onCreateViewHolder() called")
        val adapterLayout = LayoutInflater.from(context).inflate(R.layout.recycler_comment, parent, false)
        return CommentViewHolder(adapterLayout)
    }



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val posts = dataset[position]
        holder.commentView.text = posts.comments
        val k = ZonedDateTime.of(LocalDateTime.parse(posts.time, formatter), ZoneId.systemDefault()).toInstant().toEpochMilli()
        val test = DateUtils.getRelativeDateTimeString(context, k,
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.WEEK_IN_MILLIS, 1).split(" ")
        holder.commentTimeView.text =test[0]+" "+test[1]
        if(itemClick!=null) {
            holder.deleteButton.setOnClickListener { v->
                itemClick?.onClick(v, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}