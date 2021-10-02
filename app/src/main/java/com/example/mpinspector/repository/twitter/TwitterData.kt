package com.example.mpinspector.repository.twitter

import androidx.lifecycle.LiveData
import com.example.mpinspector.MyApp
import com.example.mpinspector.repository.network.api.TweetResponse
import com.example.mpinspector.repository.db.MpDatabase
import com.example.mpinspector.repository.models.TweetModel
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

    override suspend fun getTweet(id: String): TweetModel {
        return twitterWebService.getTweet(id, TwitterQueries.join(
            arrayOf(
                TwitterQueries.TweetFields.AUTHOR_ID,
                TwitterQueries.TweetFields.CREATED_AT
            )
        ),
            MyApp.TWITTER_AUTH
        ).data
    }
}