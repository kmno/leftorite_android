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

    var latestMsgId by intPref(0)

    var lastSelectedCategoryIndex by intPref(-1)//-1 is All
    var lastSelectedCategoryId by intPref(-1)//-1 is All
    var lastSelectedCategoryTitle by stringPref("All")
}

object ShowCase : KotprefModel() {
    var welcomeDialogIsShown by ShowCase.booleanPref(false)
    var appTipIsShown by ShowCase.booleanPref(false)
}

object AppSetting : KotprefModel() {
    var dataSaver by AppSetting.booleanPref(false)
}

object ConfigPref : KotprefModel() {
    var top_text by stringPref()
    var bottom_text by stringPref()
    var new_msg_id by intPref(0)
    var new_msg_content by stringPref()
    var new_msg_title by stringPref()
    var new_version_title by stringPref()
    var force_update by intPref()
    var new_version_changelog by stringPref()
}