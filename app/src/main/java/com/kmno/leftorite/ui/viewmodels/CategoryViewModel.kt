/*
 * Creator: Kamran Noorinejad on 6/9/20 3:37 PM
 * Last modified: 6/9/20 3:37 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.api.Resource.Companion.loading
import com.kmno.leftorite.data.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

/**
 * Created by Kamran Noorinejad on 6/9/2020 AD 15:37.
 * Edited by Kamran Noorinejad on 6/9/2020 AD 15:37.
 */
class CategoryViewModel(
    private val appCtx: Application,
    private val categoryRepository: CategoryRepository
) :
    ViewModel() {

    fun selectAllCategories() = liveData(Dispatchers.IO) {
        //emit(categoryRepository.getCategoriesList()?.collect())

        emit(loading())
        try {
            categoryRepository.getCategoriesList()?.collect {
                emit(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                Resource.error(
                    false,
                    appCtx.getString(R.string.network_error_text),
                    null
                )
            )
        }
    }

}