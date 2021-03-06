/*
 * Creator: Kamran Noorinejad on 6/10/20 2:01 PM
 * Last modified: 6/10/20 2:01 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kmno.leftorite.BuildConfig
import com.kmno.leftorite.core.Constants
import com.kmno.leftorite.utils.AppSetting
import com.kmno.leftorite.utils.ShowCase
import com.kmno.leftorite.utils.UserInfo

/**
 * Created by Kamran Noorinejad on 6/10/2020 AD 14:01.
 * Edited by Kamran Noorinejad on 6/10/2020 AD 14:01.
 */

class SettingActivityViewModel(private val context: Context) : ViewModel() {

    var appVersionText: String = BuildConfig.VERSION_NAME

    var dataSaver = AppSetting.dataSaver

    init {
    }

    fun signOutUser() {
        UserInfo.clear()
        ShowCase.clear()
        AppSetting.clear()
    }

    fun setDataSaverPref(_state: Boolean) {
        AppSetting.dataSaver = _state
        if (_state) AppSetting.itemsPerRequestLimit = Constants.itemsPerRequestLimitMin
        else AppSetting.itemsPerRequestLimit = Constants.itemsPerRequestLimitDefault

    }

}