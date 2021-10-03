package com.example.mpinspector.repository.models

import com.squareup.moshi.Json

data class TweetModel(
    val id: String,
    @field:Json(name = "author_id")
    val author: String,
    @field:Json(name = "created_at")
    val createdAt: String,
    @field:Json(name = "text")
    val content: String
)

data class TweetApiQueryMeta(
    @field:Json(name = "oldest_id")
    val oldestId: String,

    @field:Json(name = "newest_id")
    val newestId: String,

    @field:Json(name = "result_count")
    val count: Int,

    @field:Json(name = "next_token")
    val nextToken: String?,

    @field:Json(name = "previous_token")
    val previousToken: String?,
)

data class TweetApiQueryResult(
    val data: List<TweetModel>,
    val meta: TweetApiQueryMeta,
)
