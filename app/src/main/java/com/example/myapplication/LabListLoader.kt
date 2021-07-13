package com.example.myapplication

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.myapplication.data.Lab
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class LabListLoader() {
    fun loadLabList(assetManager: AssetManager): ArrayList<Lab> {
        var loadedLabList: ArrayList<Lab> = ArrayList()
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

                for (j in 0 until tempArray.length()) {
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
                    keywordsArray,
                    baseInfo.getString("website")+"/favicon.ico",
                    null
                )
                loadedLabList.add(tempData)
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return loadedLabList
    }
    fun loadFavicon(loadedLabList: ArrayList<Lab>):ArrayList<Lab> {
        var favLabList: ArrayList<Lab> = ArrayList()
            runBlocking {
                val job = GlobalScope.launch {
                    for (lab in loadedLabList) {
                        val faviconUrl: String =
                            "https://favicongrabber.com/api/grab/" + lab.Website.replace("https://", "")
                                .replace("http://", "")
                        try {
                            val uri = URL(faviconUrl)
                            try {
                                val apiResponse = uri.readText()
                                val iconArray: JSONArray? =
                                    JSONObject(apiResponse).getJSONArray("icons")
                                if (iconArray != null) {
                                    if (iconArray.length() != 0) {
                                        val firstIcon: JSONObject = iconArray[0] as JSONObject
                                        lab.LabImageUrl = firstIcon.getString("src")
                                        favLabList.add(lab)
                                        Log.d("로그","url=${lab.LabImageUrl}. favlablist=${favLabList.size}")
                                    }
                                }
                            } catch (e: FileNotFoundException) {

                            }
                        } catch (e: MalformedURLException) {
                        }
                    }
                }
                Log.d("로그", "before job.join. . favlablist=$favLabList")
                job.join()
                Log.d("로그", "after job.join.favlablist=${favLabList.size}")
        }
        return loadedLabList
    }
    val TAG: String = "로그"

    public final fun URL.toBitmap(): Bitmap?{
        return try{
            BitmapFactory.decodeStream(openStream())
        } catch (e: IOException){
            null
        }
    }

    fun loadBitmap(LabList: ArrayList<Lab>):ArrayList<Lab>{
        for (lab in LabList){
            val url = URL(lab.LabImageUrl)
            val result: Deferred<Bitmap?> = GlobalScope.async {
                url.toBitmap()
            }

            GlobalScope.launch(Dispatchers.Main) {
                // show bitmap on image view when available
                lab.LabBitmap = result.await()
                Log.d(TAG, "LabListLoader - loadBitmap() called")
            }
        }
        return LabList
    }


}