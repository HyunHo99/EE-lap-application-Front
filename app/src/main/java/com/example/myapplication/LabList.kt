package com.example.myapplication

import com.example.myapplication.data.Lab
import android.content.Context
import org.json.JSONObject

data class LabList(val context: Context) {
    private fun loadLabList(): List<Lab> {
        val LabList: ArrayList<Lab> = ArrayList()
        try {
            val inputStream = context.assets?.open("LabList.json")
            val size = inputStream!!.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val strJson = String(buffer, Charsets.UTF_8)
            val jsonObject = JSONObject(strJson)

            val labArray = jsonObject.getJSONArray("labs")
            for (i in 0 until labArray.length()) {
                val baseInfo = labArray.getJSONObject(i)

                val tempData = Lab(
                    baseInfo.getString("professor"),
                    baseInfo.getString("id"),
                    baseInfo.getString("division"),
                    baseInfo.getString("lab"),
                    baseInfo.getString("labInitial"),
                    baseInfo.getString("locationProf"),
                    baseInfo.getString("locationLab"),
                    baseInfo.getString("telProf"),
                    baseInfo.getString("website"),
                    baseInfo.getJSONObject("keywords")
                    
            }

        }
    }
}
