package com.example.mpinspector.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.mpinspector.repository.models.MpModel

@Dao
abstract class MpDao : GenericDao<MpModel>() {
    @Query("SELECT picture FROM mp WHERE mp.personNumber IS (:id)")
    abstract suspend fun selectPicture(id: Int): String

    @Query("SELECT * FROM mp WHERE mp.personNumber IS (:id)")
    abstract fun selectById(id: Int): LiveData<MpModel>

    @Query("SELECT * FROM mp")
    abstract fun selectAll(): LiveData<List<MpModel>>
}

