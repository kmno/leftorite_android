/*
 * Creator: Kamran Noorinejad on 5/17/20 11:48 AM
 * Last modified: 5/17/20 11:48 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.kmno.leftorite.R

/**
 * Created by Kamran Noorinejad on 5/17/2020 AD 11:48.
 * Edited by Kamran Noorinejad on 5/17/2020 AD 11:48.
 */

/**
 * Extensions for simpler launching of Activities
 */

inline fun <reified T : Any> Activity.launchActivity(
    options: Bundle? = null,
    finish: Boolean = false,
    finishAffinity: Boolean = false,
    noinline init: Intent.() -> Unit = {}
) {

    val intent = newIntent<T>(this)
    intent.init()

    if (finish) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        finish()
    }

    if (finishAffinity) {
        finishAffinity()
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }

    overridePendingTransition(
        R.anim.slide_in_right,
        R.anim.slide_out_left
    )
}

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {

    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)