/*
 * Creator: Kamran Noorinejad on 5/17/20 10:47 AM
 * Last modified: 5/17/20 10:47 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kmno.leftorite.data.api.ApiClientProvider
import com.kmno.leftorite.data.api.Resource
import kotlinx.coroutines.Dispatchers

/**
 * Created by Kamran Noorinejad on 5/17/2020 AD 10:47.
 * Edited by Kamran Noorinejad on 5/17/2020 AD 10:47.
 */
class AuthActivityViewModel(apiProvider: ApiClientProvider) : ViewModel() {

    private val api = apiProvider.createApiClient()

    fun loadData() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.loginUser(
                "kamran3@home.com",
                "123456789",
                "2y5jjvaVHPoKu43VrVAbQK2wDPErS4VzzqyM1fefQ41sD5iYZb9zmpnPPezoc4iWokTUBKEh4QKpQqeBrI9RdI"
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
                    "error loading data from network",
                    null
                )
            )
        }

    }


}