package com.test.featurestest.data.network

import com.test.featurestest.data.network.model.ClientApiModel
import retrofit2.Response
import retrofit2.http.GET

interface ClientApi {
    @GET("/v3/7592ac20-8e60-47a2-b479-a5bc9a3a621e")
    suspend fun getClient(): Response<ClientApiModel>
}