package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.Fragment.FragPost
import com.example.myapplication.Fragment.FragFree2
import com.example.myapplication.Fragment.FragLab
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private val fragPo by lazy { FragLab() }
    private val fragFr1 by lazy { FragPost() }
    private val fragFr2 by lazy { FragFree2() }

    val TAG: String = "로그"

    private val url = "http://192.249.18.134:80"

    private val fragments: List<Fragment> = listOf(
        fragPo, fragFr1, fragFr2
    )

    private val pagerAdapter: MainPagerAdapter by lazy {
        MainPagerAdapter(this, fragments)
    }

    var navigation: BottomNavigationView ?= null
    var vpMain: ViewPager2 ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "MainActivity - onCreate() called")
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein,R.anim.fadeout)
        setContentView(R.layout.activity_main)


//        val formBody: RequestBody = FormBody.Builder().add("subject", "test").add("content", "test").build()
//        val textView = findViewById<TextView>(R.id.textView2)
//        val client = OkHttpClient()
//        val request = Request.Builder().url(url).post(formBody).build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                runOnUiThread{textView.text = "fail to get"}
//            }
//
//
//            override fun onResponse(call: Call, response: Response) {
//                runOnUiThread{textView.text = response?.body?.string()}
//            }
//        })


        navigation = findViewById<BottomNavigationView>(R.id.navigation)
        vpMain = findViewById<ViewPager2>(R.id.vp_main)

        initViewPager()
        initNavigationBar()

    }


    private fun initNavigationBar() {
        Log.d(TAG, "MainActivity - initNavigationBar() called")

        try {
            navigation = findViewById<BottomNavigationView>(R.id.navigation)
            vpMain = findViewById<ViewPager2>(R.id.vp_main)
        } catch (e: NullPointerException) {
            Log.e(TAG, "initNavigationBar: ", e)
        }

        navigation?.run {
            this.setOnNavigationItemSelectedListener {
                val page = when(it.itemId) {
                    R.id.contact -> 0
                    R.id.game -> 1
                    R.id.gallary -> 2
                    else -> 0
                }
                if (page!= vpMain!!.currentItem){
                    vpMain!!.currentItem = page
                }
                true
            }
            selectedItemId = R.id.contact
        }
    }

    private fun initViewPager() {
        Log.d(TAG, "MainActivity - initViewPager() called")
        vpMain?.isUserInputEnabled = false

        vpMain?.run {
            this.adapter = pagerAdapter

            this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    val nav = when(position) {
                        0 -> R.id.contact
                        1 -> R.id.game
                        2 -> R.id.gallary
                        else -> R.id.contact
                    }
                    if (navigation!!.selectedItemId != nav) {
                        navigation!!.selectedItemId = nav
                    }
                }
            })
        }
    }
}






















