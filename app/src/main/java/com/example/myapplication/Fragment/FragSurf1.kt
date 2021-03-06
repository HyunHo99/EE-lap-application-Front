package com.example.myapplication.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.LabAdapter
import com.example.myapplication.LabListLoader
import com.example.myapplication.R
import com.example.myapplication.activity.MyGlobal.Companion.listWithFav
import com.example.myapplication.data.Lab

class FragSurf1 : Fragment() {

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
        Log.d(TAG, "FragSurf1 - onCreateView() called. bundle: $bundle")
        clickedKeyword = bundle?.getString("clickedKeywordName")
        return inflater.inflate(R.layout.fragment_surf_tab1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.lab_recycler)
        val rawLabList0 = LabListLoader().loadLabList(assetManager = resources.assets)
        val rawLabList = listWithFav
        Log.d(TAG, "FragSurf1 - onViewCreated() called, rawlablist=$rawLabList")

        val newLabList = clickedKeyword?.let { filterListByKeyword(it, rawLabList) }

        val lAdapter = newLabList?.let { LabAdapter(requireContext(), it) }
        recyclerView.adapter = lAdapter

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

}
