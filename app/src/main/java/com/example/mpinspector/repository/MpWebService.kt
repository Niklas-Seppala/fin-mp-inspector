package com.example.mpinspector.repository

import retrofit2.http.GET

interface MpWebService {
    @GET("~peterh/mps.json")
    suspend fun getMps(): List<MpModel>
}