package com.example.mpinspector.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpinspector.repository.models.CommentModel


@Dao
abstract class CommentDao : BaseDao<CommentModel>() {
    @Query("SELECT * FROM mp_comments WHERE mpId IS (:id)")
    abstract fun selectForMpId(id: Int): LiveData<MutableList<CommentModel>>
}