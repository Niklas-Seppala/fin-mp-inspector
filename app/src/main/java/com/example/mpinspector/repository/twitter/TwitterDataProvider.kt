package com.example.mpinspector.repository.twitter

import androidx.lifecycle.LiveData
import com.example.mpinspector.repository.models.TweetModel
import com.example.mpinspector.repository.models.TwitterFeedModel

/**
 * Interface that provides the repository with application
 * related Twitter data.
 */
interface TwitterDataProvider {

    fun twitterFeedSize(): LiveData<Int>

    suspend fun markTweetAsRead(tweet: TweetModel)

//    suspend fun deleteAllReadByOwner(mpId: Int)

    suspend fun getNewTweets();

    fun getAllNotYetRead() : LiveData<List<TweetModel>>

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
    suspend fun mpHasTwitter(mpId: Int): Boolean

    /**
     *
     * @param mpId Int
     * @return LiveData<Boolean>
     */
    fun isMpInTwitterFeed(mpId: Int): LiveData<Boolean>
}