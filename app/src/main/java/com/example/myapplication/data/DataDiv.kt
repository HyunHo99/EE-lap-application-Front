package com.example.myapplication.data

import com.example.myapplication.R
import com.example.myapplication.model.Division

class DataDiv() {
    fun loadDivisionres(): List<Division>{
        return listOf(
            Division(R.string.div_comp,R.string.div_color, R.drawable.spareimg),
            Division(R.string.div_comm,R.string.div_color, R.drawable.spareimg),
            Division(R.string.div_circ,R.string.div_color, R.drawable.spareimg),
            Division(R.string.div_wave,R.string.div_color, R.drawable.spareimg),
            Division(R.string.div_devi,R.string.div_color, R.drawable.spareimg),
            Division(R.string.div_sign,R.string.div_color, R.drawable.spareimg)
        )
    }
}