package com.example.mpinspector.repository.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "mp_comments",  foreignKeys = [ForeignKey(entity = MemberOfParliamentModel::class,
                                                              parentColumns = ["personNumber"],
                                                              childColumns = ["mpId"],
                                                              onDelete = ForeignKey.CASCADE)])
data class CommentModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val mpId: Int,
    val content: String,
    val timestamp: Long
)