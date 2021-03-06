package com.example.mpinspector.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.example.mpinspector.repository.models.*

/**
 * Data-access object class for favorites.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
@Dao
abstract class FavoriteDao : GenericDao<FavoriteModel>() {
    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE mpId is (:id))")
    abstract fun existsById(id: Int): LiveData<Boolean>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM mp AS m JOIN favorites AS fav ON m.personNumber IS fav.mpId ORDER BY fav.timestamp ASC")
    abstract fun getAllSorted(): LiveData<List<MpModel>>
}

/**
 * Data-access object class for Tweets.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
@Dao
abstract class TweetDao : GenericDao<TweetModel>() {
    @Query(
""" SELECT * FROM tweet
    WHERE authorId IN 
        (SELECT mpId FROM twitter_feed) 
    ORDER BY timestamp DESC""")
    abstract fun getTweets(): LiveData<List<TweetModel>>
}

/**
 *
 * Data-access object class for Comments.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
@Dao
abstract class CommentDao : GenericDao<CommentModel>() {
    @Query("SELECT * FROM mp_comments WHERE mpId IS (:id)")
    abstract fun selectForMpId(id: Int): LiveData<MutableList<CommentModel>>
}

/**
 * Data-access object class for MPs.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
@Dao
abstract class MpDao : GenericDao<MpModel>() {
    @Query("SELECT picture FROM mp WHERE mp.personNumber IS (:id)")
    abstract suspend fun selectPicture(id: Int): String

    @Query("SELECT * FROM mp WHERE mp.personNumber IS (:id)")
    abstract fun selectById(id: Int): LiveData<MpModel>

    @Query("SELECT * FROM mp ORDER BY last")
    abstract fun selectAll(): LiveData<List<MpModel>>

    @RewriteQueriesToDropUnusedColumns
    @Transaction
    @Query("SELECT * FROM mp WHERE personNumber IS(:id)")
    abstract fun getMpWithComments(id: Int): LiveData<MpWithComments>
}

/**
 * Data-access object class for MP twitter data.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
@Dao
abstract class MpTwitterDao : GenericDao<MpTwitterIdModel>() {
    @RewriteQueriesToDropUnusedColumns
    @Query(
""" SELECT * FROM mp AS m
    JOIN mp_twitter_data AS t
    ON m.personNumber IS t.mpId
    JOIN twitter_feed AS asd
    ON m.personNumber IS asd.mpId
    WHERE m.personNumber IN 
        (SELECT mpId FROM twitter_feed)""")
    abstract fun getSubscribed(): List<MpTwitterModel>

    @Query(
""" SELECT EXISTS
        (SELECT * FROM mp_twitter_data 
        WHERE mpId IS (:mpId) AND twitterId IS NOT NULL)""")
    abstract suspend fun mpHasTwitter(mpId: Int): Boolean
}

/**
 * Data-access object class for Twitter feed subscriptions.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
@Dao
abstract class TwitterFeedDao : GenericDao<TwitterFeedModel>() {
    @Query("SELECT COUNT(*) FROM twitter_feed ")
    abstract fun twitterFeedSize(): LiveData<Int>

    @Query("SELECT * FROM twitter_feed")
    abstract fun select(): LiveData<TwitterFeedModel>

    @Query("SELECT EXISTS(SELECT * FROM twitter_feed WHERE mpId is (:id))")
    abstract fun existsById(id: Int): LiveData<Boolean>
}