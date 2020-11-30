/*
 * Creator: Kamran Noorinejad on 11/30/20 4:10 PM
 * Last modified: 11/30/20 4:10 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.repository

import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.model.History
import com.kmno.leftorite.data.repository.base.BaseRepository

/**
 * Created by Kamran Noorinejad on 11/30/2020 AD 16:10.
 * Edited by Kamran Noorinejad on 11/30/2020 AD 16:10.
 */
class HistoryRepository : BaseRepository() {

    suspend fun getHistoryByCategory(
        userId: Int,
        categoryId: Int,
        token: String
    ): Resource<List<History>> {
        return try {
            Resource.loadingRepo()
            val response = api.getHistoryByCategory(userId, categoryId, token)
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
            Resource.error(
                false,
                appCtx.getString(R.string.network_error_text),
                null
            )
        }
    }
}