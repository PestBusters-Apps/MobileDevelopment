package com.example.pestbusters.retrofit

import de.hdodenhof.circleimageview.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private const val BASE_URL_ML = "https://pestbuster-ml-784490431660.asia-southeast2.run.app"
    private const val BASE_URL_TREATMENTS = "https://pestbuster-sql-784490431660.us-central1.run.app"

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY // For debug
        } else {
            HttpLoggingInterceptor.Level.NONE // For release
        }
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_ML)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val apiServiceTreatments: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_TREATMENTS)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
