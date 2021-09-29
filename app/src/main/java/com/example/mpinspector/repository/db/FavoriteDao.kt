package com.example.mpinspector.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.mpinspector.repository.models.FavoriteModel

@Dao
abstract class FavoriteDao : GenericDao<FavoriteModel>() {
    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE mpId is (:id))")
    abstract suspend fun existsById(id: Int) : Boolean

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE mpId is (:id))")
    abstract fun existsById_LIVE_DATA(id: Int): LiveData<Boolean>
}