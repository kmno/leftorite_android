/*
 * Creator: Kamran Noorinejad on 11/30/20 11:44 AM
 * Last modified: 11/30/20 11:44 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.repository

import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.model.User
import com.kmno.leftorite.data.repository.base.BaseRepository

/**
 * Created by Kamran Noorinejad on 11/30/2020 AD 11:44.
 * Edited by Kamran Noorinejad on 11/30/2020 AD 11:44.
 */
class AuthRepository : BaseRepository() {

    suspend fun signInUser(
        email: String,
        password: String
    ): Resource<User> {
        return try {
            Resource.loadingRepo()
            val response = api.signInUser(
                email,
                password
            )
            if (response.isSuccessful) {
                Resource.success(
                    response.body()?.status,
                    response.body()?.response?.message,
                    response.body()?.response?.data
                )
            } else {
                Resource.error(
                    response.body()?.status,
                    response.body()?.response?.message,
                    null
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.error(
                false,
                appCtx.getString(R.string.network_error_text),
                null
            )
        }
    }

    suspend fun signUpUser(
        email: String,
        password: String,
        osVersion: String,
        deviceName: String,
        localIpAddress: String,
        apiKey: String
    ): Resource<User> {
        return try {
            Resource.loadingRepo()
            val response = api.signUpUser(
                email,
                password,
                osVersion,
                deviceName,
                localIpAddress,
                apiKey
            )
            if (response.isSuccessful) {
                Resource.success(
                    response.body()?.status,
                    response.body()?.response?.message,
                    response.body()?.response?.data
                )
            } else {
                Resource.error(
                    response.body()?.status,
                    response.body()?.response?.message,
                    null
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.error(
                false,
                appCtx.getString(R.string.network_error_text),
                null
            )
        }
    }
}