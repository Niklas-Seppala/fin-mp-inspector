package com.example.mpinspector.repository

import com.example.mpinspector.repository.mps.MpData
import com.example.mpinspector.repository.mps.MpDataProvider
import com.example.mpinspector.repository.twitter.TwitterData
import com.example.mpinspector.repository.twitter.TwitterDataProvider

/**
 * Application's Repository singleton.
 *
 * All data access should flow through this object.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
object Repository {
    /**
     * Twitter related data.
     */
    val twitter: TwitterDataProvider = TwitterData()

    /**
     * Members of parliament related data.
     */
    val mps: MpDataProvider = MpData()
}
