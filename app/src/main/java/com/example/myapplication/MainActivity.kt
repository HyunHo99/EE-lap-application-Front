package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.Fragment.FragPost

import com.example.myapplication.Fragment.FragLab

import com.example.myapplication.Fragment.FragLogin
import com.example.myapplication.Fragment.FragData
import com.example.myapplication.activity.LoadingActivity
import com.example.myapplication.activity.MyGlobal.Companion.globalVar
import com.example.myapplication.activity.MyGlobal.Companion.listWithFav
import com.example.myapplication.data.Lab
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay


class MainActivity : AppCompatActivity() {

    private val fragPo by lazy { FragLab() }
    private val fragFr1 by lazy { FragPost() }
    private val fragFr2 by lazy { FragLogin() }

    val TAG: String = "로그"


    private val url = "http://192.249.18.134:80"
    val fragments = listOf(fragPo, fragFr1, fragFr2)

    private val pagerAdapter: MainPagerAdapter by lazy {
        MainPagerAdapter(this, fragments)
    }

    var navigation: BottomNavigationView ?= null
    var vpMain: ViewPager2 ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "MainActivity - onCreate() called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val gsa = GoogleSignIn.getLastSignedInAccount(this)
        if(gsa?.id!=null){
            globalVar = gsa.id!!
        }

        navigation = findViewById<BottomNavigationView>(R.id.navigation)
        vpMain = findViewById<ViewPager2>(R.id.vp_main)

        initViewPager()
        initNavigationBar()

        if (listWithFav.size==0) {
            Log.d(TAG, "MainActivity - onCreate() called. listWithFav.size=${listWithFav.size} ")
            val intent = Intent(this, LoadingActivity::class.java)
            Log.d(TAG, "MainActivity - onCreate() called. intent=$intent")
            startActivity(intent)
        }


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






















