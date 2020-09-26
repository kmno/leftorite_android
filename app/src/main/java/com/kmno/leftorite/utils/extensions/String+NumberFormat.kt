/*
 * Creator: Kamran Noorinejad on 9/23/20 3:27 PM
 * Last modified: 9/23/20 3:27 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.utils.extensions

/**
 * Created by Kamran Noorinejad on 9/23/2020 AD 15:27.
 * Edited by Kamran Noorinejad on 9/23/2020 AD 15:27.
 */
import java.text.NumberFormat

val Int.commaString: String
    get() = NumberFormat.getInstance().format(this)

val String.commaString: String
    get() = NumberFormat.getNumberInstance().format(this.toDouble())

val Long.commaString: String
    get() = NumberFormat.getInstance().format(this)

val Double.commaString: String
    get() = NumberFormat.getInstance().format(this)