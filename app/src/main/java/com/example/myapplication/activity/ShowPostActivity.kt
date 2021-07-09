package com.example.myapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.CommentAdapter
import com.example.myapplication.adapter.PostAdapter
import com.example.myapplication.data.Comments

class ShowPostActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showpost)

        val mydata = ArrayList<Comments>()
        mydata.add(Comments("testsetst"))
        val recyclerView = findViewById<RecyclerView>(R.id.showpost_recycler)
        val cAdapter = CommentAdapter(this, mydata)
        recyclerView?.adapter=cAdapter
        val lm = LinearLayoutManager(this)
        recyclerView.layoutManager = lm
        recyclerView?.setHasFixedSize(true)
    }
}