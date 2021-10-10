package com.example.mpinspector.repository.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Model class for database favorite entires.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
@Entity(tableName = "favorites", foreignKeys = [ForeignKey(
    entity = MpModel::class,
    parentColumns = ["personNumber"],
    childColumns = ["mpId"],
    onDelete = ForeignKey.CASCADE
)])
data class FavoriteModel(
    @PrimaryKey
    val mpId: Int,
    val timestamp: Long
)