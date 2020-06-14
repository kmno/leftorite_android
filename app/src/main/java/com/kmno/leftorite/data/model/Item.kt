/*
 * Creator: Kamran Noorinejad on 6/2/20 9:34 AM
 * Last modified: 6/2/20 9:34 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Kamran Noorinejad on 6/2/2020 AD 09:34.
 * Edited by Kamran Noorinejad on 6/2/2020 AD 09:34.
 */
data class Item(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("popularity")
    val popularity: Int,
    @SerializedName("category_id")
    val category_id: Int,
    @SerializedName("description")
    val description: String
)