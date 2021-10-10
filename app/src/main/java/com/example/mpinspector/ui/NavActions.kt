package com.example.mpinspector.ui

import com.example.mpinspector.ui.mplist.MpListFragmentDirections
import com.example.mpinspector.ui.twitter.TwitterFeedFragmentDirections
import com.example.mpinspector.ui.mplist.FavoriteMpsFragmentDirections

/**
 * Shorthands for very verbose naming.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
object NavActions {
    val fromFavToInspect = FavoriteMpsFragmentDirections.actionNavFavMpsToNavMpInspect()
    val fromMpListToInspect = MpListFragmentDirections.actionMpListItemFragmentToMpFragment()
    val fromTwitterFeedToInspect = TwitterFeedFragmentDirections.actionNavTwitterFeedToNavMpInspect()
}