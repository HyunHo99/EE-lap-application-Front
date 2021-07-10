package com.example.myapplication.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.Adapter.FragmentAdapter
import com.example.myapplication.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class SurfActivity : AppCompatActivity(){

    val TAG: String = "로그"

    private val tabTextList = arrayListOf("연구실","관련 키워드","소식")

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "SurfActivity - onCreate() called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surf)
        init()
    }

    private fun init() {
        Log.d(TAG, "SurfActivity - init() called")
        val viewPager2 = findViewById<ViewPager2>(R.id.vp_surf)
        val tabLayout = findViewById<TabLayout>(R.id.tab_surf)
        viewPager2.adapter = FragmentAdapter(this)
        TabLayoutMediator(tabLayout, viewPager2){
            tab, position ->
            tab.text = tabTextList[position]
        }.attach()
    }

}
