/*
 * Creator: Kamran Noorinejad on 5/19/20 12:11 PM
 * Last modified: 5/19/20 12:11 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.api

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.kmno.leftorite.data.model.*
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Kamran Noorinejad on 5/19/2020 AD 12:11.
 * Edited by Kamran Noorinejad on 5/19/2020 AD 12:11.
 */
interface ApiService {

    //Authentication
    @POST("/api/v1/login")
    suspend fun signInUser(
        @Query("email", encoded = true) email: String,
        @Query("password", encoded = true) password: String
    ): Response<ServiceResponse<ApiResponse<User>>>

    @POST("/api/v1/register")
    suspend fun signUpUser(
        @Query("email", encoded = true) email: String,
        @Query("password", encoded = true) password: String,
        @Query("device_os", encoded = true) device_os: String,
        @Query("device_model", encoded = true) device_model: String,
        @Query("ip", encoded = true) ip: String,
        @Query("apiKey", encoded = true) apiKey: String
    ): Response<ServiceResponse<ApiResponse<User>>>

    //Categories
    @POST("/api/v1/getCategories")
    suspend fun getCategories(
        @Query("id", encoded = true) id: Int,
        @Query("token", encoded = true) token: String
    ): Response<ServiceResponse<ApiResponse<List<Category>>>>

    //Items
/*    @POST("/api/v1/getItems")
    suspend fun getAllItems(
        @Query("id", encoded = true) id: Int,
        @Query("token", encoded = true) token: String,
        @Query("limit", encoded = true) limit: Int,
        @Query("offset", encoded = true) offset: Int
    ): Response<ServiceResponse<ApiResponse<List<Pair>>>>*/

    @POST("/api/v1/getItemsByCategory")
    suspend fun getItemsByCategory(
        @Query("catId", encoded = true) catId: Int,
        @Query("id", encoded = true) id: Int,
        @Query("token", encoded = true) token: String,
        @Query("limit", encoded = true) limit: Int,
        @Query("offset", encoded = true) offset: Int
    ): Response<ServiceResponse<ApiResponse<List<Pair>>>>

    @POST("/api/v1/setSelectedItem")
    suspend fun setSelectedItem(
        @Query("itemId", encoded = true) itemId: Int,
        @Query("pairId", encoded = true) pairId: Int,
        @Query("id", encoded = true) id: Int,
        @Query("token", encoded = true) token: String
    ): Response<ServiceResponse<ApiResponse<Any>>>

    //History
    @POST("/api/v1/getHistoryByCategory")
    suspend fun getHistoryByCategory(
        @Query("id", encoded = true) id: Int,
        @Query("categoryId", encoded = true) categoryId: Int,
        @Query("token", encoded = true) token: String
    ): Response<ServiceResponse<ApiResponse<List<History>>>>

    //Config
    @POST("/api/v1/getConfig")
    suspend fun getConfig(): Response<ServiceResponse<ApiResponse<Config>>>
}

@Keep
data class ServiceResponse<T>(
    @SerializedName("status") val status: Boolean,
    @SerializedName("response") val response: T
)

@Keep
data class ApiResponse<T>(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: T
)