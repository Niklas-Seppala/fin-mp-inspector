package com.example.mpinspector.repository.models

/**
 * MPs data combined with Twitter profile data.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
data class MpTwitterModel(
    val personNumber: Int,
    val first: String,
    val last: String,
    val party: String,
    val minister: Boolean,
    val twitterId: String?,
    val username: String?,
    val newestId: String?
)