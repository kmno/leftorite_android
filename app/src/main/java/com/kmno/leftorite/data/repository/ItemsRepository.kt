/*
 * Creator: Kamran Noorinejad on 11/30/20 11:44 AM
 * Last modified: 11/30/20 11:44 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.repository

import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.model.Pair
import com.kmno.leftorite.data.repository.base.BaseRepository
import com.kmno.leftorite.utils.AppSetting
import com.kmno.leftorite.utils.UserInfo

/**
 * Created by Kamran Noorinejad on 11/30/2020 AD 11:44.
 * Edited by Kamran Noorinejad on 11/30/2020 AD 11:44.
 */
class ItemsRepository : BaseRepository() {

    suspend fun setSelectedItem(itemId: Int, pairId: Int): Resource<Any> {
        return try {
            Resource.loadingRepo()
            val response = api.setSelectedItem(itemId, pairId, UserInfo.id, UserInfo.token)
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

    suspend fun getItemsByCategory(categoryId: Int, offset: Int): Resource<List<Pair>> {
        return try {
            Resource.loadingRepo()
            val response = api.getItemsByCategory(
                categoryId, UserInfo.id, UserInfo.token,
                AppSetting.itemsPerRequestLimit, offset
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
            Resource.error(
                false,
                appCtx.getString(R.string.network_error_text),
                null
            )
        }
    }

}