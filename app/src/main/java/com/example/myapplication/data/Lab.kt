package com.example.myapplication.data

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lab(
    val Professor: String,
    val Id: String,
    val Division: String,
    val LabName: String,
    val LabInitial: String,
    val LocationProf: String,
    val LocationLab: String,
    val TelProf: String,
    val Website: String,
    val Keywords: MutableList<String>,
    var LabImageUrl: String?,
    var LabBitmap: Bitmap?
) : Parcelable