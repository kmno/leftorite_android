/*
 * Creator: Kamran Noorinejad on 9/23/20 12:17 PM
 * Last modified: 9/23/20 12:17 PM
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
import com.kmno.leftorite.data.repository.DbRepository
import com.kmno.leftorite.utils.UserInfo
import kotlinx.coroutines.Dispatchers

/**
 * Created by Kamran Noorinejad on 9/23/2020 AD 12:17.
 * Edited by Kamran Noorinejad on 9/23/2020 AD 12:17.
 */
class ProfileActivityViewModel(
    private val context: Context,
    apiProvider: ApiClientProvider,
    private val dbRepository: DbRepository
) :
    ViewModel() {

    private val api = apiProvider.createApiClient()

    fun getHistoryByCategoryId(categoryId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.getHistoryByCategory(
                UserInfo.id, categoryId, UserInfo.token
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
            emit(
                Resource.error(
                    false,
                    context.getString(R.string.network_error_text),
                    null
                )
            )
        }

    }

}