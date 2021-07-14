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
import com.bumptech.glide.Glide
import com.example.myapplication.LabListLoader
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.activity.MyGlobal.Companion.globalVar
import com.example.myapplication.activity.MyGlobal.Companion.listWithFav
import com.example.myapplication.adapter.KeywordAdapter
import com.example.myapplication.data.Keyword
import com.example.myapplication.data.Lab
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
        val homeView = findViewById<AppCompatImageButton>(R.id.home_btn)

        homeView.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        locationView.setOnClickListener(){
            val locationLab: String = clickedLabItem!!.LocationLab
            val locationProf: String = clickedLabItem!!.LocationProf
            Toast.makeText(this, "연구실: {$locationLab}\n교수님: {$locationProf}", Toast.LENGTH_LONG).show()
        }

        telView.setOnClickListener(){
            Toast.makeText(this,"길게 누르면 교수님 오피스 전화로 이동해요.", Toast.LENGTH_SHORT).show()
        }
        telView.setOnLongClickListener{
            val telNumber: String = clickedLabItem!!.TelProf.toString()
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:042350"+telNumber))
            startActivity(intent)
            return@setOnLongClickListener true
        }

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
        val divImg = findViewById<AppCompatImageButton>(R.id.div_img)

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

        val divimgid: Int = when(clickedLabItem?.Division){
            "CP" -> R.drawable.ic_div_cp
            "CM" -> R.drawable.ic_div_cm
            "CC" -> R.drawable.ic_div_cc
            "SN" -> R.drawable.ic_div_sn
            "DV" -> R.drawable.ic_div_dv
            else -> R.drawable.ic_div_wv
        }

        Glide.with(this)
            .load(divimgid)
            .fitCenter()
            .into(divImg)


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
        getKeywordFreq(keywordArrayList, listWithFav)
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
