package com.aarafrao.test.network

import com.aarafrao.test.model.ItemModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("smart")
    suspend fun getProducts(): ItemModel
}

object ServiceBuilder {
    private const val BASE_URL = "https://fakestoreapiserver.reactbd.com"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}