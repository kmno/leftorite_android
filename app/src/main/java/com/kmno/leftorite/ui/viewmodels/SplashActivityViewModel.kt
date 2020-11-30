/*
 * Creator: Kamran Noorinejad on 5/13/20 12:55 PM
 * Last modified: 5/13/20 12:55 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.chibatching.kotpref.blockingBulk
import com.kmno.leftorite.BuildConfig
import com.kmno.leftorite.data.model.Config
import com.kmno.leftorite.data.repository.ConfigRepository
import com.kmno.leftorite.utils.AppSetting
import com.kmno.leftorite.utils.ConfigPref
import com.kmno.leftorite.utils.UserInfo
import kotlinx.coroutines.Dispatchers
import java.util.*

/**
 * Created by Kamran Noorinejad on 5/13/2020 AD 12:55.
 * Edited by Kamran Noorinejad on 5/13/2020 AD 12:55.
 */
class SplashActivityViewModel(private val configRepository: ConfigRepository) : ViewModel() {

    var appVersionText: String =
        "v${BuildConfig.VERSION_NAME} Ⓒ ${Calendar.getInstance().get(Calendar.YEAR)}"
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
                this.default_cat_id = config.default_cat_id
                this.itemsPerRequestLimitDefault = config.itemsPerRequestLimitDefault
                this.itemsPerRequestLimitMin = config.itemsPerRequestLimitMin
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun checkApiRequestLimits() {
        if (AppSetting.itemsPerRequestLimit == 0)
            AppSetting.itemsPerRequestLimit = ConfigPref.itemsPerRequestLimitDefault
    }

    fun getInitialConfig() = liveData(Dispatchers.IO) {
        emit(configRepository.getConfig())
    }

}