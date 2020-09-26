/*
 * Creator: Kamran Noorinejad on 9/23/20 3:36 PM
 * Last modified: 9/23/20 3:36 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.utils.extensions

import java.util.regex.Pattern

/**
 * Created by Kamran Noorinejad on 9/23/2020 AD 15:36.
 * Edited by Kamran Noorinejad on 9/23/2020 AD 15:36.
 */

fun String.isEmailValid(): Boolean {
    val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,8}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}