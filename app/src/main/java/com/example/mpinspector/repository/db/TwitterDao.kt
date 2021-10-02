package com.example.mpinspector.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.mpinspector.repository.models.MpModel
import com.example.mpinspector.repository.models.TwitterFeedModel

@Dao
abstract class TwitterDao : GenericDao<TwitterFeedModel>() {

    @Query("SELECT * FROM twitter_feed")
    abstract fun select(): LiveData<TwitterFeedModel>

    @Query("SELECT EXISTS(SELECT * FROM twitter_feed WHERE mpId is (:id))")
    abstract fun existsById(id: Int): LiveData<Boolean>
}