package com.example.myapplication.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.Comments
import com.example.myapplication.data.Posts


class CommentAdapter (private val context: Context, private val dataset: ArrayList<Comments>): RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    val TAG: String = "로그"



    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val commentView : TextView = view.findViewById(R.id.txt_comment)
        val commentTimeView : TextView = view.findViewById(R.id.txt_commentTime)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        Log.d(TAG, "ContactAdapter - onCreateViewHolder() called")
        val adapterLayout = LayoutInflater.from(context).inflate(R.layout.recycler_comment, parent, false)
        return CommentViewHolder(adapterLayout)
    }



    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val posts = dataset[position]
        holder.commentView.text = posts.comments
        holder.commentTimeView.text = posts.time
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}