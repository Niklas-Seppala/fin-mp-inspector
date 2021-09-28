package com.example.mpinspector.repository.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "mp_comments",
    indices = [Index(value = ["id"], unique = true)],
    foreignKeys = [ForeignKey(
        entity = MpModel::class,
        parentColumns = ["personNumber"],
        childColumns = ["mpId"],
        onDelete = ForeignKey.CASCADE)])
data class CommentModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mpId: Int,
    val content: String,
    val timestamp: Long
)