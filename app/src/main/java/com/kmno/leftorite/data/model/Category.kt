/*
 * Creator: Kamran Noorinejad on 6/2/20 9:33 AM
 * Last modified: 6/2/20 9:33 AM
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
 * Created by Kamran Noorinejad on 6/2/2020 AD 09:33.
 * Edited by Kamran Noorinejad on 6/2/2020 AD 09:33.
 */
@Keep
@Entity(tableName = "${Constants.dbName}_categories")
data class Category(
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int,
    @SerializedName("title")
    @ColumnInfo(name = "title")
    var title: String,
    @SerializedName("is_active")
    @ColumnInfo(name = "is_active")
    var is_active: Int,
    @SerializedName("is_new")
    @ColumnInfo(name = "is_new")
    var is_new: Int/*,
    @SerializedName("question_text")
    @ColumnInfo(name = "question_text")
    var question_text: String*/
)