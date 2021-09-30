package com.example.mpinspector.ui.mplist

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mpinspector.R
import com.example.mpinspector.ui.NavActions
import com.example.mpinspector.ui.adapters.GenericAdapter

abstract class MpListFragment : Fragment(), GenericAdapter.OnMyItemClick {
    protected lateinit var adapter: MpAdapter // MAJOR FOOT GUN :D

    override fun onItemClick(pos: Int) {
        val mps = adapter.currentItems2
        val mp = mps[pos]
        val nav = findNavController()
        when (nav.currentDestination?.id ?: -1) {
            R.id.nav_fav_mps -> {
                val action = NavActions.fromFavToInspect
                action.mpId = mp.personNumber
                nav.navigate(action)
            }
            R.id.nav_mp_list -> {
                val action = NavActions.fromMpListToInspect
                action.mpId = mp.personNumber
                nav.navigate(action)
            }
        }
    }

}