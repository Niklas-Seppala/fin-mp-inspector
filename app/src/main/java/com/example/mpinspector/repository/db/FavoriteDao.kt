package com.example.mpinspector.repository.db

import androidx.room.Dao
import androidx.room.Query
import com.example.mpinspector.repository.models.FavoriteModel

@Dao
abstract class FavoriteDao : GenericDao<FavoriteModel>() {
    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE mpId is (:id))")
    abstract suspend fun existsById(id: Int) : Boolean
}