package com.example.mpinspector.ui

import com.example.mpinspector.ui.mplist.FavoriteMpListFragmentDirections
import com.example.mpinspector.ui.mplist.MpListItemFragmentDirections

object NavActions {
    val fromFavToInspect = FavoriteMpListFragmentDirections.actionNavFavMpsToNavMpInspect()
    val fromMpListToInspect = MpListItemFragmentDirections.actionMpListItemFragmentToMpFragment()
}