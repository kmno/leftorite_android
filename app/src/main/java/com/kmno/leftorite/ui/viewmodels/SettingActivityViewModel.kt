/*
 * Creator: Kamran Noorinejad on 6/10/20 2:01 PM
 * Last modified: 6/10/20 2:01 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kmno.leftorite.BuildConfig

/**
 * Created by Kamran Noorinejad on 6/10/2020 AD 14:01.
 * Edited by Kamran Noorinejad on 6/10/2020 AD 14:01.
 */

class SettingActivityViewModel(private val context: Context) : ViewModel() {

    var appVersionText: String = BuildConfig.VERSION_NAME

    var dataSaverState = true

    init {
    }

    override fun onCleared() {
        super.onCleared()
    }

}