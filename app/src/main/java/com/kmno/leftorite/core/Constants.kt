/*
 * Creator: Kamran Noorinejad on 6/15/20 3:54 PM
 * Last modified: 6/13/20 3:31 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.core

/**
 * Created by Kamran Noorinejad on 5/19/2020 AD 14:45.
 * Edited by Kamran Noorinejad on 5/19/2020 AD 14:45.
 */
object Constants {
    const val apiKey = "8c0892514e884f09af7c09a9b067b02b"
    const val baseUrl = "http://185.208.172.32:3000/"
    private const val assetsUrl = "http://185.208.172.32:3002/images/"
    const val itemsImageUrl = "${assetsUrl}item/"
    const val categoryImageUrl = "${assetsUrl}category/"
    const val userImageUrl = "${assetsUrl}user/"
    const val dbName = "db_leftorite"
    const val itemsPerRequestLimitDefault = 15
    const val itemsPerRequestLimitMin = 5
}