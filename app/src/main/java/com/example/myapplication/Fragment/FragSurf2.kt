package com.example.myapplication.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class FragSurf2 : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_surf_tab2, container, false)
    }

    fun newInstance(): FragSurf2 {
        val args = Bundle()
        val fragment = FragSurf2()
        fragment.arguments = args
        return fragment
    }


}
