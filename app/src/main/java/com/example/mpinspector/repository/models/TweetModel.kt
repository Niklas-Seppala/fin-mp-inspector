package com.example.mpinspector.repository.models

import androidx.room.*
import com.squareup.moshi.Json
import java.time.Instant

@Entity(tableName = "tweet",
    indices = [Index("authorId", unique = false)],
    foreignKeys = [ForeignKey(
        entity = TwitterFeedModel::class,
        parentColumns = ["mpId"],
        childColumns = ["authorId"],
        onDelete = ForeignKey.CASCADE
    )])
data class TweetModel(
    @PrimaryKey
    val id: String,

    @field:Json(name = "author_id")
    val author: String,

    @field:Json(name = "created_at")
    var createdAt: String,

    @field:Json(name = "text")
    val content: String,

    var isRead: Boolean = false,
    var authorId: Int,
    var authorName: String,
    var authorParty: String,
    var timestamp: Long = 0,
    var username: String
) {
    init {
        timestamp = Instant.parse(createdAt).epochSecond
    }

    fun attachOwner(owner: MpTwitterModel) {
        authorId = owner.personNumber
        authorName = "${owner.first} ${owner.last}"
        authorParty = owner.party
        username = owner.username ?: return
    }
}

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
    val data: List<TweetModel>?,
    val meta: TweetApiQueryMeta?,
)
