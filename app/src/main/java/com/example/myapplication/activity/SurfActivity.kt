package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.adapter.FragmentAdapter
import com.example.myapplication.Fragment.FragSurf1
import com.example.myapplication.Fragment.FragSurf2
import com.example.myapplication.Fragment.FragSurf3
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class SurfActivity : AppCompatActivity(){

    val TAG: String = "로그"

    private val tabTextList = arrayListOf("연구실","관련 키워드","소식")
    var clickedKeywordName: String? = null
    var clickedKeywordFreq: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "SurfActivity - onCreate() called")
        super.onCreate(savedInstanceState)
//        overridePendingTransition(R.anim.slide_left_exit, R.anim.slide_left_enter)
        setContentView(R.layout.activity_surf)
        val intentSurfActivity: Intent = intent
        clickedKeywordName = intentSurfActivity.extras?.getString("clickedKeywordName")
        clickedKeywordFreq = intentSurfActivity.extras?.getInt("clickedKeywordFreq")
        Log.d(TAG, "SurfActivity - onCreate() called, clickedKeyword=$clickedKeywordName")
        init()

        val homeView = findViewById<AppCompatImageButton>(R.id.home_btn)

        homeView.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

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
        val keywordNumberView = findViewById<TextView>(R.id.numb_lab)
        keywordTextView.text = clickedKeywordName
        keywordNumberView.text = clickedKeywordFreq.toString()+"개의 연구실"
        val parentCategoryView = findViewById<TextView>(R.id.parent_category)

        val listOfDiv:List<String> = listOf("컴퓨터","신호","회로","통신","소자","전파")
        if (clickedKeywordName in listOfDiv) {
            parentCategoryView.text = "디비전/"
        }

        val fragSurf1 by lazy { FragSurf1() }
        val fragSurf2 by lazy { FragSurf2() }
        val fragSurf3 by lazy { FragSurf3() }


        val bundle: Bundle = Bundle()
        bundle.putString("clickedKeywordName", clickedKeywordName)
        fragSurf1.arguments = bundle
        fragSurf2.arguments = bundle
        fragSurf3.arguments = bundle
        val fAdapter : FragmentAdapter = FragmentAdapter(this)
        fAdapter.addFragment(fragSurf1)
        fAdapter.addFragment(fragSurf2)
        fAdapter.addFragment(fragSurf3)
        viewPager2.adapter = fAdapter

        TabLayoutMediator(tabLayout, viewPager2){
            tab, position ->
            tab.text = tabTextList[position]
        }.attach()
        Log.d(TAG, "SurfActivity - init() called. fragSurf1.arguments=${fragSurf1.arguments}")
    }
    override fun onBackPressed() {
        super.onBackPressed()
//        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
    }
}
