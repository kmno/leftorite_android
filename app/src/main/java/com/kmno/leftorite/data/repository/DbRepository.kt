/*
 * Creator: Kamran Noorinejad on 6/15/20 2:14 PM
 * Last modified: 6/15/20 2:14 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.data.repository

import android.app.Application
import android.content.Context
import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.ApiClientProvider
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.db.LeftoriteDatabase
import com.kmno.leftorite.data.db.dao.CategoryDao
import com.kmno.leftorite.data.db.dao.ItemDao
import com.kmno.leftorite.data.model.Category
import com.kmno.leftorite.data.model.Item
import com.kmno.leftorite.utils.NetworkInfo
import com.kmno.leftorite.utils.UserInfo
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
    _context: Context,
    application: Application,
    apiProvider: ApiClientProvider,
    netInfo: NetworkInfo
) : CoroutineScope {

    private val context = _context
    private val api = apiProvider.createApiClient()
    private val networkState = netInfo

    private var status = true
    private var message = "OFFLINE"

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var categoryDao: CategoryDao?
    private var itemDao: ItemDao?

    init {
        val db = LeftoriteDatabase.getDatabase(application)
        categoryDao = db?.categoryDao()
        itemDao = db?.itemDao()
    }

    /* CATEGORIES */
    /**
     * check if network available
     * then get the latest list of categories and store them in db
     **/
    suspend fun getCategoriesList(): Resource<List<Category>>? {
        if (networkState.isOnline()) {
            try {
                val response = api.getCategories(UserInfo.id, UserInfo.token)
                if (response.isSuccessful) {
                    status = response.body()?.status ?: true
                    message = response.body()?.response?.message ?: ""
                    response.body()?.response?.data?.let {
                        refreshCategoriesOfflineCache(it)
                    }
                } else {
                    return Resource.error(
                        response.body()?.status,
                        response.body()?.response?.message,
                        null
                    )
                }
            } catch (e: Exception) {
                return Resource.error(
                    false,
                    context.getString(R.string.network_error_text),
                    null
                )
            }
        }
        return withContext(Dispatchers.IO) {
            Resource.success(
                status,
                message,
                categoryDao?.getCategories()
            )
        }
    }

    /**
     * Refresh the categories stored in the offline cache.
     **/
    private fun refreshCategoriesOfflineCache(categories: List<Category>) {
        try {
            launch { insertCategoryInBackground(categories) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     */
    private suspend fun insertCategoryInBackground(categories: List<Category>) =
        withContext(Dispatchers.IO) {
            categories.forEach { record ->
                categoryDao?.insertCategory(record)
            }
        }

    /* ITEMS */
    suspend fun getAllItemsList(): Resource<List<Item>>? {
        if (networkState.isOnline()) {
            try {
                val response = api.getAllItems(UserInfo.id, UserInfo.token)
                if (response.isSuccessful) {
                    status = response.body()?.status ?: true
                    //message = response.body()?.response?.message ?: ""
                    message = "All"
                    response.body()?.response?.data?.let {
                        refreshItemsOfflineCache(it.items)
                    }
                } else {
                    return Resource.error(
                        response.body()?.status,
                        response.body()?.response?.message,
                        null
                    )
                }
            } catch (e: Exception) {
                return Resource.error(
                    false,
                    context.getString(R.string.network_error_text),
                    null
                )
            }
        }
        return withContext(Dispatchers.IO) {
            Resource.success(
                status,
                message,
                itemDao?.getAllItems()
            )
        }
    }

    private fun refreshItemsOfflineCache(items: List<Item>) {
        try {
            launch { insertItemsInBackground(items) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun insertItemsInBackground(items: List<Item>) =
        withContext(Dispatchers.IO) {
            items.forEach { record ->
                itemDao?.insertItem(record)
            }
        }
}