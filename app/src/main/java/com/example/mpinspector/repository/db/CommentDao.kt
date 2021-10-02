package com.example.mpinspector.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.mpinspector.repository.models.CommentModel

@Dao
abstract class CommentDao : GenericDao<CommentModel>() {
    @Query("SELECT * FROM mp_comments WHERE mpId IS (:id)")
    abstract fun selectForMpId(id: Int): LiveData<MutableList<CommentModel>>
}