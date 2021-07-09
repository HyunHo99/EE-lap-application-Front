package com.example.myapplication.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
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


class PostAdapter (private val context: Context, private val dataset: ArrayList<Posts>): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    val TAG: String = "로그"



    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val subjectView: TextView = view.findViewById(R.id.recycler_subject)
        val timeView: TextView = view.findViewById(R.id.recycler_time)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        Log.d(TAG, "ContactAdapter - onCreateViewHolder() called")
        val adapterLayout = LayoutInflater.from(context).inflate(R.layout.recycler_post, parent, false)
        return PostViewHolder(adapterLayout)
    }



    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val posts= dataset[position]
        holder.subjectView.text = posts.subject
        holder.timeView.text = posts.time
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, ShowPostActivity::class.java).apply {
                putExtra("position", position)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}





























