package com.example.mpinspector.repository.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit interface for fetching MP image.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
interface ImageWebService {

    /**
     * @param picture String Profile picture endpoint.
     * @return ResponseBody Raw Okhttp3 response object.
     */
    @GET("{pic}")
    suspend fun getImage(@Path("pic")  picture: String): ResponseBody
}