/*
 * Creator: Kamran Noorinejad on 9/23/20 3:49 PM
 * Last modified: 9/23/20 3:49 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.utils.extensions

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Kamran Noorinejad on 9/23/2020 AD 15:49.
 * Edited by Kamran Noorinejad on 9/23/2020 AD 15:49.
 */
fun String.toDate(format: String): Date? {
    val dateFormatter = SimpleDateFormat(format, Locale.US)
    return try {
        dateFormatter.parse(this)
    } catch (e: ParseException) {
        null
    }
}

fun Date.toString(format: String): String {
    val dateFormatter = SimpleDateFormat(format, Locale.US)
    return dateFormatter.format(this)
}

fun Int.toDate(): Date = Date(this.toLong() * 1000L)

fun Long.toDate(): Date = Date(this * 1000L)

val Int.asDate: Date
    get() = Date(this.toLong() * 1000L)

private const val TIME_STAMP_FORMAT = "EEEE, MMMM d, yyyy - hh:mm:ss a"
private const val DATE_FORMAT = "yyyy-MM-dd"

fun Long.getTimeStamp(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(TIME_STAMP_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

fun Long.getYearMonthDay(): String {
    val date = Date(this)
    val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    simpleDateFormat.timeZone = TimeZone.getDefault()
    return simpleDateFormat.format(date)
}

@Throws(ParseException::class)
fun String.getDateUnixTime(): Long {
    try {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getDefault()
        return simpleDateFormat.parse(this)!!.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    throw ParseException("Please Enter a valid date", 0)
}