package com.example.mpinspector.repository.network

import com.example.mpinspector.repository.models.MpModel
import retrofit2.http.GET

interface MpWebService {
    @GET("~peterh/mps.json")
    suspend fun getMps(): List<MpModel>
}