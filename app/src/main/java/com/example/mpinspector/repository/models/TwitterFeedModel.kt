package com.example.mpinspector.repository.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Model class for database twitter feed subscription entry.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
@Entity(tableName = "twitter_feed",
    foreignKeys = [ForeignKey(
        entity = MpModel::class,
        parentColumns = ["personNumber"],
        childColumns = ["mpId"],
        onDelete = ForeignKey.CASCADE
    )])
data class TwitterFeedModel(
    @PrimaryKey
    val mpId: Int,
    val newestId: String?
)
