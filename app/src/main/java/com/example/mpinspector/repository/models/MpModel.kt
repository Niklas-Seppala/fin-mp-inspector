package com.example.mpinspector.repository.models

import androidx.room.*
import com.example.mpinspector.ui.mpinspect.MpViewModel
import com.example.mpinspector.utils.Year

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
) {
    @Ignore
    val fullName = "$first $last"
    @Ignore
    val age = Year.current - bornYear
    @Ignore
    val ministerStr = if (minister) "Minister" else ""
}

data class MpWithComments(
    @Embedded
    val mp: MpModel,

    @Relation(parentColumn = "personNumber", entityColumn = "mpId",)
    val comments: List<CommentModel>
)