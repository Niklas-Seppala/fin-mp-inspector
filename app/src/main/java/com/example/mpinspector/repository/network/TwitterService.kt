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

    @GET("users/{id}/tweets")
    suspend fun getTweets(@Path("id")id: String,
                          @Query("tweet.fields")fields: String,
                          @Query("max_results")count: String,
                          @Query("exclude")exclude: String="retweets,replies",
                          @Header("Authorization")header: String): TweetApiQueryResult

    @GET("users/{id}/tweets")
    suspend fun getTweets(@Path("id")id: String,
                          @Query("tweet.fields")fields: String,
                          @Query("max_results")count: String,
                          @Query("since_id")sinceId: String,
                          @Query("exclude")exclude: String="retweets,replies",
                          @Header("Authorization")header: String): TweetApiQueryResult
}