package com.example.mpinspector.repository

data class MpModel (
    val personNumber: Int,
    val seatNumber: Int,
    val first: String,
    val last: String,
    val party: String,
    val minister: Boolean,
    val picture: String,
    val twitter: String?,
    val bornYear: Int,
    val constituency: String
)