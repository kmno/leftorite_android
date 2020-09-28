/*
 * Creator: Kamran Noorinejad on 9/22/20 12:12 PM
 * Last modified: 9/22/20 12:12 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

/**
 * Created by Kamran Noorinejad on 9/22/2020 AD 12:12.
 * Edited by Kamran Noorinejad on 9/22/2020 AD 12:12.
 */
@Keep
class History(
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Int,

    @SerializedName("pair_id")
    @ColumnInfo(name = "pair_id")
    val pair_id: Int,

    @SerializedName("item_id_1")
    @ColumnInfo(name = "item_id_1")
    val item_id_1: Int,

    @SerializedName("item_id_2")
    @ColumnInfo(name = "item_id_2")
    val item_id_2: Int,

    @SerializedName("selected_item_id")
    @ColumnInfo(name = "selected_item_id")
    val selected_item_id: Int,

    @SerializedName("item_title")
    @ColumnInfo(name = "item_title")
    val item_title: String,

    @SerializedName("category_id")
    @ColumnInfo(name = "category_id")
    val category_id: Int,

    @SerializedName("popularity")
    @ColumnInfo(name = "popularity")
    val popularity: Int,

    @SerializedName("date_time")
    @ColumnInfo(name = "date_time")
    val date_time: String
)