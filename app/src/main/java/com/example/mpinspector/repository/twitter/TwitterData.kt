package com.example.mpinspector.repository.twitter

import androidx.lifecycle.LiveData
import com.example.mpinspector.MyApp
import com.example.mpinspector.repository.db.MpDatabase
import com.example.mpinspector.repository.models.TweetModelComplete
import com.example.mpinspector.repository.models.TwitterFeedModel
import com.example.mpinspector.repository.network.Network
import com.example.mpinspector.repository.network.TwitterQueries
import com.example.mpinspector.repository.network.TwitterService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TwitterData : TwitterDataProvider {
    private val twitterWebService = Network.twitterClient.create(TwitterService::class.java)

    override fun isMpInTwitterFeed(mpId: Int): LiveData<Boolean> {
        return MpDatabase.instance.twitterDao().existsById(mpId)
    }

    override fun getAllNotYetRead() : LiveData<List<TweetModelComplete>> {
        return MpDatabase.instance.completeTweetDao().getNotYetRead()
    }

    override suspend fun markTweetAsRead(tweet: TweetModelComplete) {
        tweet.isRead = true
        withContext(Dispatchers.IO) {
            MpDatabase.instance.completeTweetDao().update(tweet)
        }
    }

    override suspend fun getNewTweets() {
        withContext(Dispatchers.IO) {
            for (mp in MpDatabase.instance.mpTwitterDao().getSubscribed()) {
                val responseObj = twitterWebService.getTweets(mp.twitterId ?: continue,
                    count = "10",
                    header = MyApp.TWITTER_AUTH,
                    fields = TwitterQueries.join(arrayOf(
                        TwitterQueries.TweetFields.AUTHOR_ID,
                        TwitterQueries.TweetFields.CREATED_AT)
                    )
                )
                val tweets = responseObj.data
                for (t in tweets) {
                    t.attachOwner(mp)
                    MpDatabase.instance.completeTweetDao().insert(t)
                }
            }
        }
    }

    override suspend fun addMpToTwitterFeed(feed: TwitterFeedModel) {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.twitterDao().insert(feed)
        }
    }

    override suspend fun removeMpFromFeed(feed: TwitterFeedModel) {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.twitterDao().delete(feed)
        }
    }

    override fun mpHasTwitter(mpId: Int): LiveData<Boolean>  {
        return MpDatabase.instance.mpTwitterDao().mpHasTwitter(mpId)
    }
}