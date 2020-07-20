/*
 * Creator: Kamran Noorinejad on 7/15/20 7:01 PM
 * Last modified: 7/15/20 7:01 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Created by Kamran Noorinejad on 7/15/2020 AD 19:01.
 * Edited by Kamran Noorinejad on 7/15/2020 AD 19:01.
 */
@Keep
data class Config(
    @SerializedName("id")
    var id: Int,
    @SerializedName("top_text")
    var top_text: String,
    @SerializedName("bottom_text")
    var bottom_text: String,
    @SerializedName("new_msg_id")
    var new_msg_id: Int,
    @SerializedName("new_msg_title")
    var new_msg_title: String,
    @SerializedName("new_msg_content")
    var new_msg_content: String,
    @SerializedName("new_version_title")
    var new_version_title: String,
    @SerializedName("force_update")
    var force_update: Int,
    @SerializedName("new_version_changelog")
    var new_version_changelog: String
)