package com.example.myapplication.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.KeywordAdapter
import com.example.myapplication.LabListLoader
import com.example.myapplication.R
import com.example.myapplication.data.Keyword
import com.example.myapplication.data.Lab

class FragSurf2 : Fragment(){

    val TAG: String = "로그"
    var clickedKeyword: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle: Bundle? = arguments
        Log.d(TAG, "FragSurf2 - onCreateView() called. bundle: $bundle")
        clickedKeyword = bundle?.getString("clickedKeyword")
        return inflater.inflate(R.layout.fragment_surf_tab2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.keyword_recycler)
        val rawLabList = LabListLoader().loadLabList(assetManager = resources.assets)
        val newLabList = clickedKeyword?.let { filterListByKeyword(it, rawLabList) }
        var newKeywordList: ArrayList<Keyword> = ArrayList()
        if (clickedKeyword!=null && newLabList!=null)  {
            newKeywordList = makeListOfKeyword(clickedKeyword!!, newLabList)
        }

        val kAdapter = KeywordAdapter(requireContext(), newKeywordList)
        recyclerView.adapter = kAdapter
        val layout = LinearLayoutManager(activity)
        recyclerView.layoutManager = layout
        recyclerView.setHasFixedSize(true)
    }

    private fun filterListByKeyword(keyword: String, rawLabList: ArrayList<Lab>): ArrayList<Lab> {
        val newLabList: ArrayList<Lab> = ArrayList()
        val listOfDiv:List<String> = listOf("컴퓨터","신호","회로","통신","소자","전파")
        if (clickedKeyword in listOfDiv) {

            val division: String = when(keyword){
                "컴퓨터" -> "CP"
                "통신" -> "CM"
                "신호" -> "SN"
                "회로" -> "CC"
                "소자" -> "DV"
                else -> "WV"
            }
            for (i in 0 until rawLabList.size) {
                if (rawLabList[i].Division == division){
                    newLabList.add(rawLabList[i])
                    Log.d(TAG, "division: $division. newLabList=$newLabList")
                }
            }
        }
        else {
            for (i in 0 until rawLabList.size) {
                if (keyword in rawLabList[i].Keywords) newLabList.add(rawLabList[i])
            }
        }
        return newLabList
    }

    private fun makeListOfKeyword(keyword: String, newLabList: ArrayList<Lab>): ArrayList<Keyword> {
       val newKeywordList: ArrayList<Keyword> = ArrayList()

       for (i in 0 until newLabList.size) {
           val lab = newLabList[i]
           Log.d(TAG, "in i loop. lab is ${lab.Professor}")
           for (j in 0 until lab.Keywords.size) {
               // for each keyword
               Log.d(TAG, "in for loop. j=$j, Keyword=${lab.Keywords[j]}")
               val index: Int = isInKeywordList(lab.Keywords[j], newKeywordList)
               if (index>=0) {
                   newKeywordList[index].KeywordFreq ++
                   Log.d(TAG, "keyword exists. keyName=${newKeywordList[index].KeywordName}, keyFreq=${newKeywordList[index].KeywordFreq}")
               } else {
                   val tempData = Keyword(
                       lab.Keywords[j],
                       1
                   )
                   Log.d(TAG, "new keyword added to list, KeywordName=${tempData.KeywordName}")
                   newKeywordList.add(tempData)
               }
           }
       }



//       for (i in 0 until newLabList.size) {
//           Log.d(TAG, "FragSurf2 - makeListOfKeyword() called, &&&&&")
//           for (j in 0 until newLabList[i].Keywords.size) {
//               Log.d(TAG, "FragSurf2 - makeListOfKeyword() called, +++++")
//               if (newLabList[i].Keywords[j] != keyword){
//                   for (k in 0 until newKeywordList.size){
//                       Log.d(TAG, "FragSurf2 - makeListOfKeyword() called, *****")
//                       if (newLabList[i].Keywords[j] == newKeywordList[k].KeywordName){
//                           newKeywordList[k].KeywordFreq ++
//                       } else {
//                           val tempData = Keyword(
//                               newLabList[i].Keywords[j],
//                               0
//                           )
//                           Log.d(TAG, "FragSurf2 - makeListOfKeyword() called, tempData=$tempData")
//                           newKeywordList.add(tempData)
//                       }
//                   }
//               }
//           }
//       }
       newKeywordList.sortedWith(compareBy { it.KeywordFreq })
//       val finalKeywordList: ArrayList<Keyword> = ArrayList()
//       for (i in 0 until newKeywordList.size){
//           finalKeywordList.add(newKeywordList[i])
//       }
       Log.d(TAG, "FragSurf2 - makeListOfKeyword() called, Keywordnumber=${newKeywordList.size}, some keywords: ${newKeywordList[0].KeywordName},${newKeywordList[1].KeywordName} ")
       return newKeywordList
    }

    private fun isInKeywordList(keyword: String, newKeywordList: ArrayList<Keyword>): Int {
        if (newKeywordList.size ==0) return -1
        else {
            for (i in 0 until newKeywordList.size) {
                if (newKeywordList[i].KeywordName==keyword) return i
            }
            return -1
        }

    }


}



















