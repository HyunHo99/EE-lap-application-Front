package com.example.myapplication.activity

import android.app.Application

class MyGlobal : Application() {
    companion object {
        var globalVar = "0"
    }

    override fun onCreate() {
        super.onCreate()
        // initialization code here
    }
}