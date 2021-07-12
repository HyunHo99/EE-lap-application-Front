package com.example.myapplication.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.example.myapplication.Adapter.DivAdapter
import com.example.myapplication.Adapter.FragmentAdapter
import com.example.myapplication.Adapter.KeywordAdapter
import com.example.myapplication.Fragment.FragSurf1
import com.example.myapplication.Fragment.FragSurf2
import com.example.myapplication.Fragment.FragSurf3
import com.example.myapplication.LabListLoader
import com.example.myapplication.R
import com.example.myapplication.data.Keyword
import com.example.myapplication.data.Lab
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class LabDetailActivity : AppCompatActivity(){

    val TAG: String = "로그"

    var clickedLabItem: Lab?= null
    var isLiked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab_detail)
        val intentLabDetailActivity: Intent = intent
        clickedLabItem = intentLabDetailActivity.extras?.get("clickedLab") as Lab?
        init()

        val backBtn : View = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            finishAfterTransition()
        }
        val websiteView = findViewById<AppCompatButton>(R.id.web_btn)
        val locationView = findViewById<AppCompatImageButton>(R.id.location_btn)
        val telView = findViewById<AppCompatImageButton>(R.id.call_btn)
        val favoriteView = findViewById<LottieAnimationView>(R.id.favorite_btn)

        websiteView.setOnClickListener(){
            val url: String = clickedLabItem!!.Website
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            this.startActivity(intent)
        }

        isLiked = false

        favoriteView.setOnClickListener{
            if (!isLiked){
                val animator = ValueAnimator.ofFloat(0.09f,0.69f).setDuration(3000)
                animator.addUpdateListener { animation: ValueAnimator ->
                    favoriteView.progress = animation.animatedValue as Float
                }
                animator.start()
                isLiked = true
            } else {
                val animator = ValueAnimator.ofFloat(0.72f, 1f).setDuration(3000)
                animator.addUpdateListener { animation: ValueAnimator ->
                    favoriteView.progress = animation.animatedValue as Float
                }
                animator.start()
                isLiked = false
            }
        }


    }



    private fun init() {

        val professorTextView = findViewById<TextView>(R.id.labdetail_professor)
        val labTextView = findViewById<TextView>(R.id.labdetail_labname)
        val divisionTextView = findViewById<TextView>(R.id.text_division)

        professorTextView.text = clickedLabItem?.Professor
        labTextView.text = clickedLabItem?.LabName
        divisionTextView.text = when(clickedLabItem?.Division){
            "CP" -> "컴퓨터"
            "CM" -> "통신"
            "CC" -> "회로"
            "SN" -> "신호"
            "DV" -> "소자"
            else -> "전파"
        }

        val recyclerView = findViewById<RecyclerView>(R.id.keyword_recycler)
        val keywordOfLab = makeKeywordArray(clickedLabItem!!.Keywords)
        Log.d(TAG, "LabDetailActivity - init() called, keywordOfLab=$keywordOfLab")
        val kAdapter = KeywordAdapter(this, keywordOfLab)
        recyclerView.adapter = kAdapter
        val layout = LinearLayoutManager(this)
        recyclerView.layoutManager = layout
        recyclerView.setHasFixedSize(true)
    }
    override fun onBackPressed() {
        super.onBackPressed()
//        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit)
    }

    private fun makeKeywordArray(keywordlist: MutableList<String>): ArrayList<Keyword> {
        val keywordArrayList: ArrayList<Keyword> = ArrayList()
        for (keyword in keywordlist){
            val tempData = Keyword(
                keyword,
                0
            )
            keywordArrayList.add(tempData)
        }
        val rawLabList = LabListLoader().loadLabList(assetManager = resources.assets)
        getKeywordFreq(keywordArrayList, rawLabList)
        Log.d(TAG, "LabDetailActivity - makeKeywordArray() called. keywordarraylist = $keywordArrayList")
        return keywordArrayList
    }

    private fun getKeywordFreq(newKeywordList: ArrayList<Keyword>, rawLabList: ArrayList<Lab>){

        // each keyword
        for (eachKeyword in newKeywordList){
            for (eachLab in rawLabList) {
                if (eachKeyword.KeywordName in eachLab.Keywords){
                    eachKeyword.KeywordFreq ++
                }
            }
        }

    }
}
