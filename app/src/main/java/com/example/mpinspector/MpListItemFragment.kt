package com.example.mpinspector

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MpListItemFragment : Fragment() {
    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View? {
        val view = infl.inflate(R.layout.fragment_item_list, cont, false)

        if (view is RecyclerView) {
            view.layoutManager = LinearLayoutManager(context)
            view.adapter = MpItemRecyclerViewAdapter(ParliamentMembersData.members)
        }
        return view
    }
}