/*
 * Creator: Kamran Noorinejad on 9/23/20 12:17 PM
 * Last modified: 9/23/20 12:17 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kmno.leftorite.data.repository.HistoryRepository
import com.kmno.leftorite.utils.UserInfo
import kotlinx.coroutines.Dispatchers

/**
 * Created by Kamran Noorinejad on 9/23/2020 AD 12:17.
 * Edited by Kamran Noorinejad on 9/23/2020 AD 12:17.
 */
class ProfileActivityViewModel(private val historyRepository: HistoryRepository) : ViewModel() {

    fun getHistoryByCategoryId(categoryId: Int) = liveData(Dispatchers.IO) {
        emit(historyRepository.getHistoryByCategory(UserInfo.id, categoryId, UserInfo.token))
    }

}