/*
 * Creator: Kamran Noorinejad on 11/30/20 12:10 PM
 * Last modified: 11/30/20 12:10 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.repository.base

import android.app.Application
import com.kmno.leftorite.data.api.ApiService
import com.kmno.leftorite.data.db.LeftoriteDatabase
import com.kmno.leftorite.utils.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Kamran Noorinejad on 11/30/2020 AD 12:10.
 * Edited by Kamran Noorinejad on 11/30/2020 AD 12:10.
 */
open class BaseRepository : CoroutineScope, KoinComponent {

    val appCtx: Application by inject()
    val api: ApiService by inject()
    val isNetConnected: NetworkState by inject()
    val db = LeftoriteDatabase.getDatabase(appCtx)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

}