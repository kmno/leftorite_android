/*
 * Creator: Kamran Noorinejad on 8/19/20 4:49 PM
 * Last modified: 8/19/20 4:49 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

/**
 * Created by Kamran Noorinejad on 8/19/2020 AD 16:49.
 * Edited by Kamran Noorinejad on 8/19/2020 AD 16:49.
 */
@Keep
data class Pair(
    @SerializedName("pair_id")
    @ColumnInfo(name = "pair_id")
    val pair_id: Int,

    @SerializedName("first_item_id")
    @ColumnInfo(name = "first_item_id")
    val first_item_id: Int,

    @SerializedName("first_item_title")
    @ColumnInfo(name = "first_item_title")
    val first_item_title: String,

    @SerializedName("first_item_description")
    @ColumnInfo(name = "first_item_description")
    val first_item_description: String,

    @SerializedName("second_item_id")
    @ColumnInfo(name = "second_item_id")
    val second_item_id: Int,

    @SerializedName("second_item_title")
    @ColumnInfo(name = "second_item_title")
    val second_item_title: String,

    @SerializedName("second_item_description")
    @ColumnInfo(name = "second_item_description")
    val second_item_description: String,

    @SerializedName("category_id")
    @ColumnInfo(name = "category_id")
    val category_id: Int
)