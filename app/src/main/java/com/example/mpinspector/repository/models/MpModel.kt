package com.example.mpinspector.repository.models

import androidx.room.*
import com.example.mpinspector.utils.MyTime

/**
 * Model class for database MP entries.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
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
    val age = MyTime.currentYear - bornYear
    @Ignore
    val ministerStr = if (minister) "Minister" else ""
}

/**
 * Relation class for Mp and it's comments.
 * 
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
data class MpWithComments(
    @Embedded
    val mp: MpModel,

    @Relation(parentColumn = "personNumber", entityColumn = "mpId",)
    val comments: List<CommentModel>
)