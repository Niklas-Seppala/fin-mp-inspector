package com.example.mpinspector.repository.twitter

import androidx.lifecycle.LiveData
import com.example.mpinspector.repository.db.MpDatabase
import com.example.mpinspector.repository.models.TweetApiQueryResult
import com.example.mpinspector.repository.models.TweetModel
import com.example.mpinspector.repository.models.TwitterFeedModel
import com.example.mpinspector.repository.network.Network
import com.example.mpinspector.repository.network.Twitter
import com.example.mpinspector.repository.network.TwitterService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant

/**
 * Provides application with Twitter related data.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
class TwitterData : TwitterDataProvider {
    private val twitterWebService = Network.twitterClient.create(TwitterService::class.java)

    /**
     * Check if MP is currently in user's twitter feed.
     * @param mpId Int MP id.
     * @return LiveData<Boolean> True if is.
     */
    override fun isMpInTwitterFeed(mpId: Int): LiveData<Boolean> {
        return MpDatabase.instance.twitterDao().existsById(mpId)
    }

    /**
     * Get all the tweets that are being stored in Room Database.
     * @return LiveData<List<TweetModel>> Tweet list.
     */
    override fun getTweets() : LiveData<List<TweetModel>> {
        return MpDatabase.instance.tweetDao().getTweets()
    }

    /**
     * Get the current twitter feed size.
     * @return LiveData<Int> Feed size.
     */
    override fun getTwitterFeedSize(): LiveData<Int> {
        return MpDatabase.instance.twitterDao().twitterFeedSize()
    }

    /**
     * Updates specified tweet's state as READ.
     * @param tweet TweetModel Updated tweet database model object.
     */
    override suspend fun markTweetAsRead(tweet: TweetModel) {
        withContext(Dispatchers.IO) {
            tweet.isRead = true
            MpDatabase.instance.tweetDao().update(tweet)
        }
    }

    /**
     * Get the latest tweets from API. If response contains not yet stored tweets,
     * store them to database.
     */
    override suspend fun loadLatestTweets() {
        withContext(Dispatchers.IO) {
            for (mp in MpDatabase.instance.mpTwitterDao().getSubscribed()) {
                mp.twitterId ?: continue // This should never happen, SQL should guarantee it.

                // Get tweets from API based on previous tweet id.
                val resp = if (mp.newestId == null) getTweetsByUser(mp.twitterId)
                    else getTweetsByUserSince(mp.twitterId, mp.newestId)

                // This means no more recent tweets available.
                val tweets = resp.data ?: continue
                // Update newest tweet id for feed model.
                MpDatabase.instance.twitterDao().update(TwitterFeedModel(mp.personNumber, resp.meta?.newestId))

                for (t in tweets) {
                    t.attachOwner(mp)
                    t.timestamp = Instant.parse(t.createdAt).epochSecond
                    MpDatabase.instance.tweetDao().insert(t)
                }
            }
        }
    }

    /**
     * Get the current twitter feed size.
     * @return LiveData<Int> Feed size.
     */
    override suspend fun addMpToTwitterFeed(feed: TwitterFeedModel) {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.twitterDao().insert(feed)
        }
    }

    /**
     * Removes MP from user's twitter feed.
     * @param feed TwitterFeedModel Feed database model object.
     */
    override suspend fun removeMpFromFeed(feed: TwitterFeedModel) {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.twitterDao().delete(feed)
        }
    }

    /**
     * Check if specified MP has Twitter account.
     * @param mpId Int MP id.
     * @return Boolean True if has.
     */
    override suspend fun mpHasTwitter(mpId: Int): Boolean  {
        return MpDatabase.instance.mpTwitterDao().mpHasTwitter(mpId)
    }

    /**
     * Get 10 latest tweets by specified user.
     *
     * @param id String Twitter user id.
     * @return TweetApiQueryResult Network response object.
     */
    private suspend fun getTweetsByUser(id: String): TweetApiQueryResult {
        return twitterWebService.getTweets(id,
            count = "10",
            header = Twitter.Auth.TWITTER_AUTH,
            fields = Twitter.join(arrayOf(
                Twitter.TweetFields.AUTHOR_ID,
                Twitter.TweetFields.CREATED_AT)
            )
        )
    }

    /**
     * Get 10 latest tweets by specified user, but only include later than
     * specified "newestId" tweets.
     *
     * @param id String Twitter user id.
     * @param newestId String fetch only newer than this tweet.
     * @return TweetApiQueryResult Network response object.
     */
    private suspend fun getTweetsByUserSince(id: String, newestId: String): TweetApiQueryResult {
        return twitterWebService.getTweets(id,
            count = "10",
            header = Twitter.Auth.TWITTER_AUTH,
            sinceId = newestId,
            fields = Twitter.join(arrayOf(
                Twitter.TweetFields.AUTHOR_ID,
                Twitter.TweetFields.CREATED_AT)
            )
        )
    }
}