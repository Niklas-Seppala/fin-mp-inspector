package com.example.mpinspector.repository.twitter

import com.example.mpinspector.repository.api.TweetResponse

/**
 * Interface that provides the repository with application
 * related Twitter data.
 */
interface TwitterDataProvider {
    suspend fun getTweet(id: String): TweetResponse
}