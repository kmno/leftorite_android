/*
 * Creator: Kamran Noorinejad on 5/13/20 12:55 PM
 * Last modified: 5/13/20 12:55 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.chibatching.kotpref.blockingBulk
import com.kmno.leftorite.BuildConfig
import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.ApiClientProvider
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.model.Config
import com.kmno.leftorite.utils.ConfigPref
import com.kmno.leftorite.utils.UserInfo
import kotlinx.coroutines.Dispatchers

/**
 * Created by Kamran Noorinejad on 5/13/2020 AD 12:55.
 * Edited by Kamran Noorinejad on 5/13/2020 AD 12:55.
 */
class SplashActivityViewModel(
    private val context: Context,
    apiProvider: ApiClientProvider
) : ViewModel() {

    private val api = apiProvider.createApiClient()

    var appVersionText: String = "v ${BuildConfig.VERSION_NAME}\nⒸ 2020"
    var isUserLoggedIn: Boolean

    init {
        UserInfo.loggedIn.let {
            isUserLoggedIn = it
        }
    }

    fun checkIfNewVersionAvailable(): Boolean {
        if (ConfigPref.new_version_title != BuildConfig.VERSION_NAME) return true
        return false
    }

    fun storeConfigPrefs(config: Config) {
        try {
            ConfigPref.blockingBulk {
                this.top_text = config.top_text
                this.bottom_text = config.bottom_text
                this.new_msg_id = config.new_msg_id
                this.new_msg_title = config.new_msg_title
                this.new_msg_content = config.new_msg_content
                this.new_version_title = config.new_version_title
                this.force_update = config.force_update
                this.new_version_changelog = config.new_version_changelog
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getInitialConfig() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.getConfig()
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
}