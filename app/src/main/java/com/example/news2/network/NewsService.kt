package com.example.news2.network


import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * A retrofit service to fetch a Source list
 */
interface NewsService {
    @GET("sources")
    suspend fun getSources(@Query("apiKey") apiKey: String): NetworkSourceContainer
}

object NewsNetwork {

    private const val BASE_URL = "https://newsapi.org/v2/"

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val network = retrofit.create(NewsService::class.java)
}