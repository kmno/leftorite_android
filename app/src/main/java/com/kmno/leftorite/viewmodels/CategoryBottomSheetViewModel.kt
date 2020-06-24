/*
 * Creator: Kamran Noorinejad on 6/9/20 3:37 PM
 * Last modified: 6/9/20 3:37 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.ApiClientProvider
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.model.Category
import com.kmno.leftorite.data.repository.DbRepository
import com.kmno.leftorite.utils.UserInfo
import kotlinx.coroutines.Dispatchers

/**
 * Created by Kamran Noorinejad on 6/9/2020 AD 15:37.
 * Edited by Kamran Noorinejad on 6/9/2020 AD 15:37.
 */
class CategoryBottomSheetViewModel(
    private val context: Context,
    apiProvider: ApiClientProvider,
    private val dbRepository: DbRepository
) :
    ViewModel() {

    private val api = apiProvider.createApiClient()


    fun insertCategories(categoriesList: List<Category>) =
        dbRepository.insertCategory(categoriesList)


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

    fun selectAllCategories() = liveData(Dispatchers.IO) {
        try {
            val response = dbRepository.getCategoriesList()
            emit(
                response
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}