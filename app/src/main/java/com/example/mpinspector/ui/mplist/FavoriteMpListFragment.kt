package com.example.mpinspector.ui.mplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mpinspector.R
import com.example.mpinspector.repository.Repository
import kotlinx.coroutines.launch

class FavoriteMpListFragment : Fragment() {
    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View? {
        val view = infl.inflate(R.layout.fragment_fav_mps, cont, false)
        if (view is RecyclerView) {
            view.layoutManager = LinearLayoutManager(context)
            lifecycleScope.launch {
                val mps = Repository.mps.getFavoriteMembersOfParliament()
                view.adapter = MpRVAdapter(mps)
            }
        }
        return view
    }
}