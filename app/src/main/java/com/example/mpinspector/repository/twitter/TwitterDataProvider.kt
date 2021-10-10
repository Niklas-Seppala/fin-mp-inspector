package com.example.mpinspector.repository.twitter

import androidx.lifecycle.LiveData
import com.example.mpinspector.repository.models.TweetModel
import com.example.mpinspector.repository.models.TwitterFeedModel

/**
 * Interface that provides the repository with application
 * related Twitter data.
 *
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
*/
interface TwitterDataProvider {

    /**
     * Get the current twitter feed size.
     * @return LiveData<Int> Feed size.
     */
    fun getTwitterFeedSize(): LiveData<Int>

    /**
     * Updates specified tweet's state as READ.
     * @param tweet TweetModel Updated tweet database model object.
     */
    suspend fun markTweetAsRead(tweet: TweetModel)

    /**
     * Get the latest tweets from API. If response contains not yet stored tweets,
     * store them to database.
     */
    suspend fun loadLatestTweets();

    /**
     * Get all the tweets that are being stored in Room Database.
     * @return LiveData<List<TweetModel>> Tweet list.
     */
    fun getTweets() : LiveData<List<TweetModel>>

    /**
     * Get the current twitter feed size.
     * @return LiveData<Int> Feed size.
     */
    suspend fun addMpToTwitterFeed(feed: TwitterFeedModel)

    /**
     * Removes MP from user's twitter feed.
     * @param feed TwitterFeedModel Feed database model object.
     */
    suspend fun removeMpFromFeed(feed: TwitterFeedModel)

    /**
     * Check if specified MP has Twitter account.
     * @param mpId Int MP id.
     * @return Boolean True if has.
     */
    suspend fun mpHasTwitter(mpId: Int): Boolean

    /**
     * Check if MP is currently in user's twitter feed.
     * @param mpId Int MP id.
     * @return LiveData<Boolean> True if is.
     */
    fun isMpInTwitterFeed(mpId: Int): LiveData<Boolean>
}