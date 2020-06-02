/*
 * Creator: Kamran Noorinejad on 6/2/20 9:33 AM
 * Last modified: 6/2/20 9:33 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Kamran Noorinejad on 6/2/2020 AD 09:33.
 * Edited by Kamran Noorinejad on 6/2/2020 AD 09:33.
 */
data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String
)