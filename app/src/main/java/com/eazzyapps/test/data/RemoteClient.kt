package com.eazzyapps.test.data

import com.eazzyapps.test.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RemoteClient {

    private const val baseUrl = "https://api.github.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val headersInterceptor = Interceptor { chain ->
        chain.request().let { request ->
            request.newBuilder()
                .addHeader("Authorization", "token ${BuildConfig.TOKEN}")
                .method(request.method, request.body)
        }.build().run { chain.proceed(this) }
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(headersInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(httpClient)
        .build()

    fun <T> create(clazz: Class<T>): T = retrofit.create(clazz)
}