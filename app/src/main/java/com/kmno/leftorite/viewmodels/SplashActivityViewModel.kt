/*
 * Creator: Kamran Noorinejad on 5/13/20 12:55 PM
 * Last modified: 5/13/20 12:55 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.viewmodels

import androidx.lifecycle.ViewModel
import com.kmno.leftorite.BuildConfig
import com.kmno.leftorite.core.App
import com.kmno.leftorite.utils.UserInfo

/**
 * Created by Kamran Noorinejad on 5/13/2020 AD 12:55.
 * Edited by Kamran Noorinejad on 5/13/2020 AD 12:55.
 */
class SplashActivityViewModel : ViewModel() {

    var appVersionText: String = "v ${BuildConfig.VERSION_NAME}\nⒸ 2020"
    var isUserLoggedIn: Boolean

    init {
        UserInfo.loggedIn.let {
            App.logger.error("Login.loggedIn.let $it")
            isUserLoggedIn = it
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

}