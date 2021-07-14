package com.example.myapplication.activity

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.myapplication.LabListLoader
import com.example.myapplication.MainActivity
import com.example.myapplication.R

class LoadingActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        val TAG: String = "로그"
        Log.d(TAG, "LoadingActivity - onCreate() called")

        val loader = LabListLoader()
        val initialList = loader.loadLabList(assetManager = resources.assets)
        Log.d(TAG, "MainActivity - onCreate() called. listwithFav= ${MyGlobal.listWithFav.forEach { lab -> lab.LabImageUrl }}")
        MyGlobal.listWithFav = loader.loadBitmap(loader.loadFavicon(initialList))
        Log.d(TAG, "MainActivity - onCreate() called. listwithFav= ${MyGlobal.listWithFav.forEach { lab -> lab.LabBitmap }}")
        finish()

    }

}