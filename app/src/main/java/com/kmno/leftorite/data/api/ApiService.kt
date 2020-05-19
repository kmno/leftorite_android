/*
 * Creator: Kamran Noorinejad on 5/19/20 12:11 PM
 * Last modified: 5/19/20 12:11 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.api

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.kmno.leftorite.data.model.User
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Kamran Noorinejad on 5/19/2020 AD 12:11.
 * Edited by Kamran Noorinejad on 5/19/2020 AD 12:11.
 */
interface ApiService {

    @POST("/api/v1/login")
    suspend fun loginUser(
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("token") token: String
    ): Response<ServiceResponse<ApiResponse<User>>>

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