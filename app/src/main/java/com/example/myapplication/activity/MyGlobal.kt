package com.example.myapplication.activity

import android.app.Application
import com.example.myapplication.data.Lab

class MyGlobal : Application() {
    companion object {
        var globalVar = "0"
        var listWithFav: ArrayList<Lab> = ArrayList()
    }

    override fun onCreate() {
        super.onCreate()
        // initialization code here
    }


}