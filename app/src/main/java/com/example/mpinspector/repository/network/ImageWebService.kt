package com.example.mpinspector.repository.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface ImageWebService {
    @GET("{pic}")
    suspend fun getImage(@Path("pic")  picture: String): ResponseBody
}