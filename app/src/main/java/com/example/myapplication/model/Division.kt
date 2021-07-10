package com.example.myapplication.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Division(
    @StringRes val DivisionResourceId: Int,
    @StringRes val DivColorResourceId: Int,
    @DrawableRes val DivImgResourceId: Int
)