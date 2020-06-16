/*
 * Creator: Kamran Noorinejad on 6/15/20 2:14 PM
 * Last modified: 6/15/20 2:14 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.repository

import android.app.Application
import android.content.Context
import com.kmno.leftorite.data.api.ApiClientProvider
import com.kmno.leftorite.data.db.LeftoriteDatabase
import com.kmno.leftorite.data.db.dao.CategoryDao
import com.kmno.leftorite.data.model.Category
import com.kmno.leftorite.utils.NetworkInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Created by Kamran Noorinejad on 6/15/2020 AD 14:14.
 * Edited by Kamran Noorinejad on 6/15/2020 AD 14:14.
 */

class DbRepository(
    context: Context,
    application: Application,
    apiProvider: ApiClientProvider,
    netInfo: NetworkInfo
) : CoroutineScope {

    private val api = apiProvider.createApiClient()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var categoryDao: CategoryDao?

    init {
        val db = LeftoriteDatabase.getDatabase(application)
        categoryDao = db?.categoryDao()

        //  netInfo.isOnline()
    }

    suspend fun getCategoriesList() = withContext(Dispatchers.IO) { categoryDao?.getCategories() }

    fun insertCategory(categories: List<Category>) {
        try {
            launch { insertCategoryInBackground(categories) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun insertCategoryInBackground(categories: List<Category>) {
        withContext(Dispatchers.IO) {
            categories.forEach { record ->
                categoryDao?.insertCategory(record)
            }
        }
    }

}