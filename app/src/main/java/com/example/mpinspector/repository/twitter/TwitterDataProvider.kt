package com.example.mpinspector.repository.twitter

import androidx.lifecycle.LiveData
import com.example.mpinspector.repository.models.ReadTweet
import com.example.mpinspector.repository.models.TweetModel
import com.example.mpinspector.repository.models.TwitterFeedModel

/**
 * Interface that provides the repository with application
 * related Twitter data.
 */
interface TwitterDataProvider {

    suspend fun insertReadTweetId(readTweet: ReadTweet)

    fun getReadTweetIds(): LiveData<List<String>>

    suspend fun deleteAllReadByOwner(mpId: Int)

    suspend fun getTweets(twitterId: String): List<TweetModel>

    /**
     *
     * @param feed TwitterFeedModel
     */
    suspend fun addMpToTwitterFeed(feed: TwitterFeedModel)

    /**
     *
     * @param feed TwitterFeedModel
     */
    suspend fun removeMpFromFeed(feed: TwitterFeedModel)

    /**
     *
     * @param mpId Int
     * @return Boolean
     */
    fun mpHasTwitter(mpId: Int): LiveData<Boolean>

    /**
     *
     * @param mpId Int
     * @return LiveData<Boolean>
     */
    fun isMpInTwitterFeed(mpId: Int): LiveData<Boolean>
}