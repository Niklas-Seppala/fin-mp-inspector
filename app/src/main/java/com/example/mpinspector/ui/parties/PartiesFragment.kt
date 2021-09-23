package com.example.mpinspector.ui.parties

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mpinspector.R


class PartiesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View? {
        // Inflate the layout for this fragment
        return infl.inflate(R.layout.fragment_parties, cont, false)
    }

}