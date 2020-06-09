/*
 * Creator: Kamran Noorinejad on 5/17/20 10:48 AM
 * Last modified: 5/17/20 10:48 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.ApiClientProvider
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.model.Item
import com.kmno.leftorite.utils.UserInfo
import kotlinx.coroutines.Dispatchers

/**
 * Created by Kamran Noorinejad on 5/17/2020 AD 10:48.
 * Edited by Kamran Noorinejad on 5/17/2020 AD 10:48.
 */
class HomeActivityViewModel(private val context: Context, apiProvider: ApiClientProvider) :
    ViewModel() {

    private val api = apiProvider.createApiClient()

    var items = emptyList<Item>()
    var pairs = emptyList<Any>()

    init {
    }

    //get all items list
    fun getAllItems() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.getAllItems(UserInfo.id, UserInfo.token)
            if (response.isSuccessful) {
                emit(
                    Resource.success(
                        response.body()?.status,
                        response.body()?.response?.message,
                        response.body()?.response?.data
                    )

                    // items = response.body().response.data.items
                    // pairs = response.body().response.data.finalPairs
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

    //get all categories list
    fun getCategories() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.getCategories(UserInfo.id, UserInfo.token)
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

    //get items list based on selected category
    fun getItemsByCategory(categoryId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.getItemsByCategory(categoryId, UserInfo.id, UserInfo.token)
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

    //set selected item
    fun setSelectedItem(itemId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.setSelectedItem(itemId, UserInfo.id, UserInfo.token)
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

    override fun onCleared() {
        super.onCleared()
    }
}