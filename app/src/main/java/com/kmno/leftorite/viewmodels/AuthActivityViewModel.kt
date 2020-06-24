/*
 * Creator: Kamran Noorinejad on 5/17/20 10:47 AM
 * Last modified: 5/17/20 10:47 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.chibatching.kotpref.blockingBulk
import com.kmno.leftorite.R
import com.kmno.leftorite.core.Constants.apiKey
import com.kmno.leftorite.data.api.ApiClientProvider
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.model.User
import com.kmno.leftorite.utils.UserInfo
import com.kmno.leftorite.utils.UserInfo.loggedIn
import kotlinx.coroutines.Dispatchers

/**
 * Created by Kamran Noorinejad on 5/17/2020 AD 10:47.
 * Edited by Kamran Noorinejad on 5/17/2020 AD 10:47.
 */
class AuthActivityViewModel(private val context: Context, apiProvider: ApiClientProvider) :
    ViewModel() {

    private val api = apiProvider.createApiClient()

    init {

    }

    fun setLoggedInPref(_loggedIn: Boolean) {
        loggedIn = _loggedIn
    }

    //sign in
    fun signInUser(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.signInUser(email, password)
            if (response.isSuccessful) {
                emit(
                    Resource.success(
                        response.body()?.status,
                        response.body()?.response?.message,
                        response.body()?.response?.data
                    )
                )
            } else {
                emit(
                    Resource.error(
                        response.body()?.status,
                        response.body()?.response?.message,
                        null
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                Resource.error(
                    false,
                    context.getString(R.string.network_error_text),
                    null
                )
            )
        }

    }

    //signup
    fun signUpUser(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.signUpUser(
                email,
                password,
                apiKey
            )
            if (response.isSuccessful) {
                emit(
                    Resource.success(
                        response.body()?.status,
                        response.body()?.response?.message,
                        response.body()?.response?.data
                    )
                )
            } else {
                emit(
                    Resource.error(
                        response.body()?.status,
                        response.body()?.response?.message,
                        null
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                Resource.error(
                    false,
                    context.getString(R.string.network_error_text),
                    null
                )
            )
        }

    }

    fun storeUserPrefs(user: User) {
        UserInfo.blockingBulk {
            id = user.id
            nickname = user.nickname
            email = user.email
            points = user.points
            token = user.token
            avatar = "default_avatar_${(1..4).random()}"
        }
    }

}