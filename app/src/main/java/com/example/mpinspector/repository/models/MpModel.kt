package com.example.mpinspector.repository.models

import androidx.room.Entity
import androidx.room.PrimaryKey

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