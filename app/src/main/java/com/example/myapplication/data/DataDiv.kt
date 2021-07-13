package com.example.myapplication.data

import com.example.myapplication.R

class DataDiv() {
    fun loadDivisionres(): List<Division>{
        return listOf(
            Division(R.string.div_comp,R.string.div_comp_eng, R.color.div_CP_background,R.color.div_CP_subtitle, R.color.div_CP_title, R.drawable.ic_div_cp,20),
            Division(R.string.div_comm,R.string.div_comm_eng, R.color.div_CM_background,R.color.div_CM_subtitle, R.color.div_CM_title, R.drawable.ic_div_cm, 11),
            Division(R.string.div_circ,R.string.div_circ_eng, R.color.div_CC_background,R.color.div_CC_subtitle, R.color.div_CC_title, R.drawable.ic_div_cc, 0),
            Division(R.string.div_wave,R.string.div_wave_eng, R.color.div_WV_background,R.color.div_WV_subtitle, R.color.div_WV_title, R.drawable.ic_div_wv, 0),
            Division(R.string.div_devi,R.string.div_devi_eng, R.color.div_DV_background,R.color.div_DV_subtitle, R.color.div_DV_title, R.drawable.ic_div_dv, 0),
            Division(R.string.div_sign,R.string.div_sign_eng, R.color.div_SN_background,R.color.div_SN_subtitle, R.color.div_SN_title, R.drawable.ic_div_sn, 0)
        )
    }
}