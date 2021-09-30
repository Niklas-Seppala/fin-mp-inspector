package com.example.mpinspector.repository.network

import com.example.mpinspector.repository.api.TweetResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TwitterService {
    @GET("tweets/{id}")
    suspend fun getTweet(@Path("id")id: String,
                         @Query("tweet.fields")fields: String,
                         @Header("Authorization")header: String) : TweetResponse
}