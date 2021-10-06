package com.example.mpinspector.repository.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "mp")
data class MpModel (
    @PrimaryKey
    val personNumber: Int,
    val seatNumber: Int,
    val first: String,
    val last: String,
    val party: String,
    val minister: Boolean,
    val picture: String,
    val twitter: String = "",
    val bornYear: Int,
    val constituency: String,
)

data class MpWithComments(
    @Embedded
    val mp: MpModel,
    @Relation(parentColumn = "personNumber", entityColumn = "mpId",)
    val comments: List<CommentModel>
)