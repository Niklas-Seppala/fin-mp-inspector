package com.example.mpinspector.repository.twitter

import androidx.lifecycle.LiveData
import com.example.mpinspector.repository.models.TweetModelComplete
import com.example.mpinspector.repository.models.TwitterFeedModel

/**
 * Interface that provides the repository with application
 * related Twitter data.
 */
interface TwitterDataProvider {
//
//    suspend fun insertReadTweetId(readTweet: ReadTweet)
//
//    fun getReadTweetIds(): LiveData<List<String>>

    suspend fun markTweetAsRead(tweet: TweetModelComplete)

//    suspend fun deleteAllReadByOwner(mpId: Int)

    suspend fun getNewTweets();

    fun getAllNotYetRead() : LiveData<List<TweetModelComplete>>

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