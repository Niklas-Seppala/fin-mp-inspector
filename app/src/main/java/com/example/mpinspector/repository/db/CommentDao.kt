package com.example.mpinspector.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mpinspector.repository.models.CommentModel

@Dao
interface CommentDao {
    @Query("SELECT * FROM mp_comments WHERE mpId IS (:id)")
    suspend fun getAllForMp(id: Int): MutableList<CommentModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comment: CommentModel)
}