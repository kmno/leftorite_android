/*
 * Creator: Kamran Noorinejad on 5/19/20 12:06 PM
 * Last modified: 5/19/20 12:06 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Kamran Noorinejad on 5/19/2020 AD 12:06.
 * Edited by Kamran Noorinejad on 5/19/2020 AD 12:06.
 */
data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("points")
    val points: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("last_login_date")
    val last_login_date: Long
)