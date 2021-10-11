package com.example.mpinspector.repository.network

import com.example.mpinspector.BuildConfig

/**
 * Twitter API utils.
 *
 * API docs:
 *  https://developer.twitter.com/en/docs/twitter-api/tweets/timelines/api-reference/get-users-id-tweets
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
object Twitter {
    fun join(queries: Array<String>): String {
        val sb = StringBuilder()
        var prefix = ""
        for (str in queries) {
            sb.append(prefix)
            prefix = ","
            sb.append(str)
        }
        return sb.toString()
    }

    object Auth {
        const val TWITTER_AUTH = "Bearer " + BuildConfig.TWITTER_AUTH
    }

    object TweetFields {
        const val CREATED_AT = "created_at"
        const val AUTHOR_ID = "author_id"
    }
}