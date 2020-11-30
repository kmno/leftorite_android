/*
 * Creator: Kamran Noorinejad on 11/30/20 12:09 PM
 * Last modified: 11/30/20 12:09 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.repository

import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.model.Config
import com.kmno.leftorite.data.repository.base.BaseRepository

/**
 * Created by Kamran Noorinejad on 11/30/2020 AD 12:09.
 * Edited by Kamran Noorinejad on 11/30/2020 AD 12:09.
 */
class ConfigRepository : BaseRepository() {

    suspend fun getConfig(): Resource<Config> {
        return try {
            Resource.loadingRepo()
            val response = api.getConfig()
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