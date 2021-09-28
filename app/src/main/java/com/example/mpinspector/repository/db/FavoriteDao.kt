package com.example.mpinspector.repository.db

import androidx.room.Dao
import androidx.room.Query
import com.example.mpinspector.repository.models.FavoriteModel

@Dao
abstract class FavoriteDao : BaseDao<FavoriteModel>() {
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE mpId is (:id))")
    abstract suspend fun existsById(id: Int) : Boolean
}