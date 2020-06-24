/*
 * Creator: Kamran Noorinejad on 5/17/20 10:48 AM
 * Last modified: 5/17/20 10:48 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.viewmodels

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.elconfidencial.bubbleshowcase.BubbleShowCase
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.ApiClientProvider
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.model.Item
import com.kmno.leftorite.ui.activities.HomeActivity
import com.kmno.leftorite.utils.ShowCase
import com.kmno.leftorite.utils.UserInfo
import kotlinx.coroutines.Dispatchers

/**
 * Created by Kamran Noorinejad on 5/17/2020 AD 10:48.
 * Edited by Kamran Noorinejad on 5/17/2020 AD 10:48.
 */
class HomeActivityViewModel(private val context: Context, apiProvider: ApiClientProvider) :
    ViewModel() {

    private val api = apiProvider.createApiClient()

    var items = emptyList<Item>()
    var pairs = emptyList<Any>()

    init {
    }

    fun checkIfWelcomeDialogIsShown(): Boolean {
        return ShowCase.welcomeDialogIsShown
    }

    fun setWelcomeDialogIsShown() {
        ShowCase.welcomeDialogIsShown = true
    }

    fun setAppTipIsShown() {
        ShowCase.appTipIsShown = true
    }

    fun showCaseBuilder(
        _activity: HomeActivity,
        _targetView: View? = null,
        _title: String,
        _desc: String
    ): BubbleShowCaseBuilder {
        val build = BubbleShowCaseBuilder(_activity) //Activity instance
            .title(_title) //Any title for the bubble view
            .description(_desc) //More detailed description
            .backgroundColorResourceId(R.color.colorAccent) //Bubble background color
            .textColorResourceId(R.color.colorPrimaryDark) //Bubble Text color
            .titleTextSize(18) //Title text size in SP (default value 16sp)
            .descriptionTextSize(16) //Subtitle text size in SP (default value 14sp)
            .imageResourceId(R.mipmap.ic_launcher_round) //Bubble main image
            .highlightMode(BubbleShowCase.HighlightMode.VIEW_SURFACE)
            .closeActionImageResourceId(R.drawable.ic_cancel)
        _targetView.let {
            it?.let { view -> build.targetView(view) }
        }
        return build
    }

    //get all items list
    fun getAllItems() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.getAllItems(UserInfo.id, UserInfo.token)
            if (response.isSuccessful) {
                emit(
                    Resource.success(
                        response.body()?.status,
                        response.body()?.response?.message,
                        response.body()?.response?.data
                    )

                    // items = response.body().response.data.items
                    // pairs = response.body().response.data.finalPairs
                )
            } else {
                emit(
                    Resource.error(
                        response.body()?.status,
                        response.body()?.response?.message,
                        null
                    )
                )
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

    //get items list based on selected category
    fun getItemsByCategory(categoryId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.getItemsByCategory(categoryId, UserInfo.id, UserInfo.token)
            if (response.isSuccessful) {
                emit(
                    Resource.success(
                        response.body()?.status,
                        response.body()?.response?.message,
                        response.body()?.response?.data
                    )
                )
            } else {
                emit(
                    Resource.error(
                        response.body()?.status,
                        response.body()?.response?.message,
                        null
                    )
                )
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

    //set selected item
    fun setSelectedItem(itemId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.setSelectedItem(itemId, UserInfo.id, UserInfo.token)
            if (response.isSuccessful) {
                emit(
                    Resource.success(
                        response.body()?.status,
                        response.body()?.response?.message,
                        response.body()?.response?.data
                    )
                )
            } else {
                emit(
                    Resource.error(
                        response.body()?.status,
                        response.body()?.response?.message,
                        null
                    )
                )
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

    fun updateUserPointsPref(newPoint: Int) {
        UserInfo.points = newPoint
    }

    override fun onCleared() {
        super.onCleared()
    }
}