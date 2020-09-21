/*
 * Creator: Kamran Noorinejad on 6/9/20 3:37 PM
 * Last modified: 6/9/20 3:37 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.api.Resource.Companion.loading
import com.kmno.leftorite.data.repository.DbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

/**
 * Created by Kamran Noorinejad on 6/9/2020 AD 15:37.
 * Edited by Kamran Noorinejad on 6/9/2020 AD 15:37.
 */
class CategoryBottomSheetViewModel(
    private val context: Context,
    private val dbRepository: DbRepository
) :
    ViewModel() {

    fun selectAllCategories() = liveData(Dispatchers.IO) {
        emit(loading())
        try {
            dbRepository.getCategoriesList()?.collect {
                emit(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                Resource.error(
                    false,
                    context.getString(R.string.network_error_text),
                    null
                )
            )
        }
    }

}