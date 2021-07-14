package com.example.myapplication.data

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes

data class Division(
    @StringRes val DivisionResourceId: Int,
    @StringRes val DivisionEngResourceId: Int,
    @ColorRes val DivColorBackId: Int,
    @ColorRes val DivColorSubtitleId: Int,
    @ColorRes val DivColorTitleId: Int,
    @DrawableRes val DivImgId: Int,
    val DivisionFreqId: Int
)