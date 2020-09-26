/*
 * Creator: Kamran Noorinejad on 9/23/20 3:38 PM
 * Last modified: 9/23/20 3:38 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.utils.extensions

/**
 * Created by Kamran Noorinejad on 9/23/2020 AD 15:38.
 * Edited by Kamran Noorinejad on 9/23/2020 AD 15:38.
 */

fun Any?.isNull() = this == null

fun Any?.isNotNull() = !isNull()

val String.containsLatinLetter: Boolean
    get() = matches(Regex(".*[A-Za-z].*"))

val String.containsDigit: Boolean
    get() = matches(Regex(".*[0-9].*"))

val String.isAlphanumeric: Boolean
    get() = matches(Regex("[A-Za-z0-9]*"))

val String.hasLettersAndDigits: Boolean
    get() = containsLatinLetter && containsDigit

val String.isIntegerNumber: Boolean
    get() = toIntOrNull() != null

val String.toDecimalNumber: Boolean
    get() = toDoubleOrNull() != null