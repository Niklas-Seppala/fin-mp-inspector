package com.example.mpinspector.repository.models

data class MpTwitterModel(
    val personNumber: Int,
    val first: String,
    val last: String,
    val party: String,
    val minister: Boolean,
    val twitterId: String?,
    val username: String?
)