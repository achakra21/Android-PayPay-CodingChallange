package com.abhijit.paypay.data.remote.api

import com.abhijit.paypay.data.remote.model.Currencies
import retrofit2.Response

interface ApiHelper {
    //can with as non blocking
   suspend fun getCurrencyies(apiKey: String): Response<Currencies>
}


