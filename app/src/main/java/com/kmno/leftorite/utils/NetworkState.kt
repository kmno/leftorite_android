/*
 * Creator: Kamran Noorinejad on 11/30/20 12:13 PM
 * Last modified: 11/30/20 12:13 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.utils

import android.app.Application
import com.xoxoer.lifemarklibrary.Lifemark

/**
 * Created by Kamran Noorinejad on 11/30/2020 AD 12:13.
 * Edited by Kamran Noorinejad on 11/30/2020 AD 12:13.
 */
class NetworkState(applicationContext: Application) {
    private val networkConnection = Lifemark(applicationContext)
    operator fun invoke(): Boolean = networkConnection.isNetworkConnected()
}