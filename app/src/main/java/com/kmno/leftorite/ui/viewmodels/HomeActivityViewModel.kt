/*
 * Creator: Kamran Noorinejad on 5/17/20 10:48 AM
 * Last modified: 5/17/20 10:48 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.viewmodels

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.elconfidencial.bubbleshowcase.BubbleShowCase
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import com.elconfidencial.bubbleshowcase.BubbleShowCaseListener
import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.ApiClientProvider
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.model.Category
import com.kmno.leftorite.data.repository.DbRepository
import com.kmno.leftorite.ui.activities.HomeActivity
import com.kmno.leftorite.utils.AppSetting
import com.kmno.leftorite.utils.ConfigPref
import com.kmno.leftorite.utils.ShowCase
import com.kmno.leftorite.utils.UserInfo
import kotlinx.coroutines.Dispatchers

/**
 * Created by Kamran Noorinejad on 5/17/2020 AD 10:48.
 * Edited by Kamran Noorinejad on 5/17/2020 AD 10:48.
 */
class HomeActivityViewModel(
    private val context: Context,
    apiProvider: ApiClientProvider,
    private val dbRepository: DbRepository
) :
    ViewModel() {

    private val api = apiProvider.createApiClient()

    fun checkIfWelcomeDialogIsShown(): Boolean {
        return ShowCase.welcomeDialogIsShown
    }

    fun setLastSelectedCategoryData(_selectedIndex: Int, _selectedId: Int, _selectedTitle: String) {
        UserInfo.lastSelectedCategoryIndex = _selectedIndex
        UserInfo.lastSelectedCategoryId = _selectedId
        UserInfo.lastSelectedCategoryTitle = _selectedTitle
    }


    fun getLastSelectedCategoryIndex(): Int {
        return UserInfo.lastSelectedCategoryIndex
    }

    fun getLastSelectedCategoryId(): Int {
        return UserInfo.lastSelectedCategoryId
    }

    fun getLastSelectedCategoryTitle(): String {
        return UserInfo.lastSelectedCategoryTitle
    }

    fun getLastSelectedCategoryObject(): Category {
        return Category(
            UserInfo.lastSelectedCategoryId,
            UserInfo.lastSelectedCategoryTitle,
            1, 0
        )
    }

    fun getInitialDefaultCategory(): Category {
        return Category(
            ConfigPref.default_cat_id,
            "People",
            1, 0
        )
    }

    fun getInitialDefaultCategoryId(): Int {
        return ConfigPref.default_cat_id
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
        _desc: String,
        closeAction: () -> Unit = {}
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
            .listener(object : BubbleShowCaseListener { //Listener for user actions
                override fun onTargetClick(bubbleShowCase: BubbleShowCase) {
                    //Called when the user clicks the target
                    closeAction()
                }

                override fun onCloseActionImageClick(bubbleShowCase: BubbleShowCase) {
                    //Called when the user clicks the close button
                    closeAction()
                }

                override fun onBubbleClick(bubbleShowCase: BubbleShowCase) {
                    //Called when the user clicks on the bubble
                }

                override fun onBackgroundDimClick(bubbleShowCase: BubbleShowCase) {
                    //Called when the user clicks on the background dim
                }
            })
        _targetView.let {
            it?.let { view -> build.targetView(view) }
        }
        return build
    }

    //get all items list
    /*fun getAllItems(offset: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.getAllItems(UserInfo.id, UserInfo.token,
                AppSetting.itemsPerRequestLimit, offset)
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
            emit(
                Resource.error(
                    false,
                    context.getString(R.string.network_error_text),
                    null
                )
            )
        }
    }*/

    //get items list based on selected category
    fun getItemsByCategory(categoryId: Int, offset: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.getItemsByCategory(
                categoryId, UserInfo.id, UserInfo.token,
                AppSetting.itemsPerRequestLimit, offset
            )
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
    fun setSelectedItem(itemId: Int, pairId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.setSelectedItem(itemId, pairId, UserInfo.id, UserInfo.token)
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