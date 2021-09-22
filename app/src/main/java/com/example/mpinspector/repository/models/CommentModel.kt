package com.example.mpinspector.repository.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mp_comments")
data class CommentModel (
    @PrimaryKey
    val id: Int,
    val content: String,
    val timestamp: Int
)