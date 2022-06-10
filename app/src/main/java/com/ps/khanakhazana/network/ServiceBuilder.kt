package com.ps.khanakhazana.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private const val URL  = "https://pradeeps050.github.io/khana_khazana/"

    private val logger : HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient : OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor(logger)

    private val retrofitBuilder : Retrofit.Builder = Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient.build())

    private val retrofit : Retrofit = retrofitBuilder.build()

    fun <T> buildService(service: Class<T>) : T {
        return retrofit.create(service)
    }
}