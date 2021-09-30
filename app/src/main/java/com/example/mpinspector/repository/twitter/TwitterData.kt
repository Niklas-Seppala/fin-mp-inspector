package com.example.mpinspector.repository.twitter

import com.example.mpinspector.MyApp
import com.example.mpinspector.repository.api.TweetResponse
import com.example.mpinspector.repository.network.Network
import com.example.mpinspector.repository.network.TwitterQueries
import com.example.mpinspector.repository.network.TwitterService

class TwitterData : TwitterDataProvider {
    private val twitterWebService = Network.twitterClient.create(TwitterService::class.java)

    override suspend fun getTweet(id: String): TweetResponse {
        return twitterWebService.getTweet(id, TwitterQueries.join(
            arrayOf(
                TwitterQueries.TweetFields.AUTHOR_ID,
                TwitterQueries.TweetFields.CREATED_AT
            )
        ),
            MyApp.TWITTER_AUTH
        )
    }
}