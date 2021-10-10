package com.example.mpinspector.repository.network

import com.example.mpinspector.repository.models.MpModel
import com.example.mpinspector.repository.models.MpTwitterIdModel
import retrofit2.http.GET

/**
 * Retrofit interface for fetching MP related data.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
interface MpWebService {
    @GET("~peterh/mps.json")
    suspend fun getMps(): List<MpModel>

    @GET("~niklasts/mps/mp-twitters.json")
    suspend fun getMpTwitterIds(): List<MpTwitterIdModel>
}
