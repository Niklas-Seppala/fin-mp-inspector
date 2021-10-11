package com.example.mpinspector.repository.network

/**
 * Twitter API query string utils.
 *
 * API docs:
 *  https://developer.twitter.com/en/docs/twitter-api/tweets/timelines/api-reference/get-users-id-tweets
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
object TwitterQueries {
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

    object TweetFields {
        const val CREATED_AT = "created_at"
        const val AUTHOR_ID = "author_id"
    }
}