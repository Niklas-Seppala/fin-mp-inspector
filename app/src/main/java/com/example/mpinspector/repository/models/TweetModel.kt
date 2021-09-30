package com.example.mpinspector.repository.models

data class TweetModel (
    val id: String,
    val text: String,
    val created_at: String,
    val author_id: String
)