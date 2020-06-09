/*
 * Creator: Kamran Noorinejad on 5/13/20 12:54 PM
 * Last modified: 5/13/20 12:53 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.vove7.bottomdialog.BottomDialog
import cn.vove7.bottomdialog.StatusCallback
import cn.vove7.bottomdialog.builder.oneButton
import cn.vove7.bottomdialog.toolbar
import coil.api.load
import coil.request.CachePolicy
import com.kmno.leftorite.App
import com.kmno.leftorite.R
import com.kmno.leftorite.data.Constants
import com.kmno.leftorite.data.api.State
import com.kmno.leftorite.data.model.Category
import com.kmno.leftorite.data.model.Item
import com.kmno.leftorite.ui.base.BaseActivity
import com.kmno.leftorite.ui.builders.MarkdownContentBuilder
import com.kmno.leftorite.ui.viewmodels.HomeActivityViewModel
import com.kmno.leftorite.utils.Alerts
import com.kmno.leftorite.utils.DoubleTapListener
import com.kmno.leftorite.utils.UserInfo
import com.link184.kidadapter.setUp
import com.link184.kidadapter.simple.SingleKidAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.modal_bottom_sheet.view.*
import kotlinx.android.synthetic.main.recyclerview_list_category.view.*
import kotlinx.android.synthetic.main.recyclerview_list_item.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {

    private val homeActivityViewModel: HomeActivityViewModel by viewModel()

    private lateinit var adapter: SingleKidAdapter<Any>
    private var allItems = mutableListOf<Item>()
    private var allPairs = mutableListOf<Any>()
    private lateinit var bottomDialg: BottomDialog
    private var categoriesLoaded = false

    var timeToLive: Long = 2000
    var minSpeed: Float = 4f
    var maxSpeed: Float = 7f
    var colors =
        intArrayOf(R.color.lt_yellow, R.color.lt_orange, R.color.lt_purple, R.color.lt_pink)

    override fun getResId(): Int {
        return R.layout.activity_home
    }

    @SuppressLint("SetTextI18n")
    override fun afterCreate() {

        //set screen as fullscreen and change statusbar theme
        setUpScreen()

        setupUserInfo()

        setUpBottomDialog()

        //category selection bottom sheet
        change_category_layout.setOnClickListener { bottomDialg.show() }

    }

    private fun setupUserInfo() {
        category_icon.load("${Constants.userImageUrl}avatar.png") {
            crossfade(true)
            diskCachePolicy(CachePolicy.ENABLED)
        }
        user_points.setNumber(UserInfo.points)
    }

    private fun updateUserPointsAndHeaderTitle(_responseText: String) {
        _responseText.split("=").apply {
            header_title.text = this[1]
            UserInfo.points = this[0].toInt()
            user_points.setNumber(UserInfo.points)
        }
    }

    private fun setUpBottomDialog() {
        bottomDialg = BottomDialog.builder(this, show = false) {
            toolbar {
                title = getString(R.string.select_category_title)
                round = true
                navIconId = R.drawable.ic_arrow_down
                onIconClick = {
                    it.dismiss()
                }
            }
            oneButton(getString(R.string.select_all_categories),
                bgColorId = R.color.colorPrimaryDark,
                textColorId = R.color.white,
                autoDismiss = true,
                listener = {
                    this.onClick {
                        App.logger.error("listener -------------- ")
                        getAllItems()
                    }
                })
            theme(R.style.BottomDialog_Dark)
            content(MarkdownContentBuilder())
        }

        bottomDialg.listenStatus(object : StatusCallback {
            override fun onExpand() {
                super.onExpand()
            }

            override fun onHidden() {
                super.onHidden()
                // categoriesLoaded = false
            }

            override fun onCollapsed() {
                App.logger.error("onCollapsed")
                super.onCollapsed()
                if (!categoriesLoaded) getCategories()
            }

            override fun onSlide(slideOffset: Float) {}
        })
    }

    private fun getAllItems() {
        homeActivityViewModel.getAllItems().observe(this, Observer { networkResource ->
            when (networkResource.state) {
                State.LOADING -> {
                    Alerts.showFlashbarWithProgress(this)
                }
                State.SUCCESS -> {
                    val status = networkResource.status
                    status?.let {
                        when (it) {
                            true -> {
                                Alerts.dismissProgressFlashbar()
                                networkResource.data?.let { response ->
                                    current_category_text.text = "All"
                                    allItems = response.items as MutableList<Item>
                                    allPairs = response.finalPairs as MutableList<Any>
                                    setUpItems()
                                }
                            }
                            false -> {
                                Alerts.showAlertDialogWithDefaultButton(
                                    "Error",
                                    networkResource.message!!,
                                    "Try Again",
                                    this
                                )
                            }
                        }
                    }
                }
                State.ERROR -> {
                    Alerts.showAlertDialogWithDefaultButton(
                        "Error",
                        networkResource.message!!, "Try Again", this
                    )
                }
            }
        })
    }

    private fun getCategories() {
        homeActivityViewModel.getCategories().observe(this, Observer { networkResource ->
            when (networkResource.state) {
                State.LOADING -> {
                    bottomDialg.contentView.bottom_sheet_progress_bar.visibility = View.VISIBLE
                }
                State.SUCCESS -> {
                    val status = networkResource.status
                    status?.let {
                        when (it) {
                            true -> {
                                bottomDialg.contentView.bottom_sheet_progress_bar.visibility =
                                    View.GONE
                                networkResource.data?.let { response ->
                                    categoriesLoaded = true
                                    bottomDialg.contentView.categories_recyclerview.run {
                                        this.visibility = View.VISIBLE
                                        this.setUp<Category> {
                                            withLayoutManager(GridLayoutManager(context, 2))
                                            withLayoutResId(R.layout.recyclerview_list_category)
                                            withItems(response as MutableList<Category>)
                                            bindIndexed { category, _ ->
                                                category_icon.load("${Constants.categoryImageUrl}${category.id}.png") {
                                                    crossfade(true)
                                                    placeholder(R.color.colorPrimaryDark)
                                                }
                                                category_title.text = category.title
                                                setOnClickListener {
                                                    App.logger.error(category.id.toString())
                                                    getItemsByCategory(category)
                                                    bottomDialg.dismiss()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            false -> {
                                bottomDialg.contentView.bottom_sheet_progress_bar.visibility =
                                    View.GONE
                                Alerts.showAlertDialogWithDefaultButton(
                                    "Error",
                                    networkResource.message!!,
                                    "Try Again",
                                    this
                                )
                            }
                        }
                    }
                }
                State.ERROR -> {
                    bottomDialg.contentView.bottom_sheet_progress_bar.visibility = View.GONE
                    Alerts.showAlertDialogWithDefaultButton(
                        "Error",
                        networkResource.message!!, "Try Again", this
                    )
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun getItemsByCategory(_category: Category) {
        adapter update {
            it.removeAll(it)
            adapter.clear()
            allItems.clear()
            allPairs.clear()
        }
        homeActivityViewModel.getItemsByCategory(_category.id)
            .observe(this, Observer { networkResource ->
                when (networkResource.state) {
                    State.LOADING -> {
                        //   Alerts.showFlashbarWithProgress(this)
                    }
                    State.SUCCESS -> {
                        val status = networkResource.status
                        status?.let {
                            when (it) {
                                true -> {
                                    //Alerts.dismissProgressFlashbar()
                                    networkResource.data?.let { response ->
                                        current_category_text.text = _category.title
                                        allItems = response.items as MutableList<Item>
                                        allPairs = response.finalPairs as MutableList<Any>
                                        App.logger.error {
                                            allItems + " " + allPairs
                                        }
                                        setUpItems()
                                    }
                                }
                                false -> {
                                    Alerts.showAlertDialogWithDefaultButton(
                                        "Error",
                                        networkResource.message!!,
                                        "Try Again",
                                        this
                                    )
                                }
                            }
                        }
                    }
                    State.ERROR -> {
                        Alerts.showAlertDialogWithDefaultButton(
                            "Error",
                            networkResource.message!!, "Try Again", this
                        )
                    }
                }
            })
    }

    private fun setUpScreen() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun setUpItems() {
        adapter = recyclerView.setUp<Any> {
            withLayoutResId(R.layout.recyclerview_list_item)
            withItems(allPairs)
            bindIndexed { pair, position ->

                with(pair as ArrayList<*>) {

                    (this[0] as Int).let { leftItemIndex ->
                        left_item_imageview_full.load("${Constants.itemsImageUrl}${allItems[leftItemIndex].id}.jpg")
                        left_item_imageview.load("${Constants.itemsImageUrl}${allItems[leftItemIndex].id}.jpg") {
                            crossfade(true)
                            placeholder(R.color.colorPrimaryDark)
                            diskCachePolicy(CachePolicy.ENABLED)
                        }
                    }

                    (this[1] as Int).let { rightItemIndex ->
                        right_item_imageview.load("${Constants.itemsImageUrl}${allItems[rightItemIndex].id}.jpg") {
                            crossfade(true)
                            placeholder(R.color.colorPrimary)
                        }
                        right_item_imageview_full.load("${Constants.itemsImageUrl}${allItems[rightItemIndex].id}.jpg")
                    }

                }

                resetView(this)

                //button single click
                select_right_item_button.setOnClickListener {
                    handleSelectedView(
                        "right",
                        allItems[pair[1] as Int].id,
                        position,
                        this@bindIndexed
                    )
                }

                select_left_item_button.setOnClickListener {
                    handleSelectedView(
                        "left",
                        allItems[pair[0] as Int].id,
                        position,
                        this@bindIndexed
                    )
                }

                //double click
                right_item_imageview.setOnClickListener(
                    DoubleTapListener(
                        callback = object : DoubleTapListener.Callback {
                            override fun doubleClicked() {
                                App.logger.error("doubleClicked ---------> right_item_imageview + item id =${pair[1]}")
                                handleSelectedView(
                                    "right",
                                    allItems[pair[1] as Int].id,
                                    position,
                                    this@bindIndexed
                                )
                                /*try {
                                                               viewKonfetti.build()
                                                                   .addColors(*colors)
                                                                   .setDirection(0.0, 359.0)
                                                                   .setSpeed(minSpeed, maxSpeed)
                                                                   .setFadeOutEnabled(true)
                                                                   .setTimeToLive(timeToLive)
                                                                   //  .addShapes(shapes)
                                                                   .addSizes(Size(12), Size(16, 6f))
                                                                   .setPosition(
                                                                       viewKonfetti.x + viewKonfetti.width / 2,
                                                                       viewKonfetti.y + viewKonfetti.height / 3
                                                                   )
                                                                   .burst(100)
                                                           }catch (e:Exception){
                                                               e.printStackTrace()
                                                           }*/
                            }
                        }
                    ))

                left_item_imageview.setOnClickListener(DoubleTapListener(
                    callback = object : DoubleTapListener.Callback {
                        override fun doubleClicked() {
                            App.logger.error("doubleClicked ---------> left_item_imageview")
                            handleSelectedView(
                                "left",
                                allItems[pair[0] as Int].id,
                                position,
                                this@bindIndexed
                            )
                        }
                    }
                ))

                setOnClickListener {}
            }
        }

        //disable vertical scroll
        recyclerView.layoutManager = object : LinearLayoutManager(applicationContext) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    private fun handleSelectedView(
        _selectedSide: String,
        _selectedItemId: Int,
        _position: Int,
        _selectedView: View
    ) {
        when (_selectedSide) {
            "right" -> {
                _selectedView.right_item_imageview_full.visibility = View.VISIBLE
            }
            "left" -> {
                _selectedView.left_item_imageview_full.visibility = View.VISIBLE
            }
        }
        setSelectedItem(_selectedItemId, _position, _selectedView)
    }

    private fun setSelectedItem(
        _selectedItemId: Int,
        _position: Int,
        _selectedView: View
    ) {
        homeActivityViewModel.setSelectedItem(_selectedItemId)
            .observe(this, Observer { networkResource ->
                when (networkResource.state) {
                    State.LOADING -> {
                        //   Alerts.showFlashbarWithProgress(this)
                    }
                    State.SUCCESS -> {
                        val status = networkResource.status
                        status?.let {
                            when (it) {
                                true -> {
                                    updateUserPointsAndHeaderTitle(networkResource.data as String)
                                    updateAdapter(_position)
                                }
                                false -> {
                                    resetView(_selectedView)
                                    Alerts.showAlertDialogWithDefaultButton(
                                        "Error",
                                        networkResource.message!!,
                                        "Try Again",
                                        this
                                    )
                                }
                            }
                        }
                    }
                    State.ERROR -> {
                        resetView(_selectedView)
                        Alerts.showAlertDialogWithDefaultButton(
                            "Error",
                            networkResource.message!!, "Try Again", this
                        )
                    }
                }
            })
    }

    private fun resetView(_view: View) {
        header_title.text = getString(R.string.which_one)
        _view.right_item_imageview_full.visibility = View.GONE
        _view.left_item_imageview_full.visibility = View.GONE

    }

    private fun updateAdapter(_position: Int) {
        recyclerView.postDelayed({
            adapter update {
                it.removeAt(_position)
                adapter.notifyItemRemoved(_position)
                adapter.notifyItemRangeChanged(_position, it.size)
                if (it.isEmpty()) header_title.text = getString(R.string.no_more_items)
            }

        }, 1000)
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
    }

    override fun networkStatus(state: Boolean) {
        if (state) getAllItems()
    }

}