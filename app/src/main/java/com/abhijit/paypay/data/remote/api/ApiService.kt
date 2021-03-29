package com.abhijit.paypay.data.remote.api

import com.abhijit.paypay.data.remote.model.Currencies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/live")
    suspend fun getCurrencies(@Query("access_key", encoded = true) apiKey: String): Response<Currencies>
}