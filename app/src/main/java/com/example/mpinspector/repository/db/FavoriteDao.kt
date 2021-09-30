package com.example.mpinspector.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.mpinspector.repository.models.FavoriteModel
import com.example.mpinspector.repository.models.MpModel

@Dao
abstract class FavoriteDao : GenericDao<FavoriteModel>() {
    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE mpId is (:id))")
    abstract fun existsById(id: Int): LiveData<Boolean>

    @Query("SELECT * FROM mp AS m JOIN favorites AS f ON m.personNumber IS f.mpId ORDER BY f.timestamp")
    abstract fun getAllSorted(): LiveData<List<MpModel>>
}