package com.example.myapplication.activity

import android.animation.ValueAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.myapplication.adapter.KeywordAdapter
import com.example.myapplication.adapter.DivAdapter
import com.example.myapplication.adapter.FragmentAdapter
import com.example.myapplication.adapter.KeywordAdapter
import com.example.myapplication.Fragment.FragSurf1
import com.example.myapplication.Fragment.FragSurf2
import com.example.myapplication.Fragment.FragSurf3
import com.example.myapplication.LabListLoader
import com.example.myapplication.R
import com.example.myapplication.activity.MyGlobal.Companion.globalVar
import com.example.myapplication.adapter.LabAdapter
import com.example.myapplication.data.Keyword
import com.example.myapplication.data.Lab
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import okhttp3.*
import java.io.IOException


class LabDetailActivity : AppCompatActivity(){

    val TAG: String = "로그"
    private val url = "http://192.249.18.134:80/lab/${MyGlobal.globalVar}/"

    var clickedLabItem: Lab?= null
    var isLiked: Boolean = false
    val client = OkHttpClient()
    val idList = ArrayList<String>()

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


        websiteView.setOnClickListener(){
            val url: String = clickedLabItem!!.Website
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            this.startActivity(intent)
        }
        if(globalVar!="0") {
            getFromDB()
        }
        else{
            val favoriteView = findViewById<LottieAnimationView>(R.id.favorite_btn)
            favoriteView.setOnClickListener{
                Toast.makeText(this, "즐겨찾기 기능은 로그인 후 사용 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }




    }

    private fun getFromDB() {
        idList.clear()
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "getFail")
            }
            override fun onResponse(call: Call, response: Response) {
                val test = response.body?.string()?.replace("\"", "")
                idList.addAll(test?.split(",") as MutableList<String>)
                val favoriteView = findViewById<LottieAnimationView>(R.id.favorite_btn)
                runOnUiThread {
                    var isLiked = true
                    if (idList.contains(clickedLabItem!!.Id)) {
                        val animator = ValueAnimator.ofFloat(0.09f, 0.69f).setDuration(0)
                        animator.addUpdateListener { animation: ValueAnimator ->
                            favoriteView.progress = animation.animatedValue as Float
                        }
                        animator.start()
                    } else {
                        isLiked = false
                    }

                    favoriteView.setOnClickListener {
                        if (!isLiked) {
                            val requestBody =
                                FormBody.Builder().add("labcode", clickedLabItem!!.Id).build()
                            postThis(requestBody)
                            val animator = ValueAnimator.ofFloat(0.09f, 0.69f).setDuration(3000)
                            animator.addUpdateListener { animation: ValueAnimator ->
                                favoriteView.progress = animation.animatedValue as Float
                            }
                            animator.start()
                            isLiked = true
                        } else {
                            deleteThis()
                            val animator = ValueAnimator.ofFloat(0.72f, 1f).setDuration(3000)
                            animator.addUpdateListener { animation: ValueAnimator ->
                                favoriteView.progress = animation.animatedValue as Float
                            }
                            animator.start()
                            isLiked = false
                        }
                    }
                }
            }
        })
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

    private fun postThis(formBody: RequestBody) {
        val request = Request.Builder().url(url).post(formBody).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "postFail")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "postSuccess")
            }
        })
    }

    private fun deleteThis(){
        val request = Request.Builder().delete().url(url+"${clickedLabItem!!.Id}/").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "removeFail")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "removeSuccess")
            }
        })
    }




}
