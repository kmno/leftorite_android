/*
 * Creator: Kamran Noorinejad on 5/13/20 4:12 PM
 * Last modified: 5/13/20 4:12 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.utils

import com.chibatching.kotpref.KotprefModel

/**
 * Created by Kamran Noorinejad on 5/13/2020 AD 16:12.
 * Edited by Kamran Noorinejad on 5/13/2020 AD 16:12.
 */

object UserInfo : KotprefModel() {
    var loggedIn by booleanPref(false)
    var id by intPref()
    var nickname by stringPref()
    var email by stringPref()
    var token by stringPref()
    var points by intPref(0)
    var avatar by stringPref()
}

object ShowCase : KotprefModel() {
    var welcomeDialogIsShown by ShowCase.booleanPref(false)
    var appTipIsShown by ShowCase.booleanPref(false)
}

object AppSetting : KotprefModel() {
    var dataSaver by AppSetting.booleanPref(false)
}