// com/example/event_management_api/data/remote/RetrofitClient.kt
package com.example.event_management_api.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
private const val BASE_URL = "http://104.248.153.158/event-api/"

val instance: EventApiService by lazy {
    Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EventApiService::class.java)
}
}