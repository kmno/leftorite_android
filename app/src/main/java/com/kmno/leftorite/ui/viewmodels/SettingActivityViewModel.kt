/*
 * Creator: Kamran Noorinejad on 6/10/20 2:01 PM
 * Last modified: 6/10/20 2:01 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.kmno.leftorite.data.db.LeftoriteDatabase
import com.kmno.leftorite.utils.AppSetting
import com.kmno.leftorite.utils.ConfigPref
import com.kmno.leftorite.utils.ShowCase
import com.kmno.leftorite.utils.UserInfo

/**
 * Created by Kamran Noorinejad on 6/10/2020 AD 14:01.
 * Edited by Kamran Noorinejad on 6/10/2020 AD 14:01.
 */

class SettingActivityViewModel : ViewModel() {

    var dataSaver = AppSetting.dataSaver

    fun signOutUser() {
        UserInfo.clear()
        ShowCase.clear()
        AppSetting.clear()
        ConfigPref.clear()
        LeftoriteDatabase.clearTables()
    }

    fun setDataSaverPref(_state: Boolean) {
        AppSetting.dataSaver = _state
        if (_state)
            AppSetting.itemsPerRequestLimit = ConfigPref.itemsPerRequestLimitMin
        else
            AppSetting.itemsPerRequestLimit = ConfigPref.itemsPerRequestLimitDefault
    }

}