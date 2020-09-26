/*
 * Creator: Kamran Noorinejad on 9/22/20 4:16 PM
 * Last modified: 9/22/20 4:16 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.activities

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import coil.api.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.kmno.leftorite.R
import com.kmno.leftorite.core.App
import com.kmno.leftorite.core.Constants
import com.kmno.leftorite.data.api.State
import com.kmno.leftorite.data.model.Category
import com.kmno.leftorite.data.model.History
import com.kmno.leftorite.ui.base.BaseActivity
import com.kmno.leftorite.ui.viewmodels.CategoryViewModel
import com.kmno.leftorite.ui.viewmodels.ProfileActivityViewModel
import com.kmno.leftorite.utils.DateHelper.Companion.convertLongToTime
import com.kmno.leftorite.utils.UserInfo
import com.kmno.leftorite.utils.extensions.commaString
import com.kmno.leftorite.utils.launchActivity
import com.link184.kidadapter.setUp
import com.link184.kidadapter.simple.SingleKidAdapter
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.category_tabs_layout.view.*
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.history_list_item.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.math.abs


class ProfileActivity : BaseActivity() {

    private val profileActivityViewModel: ProfileActivityViewModel by viewModel()
    private val categoryViewModel: CategoryViewModel by viewModel()

    private lateinit var historiesTestAdapter: SingleKidAdapter<Any>
    private var histories = mutableListOf<Any>()

    override fun getResId(): Int {
        return R.layout.activity_profile
    }

    override fun afterCreate() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        //show user avatar and points
        setupUserInfo()

        //feed categories tab
        getCategories()

        categories_retry_button.setOnClickListener {
            getCategories()
        }
    }

    override fun ready() {

        app_bar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->

            listOf<View>(toolbar_profile_user_avatar, toolbar_title, toolbar_description).forEach {
                it.alpha = abs(
                    verticalOffset / appBarLayout.totalScrollRange
                        .toFloat()
                )
            }
        })

        tabs_layout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                getHistoryByCategoryId(tab?.tag as Int)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    //api
    //categories
    private fun getCategories() {
        categoryViewModel.selectAllCategories()
            .observe(this, Observer { networkResource ->
                when (networkResource?.state) {
                    State.LOADING -> {
                        categories_progress_bar.visibility = View.VISIBLE
                        tabs_container.visibility = View.GONE
                        categories_retry_button.visibility = View.GONE
                    }
                    State.SUCCESS -> {
                        val status = networkResource.status
                        status?.let {
                            when (it) {
                                true -> {
                                    networkResource.data?.let { response ->
                                        if (response.count() > 0) {
                                            tabs_container.visibility = View.VISIBLE
                                            history_view_pager.visibility = View.VISIBLE
                                            categories_progress_bar.visibility = View.GONE
                                            response.forEach { categoryRecord ->
                                                tabs_layout.addTab(
                                                    tabs_layout.newTab()
                                                        .setTag(categoryRecord.id)
                                                        .setCustomView(
                                                            createTabItemView(
                                                                categoryRecord
                                                            )
                                                        ), false
                                                )
                                            }
                                        } else {
                                            categories_progress_bar.visibility = View.GONE
                                            tabs_container.visibility = View.GONE
                                            categories_retry_button.visibility = View.VISIBLE
                                        }

                                    }
                                }
                                false -> {
                                    categories_progress_bar.visibility = View.GONE
                                    tabs_container.visibility = View.GONE
                                    categories_retry_button.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                    State.ERROR -> {
                        categories_progress_bar.visibility = View.GONE
                        select_category_fixed_tab.visibility = View.GONE
                        categories_retry_button.visibility = View.VISIBLE
                    }
                }
            })
    }

    //history
    private fun getHistoryByCategoryId(_catId: Int) {
        profileActivityViewModel.getHistoryByCategoryId(_catId)
            .observe(this, Observer { networkResource ->
                when (networkResource?.state) {
                    State.LOADING -> {
                        /*categories_progress_bar.visibility = View.VISIBLE
                        tabs_container.visibility = View.GONE
                        categories_retry_button.visibility = View.GONE*/
                    }
                    State.SUCCESS -> {
                        val status = networkResource.status
                        status?.let {
                            when (it) {
                                true -> {
                                    networkResource.data?.let { response ->
                                        if (response.count() > 0) {
                                            historiesTestAdapter =
                                                histories_recyclerview.setUp<Any> {
                                                    withLayoutResId(R.layout.history_list_item)
                                                    //withItems(histories.toMutableList())
                                                    withItems(response as MutableList<Any>)
                                                    bindIndexed { history, position ->
                                                        App.logger.error(history.toString())
                                                        with(history as History) {
                                                            left_history_item_imageview?.let {
                                                                left_history_item_imageview.load("${Constants.itemsImageUrl}${this.item_id}.jpg") {
                                                                    crossfade(true)
                                                                    allowHardware(false)
                                                                    diskCachePolicy(CachePolicy.ENABLED)
                                                                }
                                                            }
                                                            right_history_item_imageview?.let {
                                                                right_history_item_imageview.load("${Constants.itemsImageUrl}${this.item_id_2}.jpg") {
                                                                    crossfade(true)
                                                                    allowHardware(false)
                                                                    diskCachePolicy(CachePolicy.ENABLED)
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                        } else {
                                            /*categories_progress_bar.visibility = View.GONE
                                            tabs_container.visibility = View.GONE
                                            categories_retry_button.visibility = View.VISIBLE*/
                                        }

                                    }
                                }
                                false -> {
                                    /*categories_progress_bar.visibility = View.GONE
                                    tabs_container.visibility = View.GONE
                                    categories_retry_button.visibility = View.VISIBLE*/
                                }
                            }
                        }
                    }
                    State.ERROR -> {
                        /*categories_progress_bar.visibility = View.GONE
                        select_category_fixed_tab.visibility = View.GONE
                        categories_retry_button.visibility = View.VISIBLE*/
                    }
                }
            })
    }

    private fun createTabItemView(_category: Category): View? {
        val parentLayout: View =
            LayoutInflater.from(this).inflate(R.layout.category_tabs_layout, null)
        parentLayout.category_avatar.load("${Constants.categoryImageUrl}${_category.id}.png") {
            crossfade(true)
            placeholder(R.color.colorPrimaryDark)
        }
        parentLayout.category_title.text = _category.title
        return parentLayout
    }

    @SuppressLint("SetTextI18n")
    private fun setupUserInfo() {
        profile_user_avatar.load("${Constants.userImageUrl}${UserInfo.avatar}") {
            crossfade(true)
            diskCachePolicy(CachePolicy.ENABLED)
            allowHardware(false)
            transformations(CircleCropTransformation())
        }

        toolbar_profile_user_avatar.load("${Constants.userImageUrl}${UserInfo.avatar}") {
            crossfade(true)
            diskCachePolicy(CachePolicy.ENABLED)
            allowHardware(false)
            transformations(CircleCropTransformation())
        }
        handlePoints()
        profile_user_email.text = UserInfo.email
        profile_user_last_login.text = "Last Login: ${convertLongToTime(UserInfo.lastLoginDate)}"
    }

    @SuppressLint("SetTextI18n")
    private fun handlePoints() {
        try {
            if (UserInfo.points > 9999) {
                profile_user_points.text = "+${(UserInfo.points / 1000).commaString}K points"
            } else {
                profile_user_points.text = "${UserInfo.points.commaString} points"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
    }

    override fun networkStatus(state: Boolean) {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            this.launchActivity<SettingsActivity> {}
            true
        } else super.onOptionsItemSelected(item)
    }
}