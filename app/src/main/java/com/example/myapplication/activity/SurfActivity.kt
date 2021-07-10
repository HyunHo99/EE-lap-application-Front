package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.Adapter.FragmentAdapter
import com.example.myapplication.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class SurfActivity : AppCompatActivity(){

    val TAG: String = "로그"

    private val tabTextList = arrayListOf("연구실","관련 키워드","소식")
    var clickedKeyword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "SurfActivity - onCreate() called")
        super.onCreate(savedInstanceState)
//        overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter)
        setContentView(R.layout.activity_surf)
        val intentSurfActivity: Intent = intent
        clickedKeyword = intentSurfActivity.extras?.getString("clickedKeyword")
        Log.d(TAG, "SurfActivity - onCreate() called, clickedKeyword=$clickedKeyword")
        init()

        val backBtn : View = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            Log.d(TAG, "SurfActivity - onCreate() - setOnClickListener called")
            finishAfterTransition()
//            overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_right_exit)
        }
    }



    private fun init() {
        Log.d(TAG, "SurfActivity - init() called")
        val viewPager2 = findViewById<ViewPager2>(R.id.vp_surf)
        val tabLayout = findViewById<TabLayout>(R.id.tab_surf)
        val keywordTextView = findViewById<TextView>(R.id.keyword)
        keywordTextView.text = clickedKeyword
        viewPager2.adapter = FragmentAdapter(this)
        TabLayoutMediator(tabLayout, viewPager2){
            tab, position ->
            tab.text = tabTextList[position]
        }.attach()
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
    }

}
