package com.example.mpinspector.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.mpinspector.repository.models.MpTwitterIdModel
import com.example.mpinspector.repository.models.MpTwitterModel
import com.example.mpinspector.repository.models.ReadTweet

@Dao
abstract class MpTwitterDao : GenericDao<MpTwitterIdModel>() {

    @Query(
""" SELECT * FROM mp 
        AS m
    JOIN mp_twitter_data
        AS t
    ON m.personNumber IS t.mpId
    WHERE m.personNumber IN 
        (SELECT mpId FROM twitter_feed)""")
    abstract fun getMps(): LiveData<List<MpTwitterModel>>

    @Query("SELECT EXISTS(SELECT * FROM mp_twitter_data WHERE mpId IS (:mpId) AND twitterId IS NOT NULL)")
    abstract fun mpHasTwitter(mpId: Int): LiveData<Boolean>
}

@Dao
abstract class ReadTweetDao : GenericDao<ReadTweet>() {
    @Query("SELECT tweetId FROM read_tweet")
    abstract fun getReadTweetIds(): LiveData<List<String>>

    @Query("DELETE FROM read_tweet WHERE owner IS (:id)")
    abstract fun deleteAllBy(id: Int)
}
