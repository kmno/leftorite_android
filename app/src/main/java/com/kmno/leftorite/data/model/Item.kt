/*
 * Creator: Kamran Noorinejad on 6/2/20 9:34 AM
 * Last modified: 6/2/20 9:34 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kmno.leftorite.core.Constants

/**
 * Created by Kamran Noorinejad on 6/2/2020 AD 09:34.
 * Edited by Kamran Noorinejad on 6/2/2020 AD 09:34.
 */
@Keep
@Entity(tableName = "${Constants.dbName}_items")
data class Item(
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String,
    @SerializedName("popularity")
    @ColumnInfo(name = "popularity")
    val popularity: Int,
    @SerializedName("category_id")
    @ColumnInfo(name = "category_id")
    val category_id: Int,
    @SerializedName("description")
    @ColumnInfo(name = "description")
    val description: String
)