package com.example.mpinspector.repository.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Model class for comment database entries.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
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
    val like: Boolean,
    val timestamp: Long
)
