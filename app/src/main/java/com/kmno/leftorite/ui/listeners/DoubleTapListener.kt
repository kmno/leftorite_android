/*
 * Creator: Kamran Noorinejad on 6/13/20 4:04 PM
 * Last modified: 6/13/20 3:53 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.listeners

import android.view.View

/**
 * Created by Kamran Noorinejad on 6/2/2020 AD 14:21.
 * Edited by Kamran Noorinejad on 6/2/2020 AD 14:21.
 */
class DoubleTapListener(
    private val doubleClickTimeLimitMills: Long = 700,
    private val callback: Callback
) :
    View.OnClickListener {
    private var lastClicked: Long = -1L

    override fun onClick(v: View?) {
        lastClicked = when {
            lastClicked == -1L -> {
                System.currentTimeMillis()
            }
            isDoubleClicked() -> {
                callback.doubleClicked()
                -1L
            }
            else -> {
                System.currentTimeMillis()
            }
        }
    }

    private fun getTimeDiff(from: Long, to: Long): Long {
        return to - from
    }

    private fun isDoubleClicked(): Boolean {
        return getTimeDiff(
            lastClicked,
            System.currentTimeMillis()
        ) <= doubleClickTimeLimitMills
    }

    interface Callback {
        fun singleClicked()
        fun doubleClicked()
    }
}