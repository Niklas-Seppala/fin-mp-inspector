package com.example.mpinspector.repository.twitter

import com.example.mpinspector.repository.models.TweetModel

interface TwitterDataProvider {
    suspend fun getTweet(id: String): TweetModel
}