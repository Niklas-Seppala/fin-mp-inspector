package com.example.mpinspector.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.mpinspector.repository.models.*

@Dao
abstract class FavoriteDao : GenericDao<FavoriteModel>() {
    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE mpId is (:id))")
    abstract fun existsById(id: Int): LiveData<Boolean>

    @Query("SELECT * FROM mp AS m JOIN favorites AS f ON m.personNumber IS f.mpId ORDER BY f.timestamp")
    abstract fun getAllSorted(): LiveData<List<MpModel>>
}

@Dao
abstract class TweetDao : GenericDao<TweetModelComplete>() {
    @Query(
""" SELECT * FROM tweet_complete
    WHERE authorId IN 
        (SELECT mpId FROM twitter_feed) 
    ORDER BY date(createdAt) DESC""")
    abstract fun getNotYetRead(): LiveData<List<TweetModelComplete>>
}

@Dao
abstract class CommentDao : GenericDao<CommentModel>() {
    @Query("SELECT * FROM mp_comments WHERE mpId IS (:id)")
    abstract fun selectForMpId(id: Int): LiveData<MutableList<CommentModel>>
}

@Dao
abstract class MpDao : GenericDao<MpModel>() {
    @Query("SELECT picture FROM mp WHERE mp.personNumber IS (:id)")
    abstract suspend fun selectPicture(id: Int): String

    @Query("SELECT * FROM mp WHERE mp.personNumber IS (:id)")
    abstract fun selectById(id: Int): LiveData<MpModel>

    @Query("SELECT * FROM mp")
    abstract fun selectAll(): LiveData<List<MpModel>>

    @Query("SELECT * FROM mp WHERE personNumber IS(:id)")
    abstract fun getMpWithComments(id: Int): LiveData<MpWithComments>
}

@Dao
abstract class MpTwitterDao : GenericDao<MpTwitterIdModel>() {
    @Query(
""" SELECT * FROM mp AS m
    JOIN mp_twitter_data AS t
    ON m.personNumber IS t.mpId
    WHERE m.personNumber IN 
        (SELECT mpId FROM twitter_feed)""")
    abstract fun getSubscribed(): List<MpTwitterModel>

    @Query(
""" SELECT EXISTS
        (SELECT * FROM mp_twitter_data 
        WHERE mpId IS (:mpId) AND twitterId IS NOT NULL)""")
    abstract fun mpHasTwitter(mpId: Int): LiveData<Boolean>
}

@Dao
abstract class TwitterFeedDao : GenericDao<TwitterFeedModel>() {
    @Query("SELECT * FROM twitter_feed")
    abstract fun select(): LiveData<TwitterFeedModel>

    @Query("SELECT EXISTS(SELECT * FROM twitter_feed WHERE mpId is (:id))")
    abstract fun existsById(id: Int): LiveData<Boolean>
}