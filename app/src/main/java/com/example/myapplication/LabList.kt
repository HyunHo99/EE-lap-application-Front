package com.example.myapplication

import com.example.myapplication.data.Lab
import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class LabList() {
    fun loadLabList(assetManager: AssetManager): List<Lab> {
        val TAG: String = "로그"
        val loadedLabList: ArrayList<Lab> = ArrayList()
        try {
            val inputStream = assetManager.open("LabList.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val strJson = String(buffer, Charsets.UTF_8)
            val jsonObject = JSONObject(strJson)

            val labArray = jsonObject.getJSONArray("labs")
            for (i in 0 until labArray.length()) {

                val baseInfo = labArray.getJSONObject(i)
                val tempArray = baseInfo.getJSONArray("keywords")

                val keywordsArray = mutableListOf<String>()

                for (j in 0 until tempArray.length()){
                    keywordsArray.add(j, tempArray.getString(j))
                }

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
                    keywordsArray
                )
                Log.d(TAG, "id:${baseInfo.getString("id")}")
                loadedLabList.add(tempData)
            }

        } catch (e: JSONException){
            e.printStackTrace()
        } catch (e: IOException){
            e.printStackTrace()
        }
        Log.d(TAG, "loadedLabList length:${loadedLabList.size}")
        return loadedLabList
    }
}
