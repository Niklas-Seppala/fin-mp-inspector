package com.example.mpinspector.repository.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "twitter_feed",
    foreignKeys = [ForeignKey(
        entity = MpModel::class,
        parentColumns = ["personNumber"],
        childColumns = ["mpId"],
        onDelete = ForeignKey.CASCADE
    )])
data class TwitterFeedModel(
    @PrimaryKey
    val mpId: Int
)

