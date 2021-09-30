package com.example.mpinspector.ui

import com.example.mpinspector.ui.mplist.MpListFragmentDirections
import com.example.mpinspector.ui.mplist.FavoriteMpRecycleViewFragmentDirections

object NavActions {
    val fromFavToInspect = FavoriteMpRecycleViewFragmentDirections.actionNavFavMpsToNavMpInspect()
    val fromMpListToInspect = MpListFragmentDirections.actionMpListItemFragmentToMpFragment()
}