/*
 * Creator: Kamran Noorinejad on 5/19/20 12:58 PM
 * Last modified: 5/19/20 12:58 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.api

import com.google.gson.GsonBuilder
import com.kmno.leftorite.core.App
import com.kmno.leftorite.core.Constants
import com.kmno.leftorite.core.Constants.connectTimeout
import com.kmno.leftorite.core.Constants.readTimeout
import com.kmno.leftorite.core.Constants.writeTimeout
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Kamran Noorinejad on 5/19/2020 AD 12:58.
 * Edited by Kamran Noorinejad on 5/19/2020 AD 12:58.
 */
class ApiClientProvider {

    private val okHttpClient by lazy { OkHttpClient() }

    fun createApiClient(): ApiService {
        val retrofit: Retrofit by lazy {
            App.logger.warn("Creating Retrofit Client")
            val builder = Retrofit.Builder()
                .baseUrl(Constants.baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            val dispatcher = Dispatcher()
            dispatcher.maxRequests = 1
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val client: OkHttpClient = okHttpClient.newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .dispatcher(dispatcher)
                .build()
            builder.client(client).build()
        }
        return retrofit.create(ApiService::class.java)
    }
}