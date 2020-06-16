/*
 * Creator: Kamran Noorinejad on 6/15/20 1:53 PM
 * Last modified: 6/15/20 1:53 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.db.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by Kamran Noorinejad on 6/15/2020 AD 13:53.
 * Edited by Kamran Noorinejad on 6/15/2020 AD 13:53.
 */
@Entity(tableName = "db_leftorite_categories")
data class Category(
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int,

    @SerializedName("title")
    @ColumnInfo(name = "title")
    var title: String
)