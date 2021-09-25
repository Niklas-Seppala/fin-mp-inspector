package com.example.mpinspector.repository.network

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