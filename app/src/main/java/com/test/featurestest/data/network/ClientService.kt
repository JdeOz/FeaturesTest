package com.test.featurestest.data.network

import com.test.featurestest.core.network.RetrofitHelper
import com.test.featurestest.data.network.model.ClientApiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.withContext
import retrofit2.create

class ClientService {
    val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getClient(): ClientApiModel? {
        return (Dispatchers.IO){
            val response = retrofit.create(ClientApi::class.java).getClient()
            response.body()
        }
    }
}