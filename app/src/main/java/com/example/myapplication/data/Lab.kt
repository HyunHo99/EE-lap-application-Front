package com.example.myapplication.data

import androidx.annotation.StringRes

data class Lab (
    val Professor: String,
    val Division: String,
    val LabName: String,
    val LabInitial: String,
    val LocationProf: String,
    val LocationLab: String,
    val TelProf: String,
    val Website: String,
    val Keywords: MutableList<String>
)