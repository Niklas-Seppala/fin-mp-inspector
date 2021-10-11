package com.example.mpinspector.repository.network

import com.example.mpinspector.repository.models.TweetApiQueryResult
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface for fetching Tweets.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
interface TwitterService {

    /**
     * Get Twitter user's latest tweets.
     *
     * API docs:
     *  https://developer.twitter.com/en/docs/twitter-api/tweets/timelines/api-reference/get-users-id-tweets
     *
     * @param id String Twitter user's id.
     * @param fields String Which Tweet fields are included. See API docs.
     * @param count String Max Tweet count in response.
     * @param exclude String Which Tweet types will get excluded. See API docs.
     * @param header String Bearer Token
     *
     * @return TweetApiQueryResult Deserialized response object containing data and meta fields.
     */
    @GET("users/{id}/tweets")
    suspend fun getTweets(@Path("id")id: String,
                          @Query("tweet.fields")fields: String,
                          @Query("max_results")count: String,
                          @Query("exclude")exclude: String="retweets,replies",
                          @Header("Authorization")header: String): TweetApiQueryResult

    /**
     * Get Twitter user's latest tweets, starting from "sinceId" parameter.
     *
     * API docs:
     *  https://developer.twitter.com/en/docs/twitter-api/tweets/timelines/api-reference/get-users-id-tweets
     *
     * @param id String Twitter user's id.
     * @param fields String Which Tweet fields are included. See API docs.
     * @param count String Max Tweet count in response.
     * @param sinceId String Start the query from this tweet forwards.
     * @param exclude String Which Tweet types will get excluded. See API docs.
     * @param header String Bearer Token
     *
     * @return TweetApiQueryResult Deserialized response object containing data and meta fields.
     */
    @GET("users/{id}/tweets")
    suspend fun getTweets(@Path("id")id: String,
                          @Query("tweet.fields")fields: String,
                          @Query("max_results")count: String,
                          @Query("since_id")sinceId: String,
                          @Query("exclude")exclude: String="retweets,replies",
                          @Header("Authorization")header: String): TweetApiQueryResult
}
