package com.example.mpinspector.repository.network

import com.example.mpinspector.repository.models.TweetApiQueryResult
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TwitterService {

    @GET("users/{id}/tweets")
    suspend fun getTweets(@Path("id")id: String,
                          @Query("tweet.fields")fields: String,
                          @Query("max_results")count: String,
//                          @Query("pagination_token")pagination_token: String="",
                          @Query("exclude")exclude: String="retweets,replies",
                          @Header("Authorization")header: String): TweetApiQueryResult
}