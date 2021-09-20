package com.example.mpinspector.repository.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Network {
    private var retrofitMps: Retrofit? = null
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

    private var retrofitImages: Retrofit? = null
    val imageClient: Retrofit
        get() {
            if (retrofitImages == null) {
                retrofitImages = Retrofit.Builder()
                    .baseUrl("https://avoindata.eduskunta.fi/")
                    .build()
            }
            return  retrofitImages as Retrofit
        }
}