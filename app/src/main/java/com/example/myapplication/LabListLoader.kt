package com.example.myapplication

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.myapplication.data.Lab
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
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
                    null,
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
        for (lab in loadedLabList) {
            lab.LabImageUrl = when (lab.Id) {
                "cp6" -> "http://nss.kaist.ac.kr/wp-content/uploads/2020/03/logo.png"
                "cp9" -> "https://oslab.kaist.ac.kr/wp-content/uploads/2019/05/cropped-ico-32x32.png"
                "cp13" -> "https://nmsl.kaist.ac.kr/img/favicon.ico"
                "cp15" -> "https://ssl.gstatic.com/atari/images/public/favicon.ico"
                "cp16" -> "http://camelab.org/pub/images/appletouchicon.png"
                "cp20" -> "https://lh3.googleusercontent.com/4Qe34vmsKhdQBacUWAd5UvpToLRTqcfgjH7ZcH0P8LB7GHdGxnhvUXDHy1jmi2iuCEU1YocVAx8DHee9HIe8qzhN9-H0uUs"
                "cm10" -> "https://static.wixstatic.com/media/89a3bb_dd9ec0a652614917a6addeaa6bec089c~mv2.gif/v1/fill/w_32%2Ch_32%2Clg_1%2Cusm_0.66_1.00_0.01/89a3bb_dd9ec0a652614917a6addeaa6bec089c~mv2.gif"
                "cc3" -> "http://mvlsi.kaist.ac.kr/media/images/favicon.ico"
                "cc4" -> "http://castlab.kaist.ac.kr/favicon.ico"
                "cc5" -> "https://static.parastorage.com/client/pfavico.ico"
                "cc11" -> "http://nice.kaist.ac.kr/modules/admin/tpl/img/faviconSample.png"
                "cc12" -> "https://lh3.googleusercontent.com/vppxxBGfaLqgng1xis0nTKjdhPYc9Yc2p5CNwztrzQGM4ggit5rw5B-OUhMcokB5-svAlyoF0SnR1OOjbZnVVZTWiB15U7nRdUOPk7W77M_uIt7I"
                "cc13" -> "http://impact.kaist.ac.kr/_/rsrc/1506326226184/favicon.ico"
                "cc15" -> "https://ssl.gstatic.com/atari/images/public/favicon.ico"
                "cc16" -> "https://static.parastorage.com/client/pfavico.ico"
                "sn3" -> "https://static.wixstatic.com/media/5b1cac_bf65d6c23fe140c2a756f72a791051c0.png/v1/fill/w_32%2Ch_32%2Clg_1%2Cusm_0.66_1.00_0.01/5b1cac_bf65d6c23fe140c2a756f72a791051c0.png"
                "sn7" -> "https://ssl.gstatic.com/atari/images/public/favicon.ico"
                "sn10" -> "http://urobot.kaist.ac.kr/_/rsrc/1623156146667/favicon.ico"
                "sn14" -> "https://static.wixstatic.com/media/dd904b_e38bf35458504869b8fc6fa383610595~mv2.png/v1/fill/w_32%2Ch_32%2Clg_1%2Cusm_0.66_1.00_0.01/dd904b_e38bf35458504869b8fc6fa383610595~mv2.png"
                "sn16" -> "https://ssl.gstatic.com/atari/images/public/favicon.ico"
                "sn17" -> "https://static.wixstatic.com/media/e47096_d3f767aeeb8e43dc93f8e8a76bcb4a9e~mv2.jpg/v1/fill/w_32%2Ch_32%2Clg_1%2Cusm_0.66_1.00_0.01/e47096_d3f767aeeb8e43dc93f8e8a76bcb4a9e~mv2.jpg"
                "wv10" -> "http://janglab.org/figures/favicon.png"
                "dv2" -> "http://nanocore.kaist.ac.kr/fa.ico"
                "dv4" -> "http://www.google.com/images/icons/product/sites-16.ico"
                "dv5" -> "http://hsnl.kaist.ac.kr/favicon.ico"
                "dv6" -> "https://static.wixstatic.com/media/7ba163_13739aa637394f93919681e49b4fb8b2~mv2.gif/v1/fill/w_32%2Ch_32%2Clg_1%2Cusm_0.66_1.00_0.01/7ba163_13739aa637394f93919681e49b4fb8b2~mv2.gif"
                "dv8" -> "https://www.modoo.at/favicon_v1.ico"
                else -> "http://icn.kaist.ac.kr"
            }
            Log.d("로그", "id = ${lab.Id} url=${lab.LabImageUrl}")

        }
        return loadedLabList
    }


//        var favLabList: ArrayList<Lab> = ArrayList()
//            runBlocking {
//                val job = GlobalScope.launch {
//                    for (lab in loadedLabList) {
//                        val faviconUrl: String =
//                            "https://favicongrabber.com/api/grab/" + lab.Website.replace("https://", "")
//                                .replace("http://", "")
//                        try {
//                            val uri = URL(faviconUrl)
//                            try {
//                                val apiResponse = uri.readText()
//                                val iconArray: JSONArray? =
//                                    JSONObject(apiResponse).getJSONArray("icons")
//                                if (iconArray != null) {
//                                    if (iconArray.length() != 0) {
//                                        val firstIcon: JSONObject = iconArray[0] as JSONObject
//                                        lab.LabImageUrl = firstIcon.getString("src")
//                                        favLabList.add(lab)
//                                        Log.d("로그","id = ${lab.Id} url=${lab.LabImageUrl}. favlablist=${favLabList.size}")
//                                    }
//                                }
//                            } catch (e: FileNotFoundException) {
//
//                            }
//                        } catch (e: MalformedURLException) {
//                        }
//                    }
//                }
//                Log.d("로그", "before job.join. . favlablist=$favLabList")
//                job.join()
//                Log.d("로그", "after job.join.favlablist=${favLabList.size}")
//        }
//        return loadedLabList



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
                Log.d("로그", "LabListLoader - loadBitmap() called")
            }
        }
        return LabList
    }


}