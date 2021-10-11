package com.example.mpinspector.repository.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Retrofit instance singletons.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
object Network {
    private var retrofitMps: Retrofit? = null
    // MPs
    val mpClient: Retrofit
        get() {
            if (retrofitMps == null) {
                retrofitMps = Retrofit.Builder()
                    .baseUrl("https://users.metropolia.fi/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            }
            return retrofitMps as Retrofit
        }

    // Images
    private var retrofitImages: Retrofit? = null
    val imageClient: Retrofit
        get() {
            if (retrofitImages == null) {
                retrofitImages = Retrofit.Builder()
                    .baseUrl("https://avoindata.eduskunta.fi/")
                    .build()
            }
            return retrofitImages as Retrofit
        }

    // Twitter
    private val retrofitTwitter = Retrofit.Builder()
        .baseUrl("https://api.twitter.com/2/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val twitterClient: Retrofit = retrofitTwitter
}