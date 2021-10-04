package com.example.mpinspector.ui

import com.example.mpinspector.ui.mplist.MpListFragmentDirections
import com.example.mpinspector.ui.mplist.FavoriteMpRecycleViewFragmentDirections
import com.example.mpinspector.ui.twitter.TwitterFeedFragmentDirections

/**
 * Shorthands for VERY verbose naming.
 */
object NavActions {
    val fromFavToInspect = FavoriteMpRecycleViewFragmentDirections.actionNavFavMpsToNavMpInspect()
    val fromMpListToInspect = MpListFragmentDirections.actionMpListItemFragmentToMpFragment()
    val fromTwitterFeedToInspect = TwitterFeedFragmentDirections.actionNavTwitterFeedToNavMpInspect()
}