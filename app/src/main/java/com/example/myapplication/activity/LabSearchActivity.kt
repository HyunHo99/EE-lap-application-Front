package com.example.myapplication.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.myapplication.LabListLoader
import com.example.myapplication.R
import com.example.myapplication.adapter.LabAdapter
import com.example.myapplication.data.Lab
import java.util.*

class LabSearchActivity : AppCompatActivity(), TextWatcher {
    val CODE = 100
    val TAG = "로그"
    lateinit var lAdapter : LabAdapter
    lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchlab)
        val labInput = findViewById<EditText>(R.id.input_searchlab)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_searchlabs)
        val rawLabList = LabListLoader().loadLabList(assetManager = resources.assets)
        labInput.addTextChangedListener(this)
        lAdapter = LabAdapter(this, rawLabList)
        lAdapter.itemClick = object : LabAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                intent.putExtra("code",rawLabList[position].Id)
                setResult(CODE, intent)
                finish()
            }
        }

        recyclerView.adapter = lAdapter
        val layout = LinearLayoutManager(this)
        recyclerView.layoutManager = layout
        recyclerView.setHasFixedSize(true)


    }

    override fun onDestroy() {
        super.onDestroy()
        setResult(0, intent)
        finish()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d(TAG, s.toString())
        lAdapter.filter.filter(s.toString())
    }

    override fun afterTextChanged(s: Editable?) {

    }
}