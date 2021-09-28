package com.example.mpinspector.repository

import com.example.mpinspector.repository.mps.MpData
import com.example.mpinspector.repository.mps.MpDataProvider
import com.example.mpinspector.repository.twitter.TwitterData
import com.example.mpinspector.repository.twitter.TwitterDataProvider

object Repository {
    val twitter: TwitterDataProvider = TwitterData()
    val mps: MpDataProvider = MpData()
}
