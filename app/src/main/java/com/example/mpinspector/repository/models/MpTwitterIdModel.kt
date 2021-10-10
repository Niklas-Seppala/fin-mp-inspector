package com.example.mpinspector.repository.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

/**
 * Model class for MPs twitter data entries.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
@Entity(tableName = "mp_twitter_data",
    foreignKeys = [ForeignKey(
        entity = MpModel::class,
        parentColumns = ["personNumber"],
        childColumns = ["mpId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MpTwitterIdModel(
    @PrimaryKey
    @field:Json(name="personNumber")
    val mpId: Int,

    @field:Json(name="id")
    val twitterId: String?,

    val username: String?
)