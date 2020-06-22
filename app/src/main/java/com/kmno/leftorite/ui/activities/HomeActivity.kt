/*
 * Creator: Kamran Noorinejad on 5/13/20 12:54 PM
 * Last modified: 5/13/20 12:53 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.activities

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.vove7.bottomdialog.BottomDialog
import cn.vove7.bottomdialog.StatusCallback
import cn.vove7.bottomdialog.builder.oneButton
import cn.vove7.bottomdialog.toolbar
import coil.api.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.kmno.leftorite.R
import com.kmno.leftorite.core.App
import com.kmno.leftorite.core.Constants
import com.kmno.leftorite.data.api.State
import com.kmno.leftorite.data.model.Category
import com.kmno.leftorite.data.model.Item
import com.kmno.leftorite.ui.base.BaseActivity
import com.kmno.leftorite.ui.builders.CategoriesViewBuilder
import com.kmno.leftorite.ui.builders.ItemDetailsViewBuilder
import com.kmno.leftorite.ui.listeners.OnSwipeTouchListener
import com.kmno.leftorite.ui.viewmodels.CategoryBottomSheetViewModel
import com.kmno.leftorite.ui.viewmodels.HomeActivityViewModel
import com.kmno.leftorite.utils.Alerts
import com.kmno.leftorite.utils.UserInfo
import com.kmno.leftorite.utils.launchActivity
import com.link184.kidadapter.setUp
import com.link184.kidadapter.simple.SingleKidAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.category_bottom_sheet.view.*
import kotlinx.android.synthetic.main.item_detail_bottom_sheet.view.*
import kotlinx.android.synthetic.main.recyclerview_list_category.view.*
import kotlinx.android.synthetic.main.recyclerview_list_item.view.*
import org.koin.android.viewmodel.ext.android.viewModel


class HomeActivity : BaseActivity() {

    private val homeActivityViewModel: HomeActivityViewModel by viewModel()
    private val categoryBottomSheetViewModel: CategoryBottomSheetViewModel by viewModel()

    private lateinit var adapter: SingleKidAdapter<Any>
    private var allItems = mutableListOf<Item>()
    private var allPairs = mutableListOf<Any>()

    private lateinit var categoryBottomDialog: BottomDialog
    private lateinit var itemDetailsBottomDialog: BottomDialog
    private val itemDetailsViewBuilder = ItemDetailsViewBuilder()

    private var categoriesLoaded = false

    override fun getResId(): Int {
        return R.layout.activity_home
    }

    @SuppressLint("SetTextI18n")
    override fun afterCreate() {

        //set screen as fullscreen and change statusbar theme
        setUpScreen()

        //show user avatar and points
        setupUserInfo()

        //setup categories bottom sheet
        setUpCategoriesBottomDialog()

        //setup item details bottom sheet
        setUpItemDetailsBottomDialog()

        //category selection bottom sheet
        change_category_layout.setOnTouchListener(object : OnSwipeTouchListener(this@HomeActivity) {
            override fun onSwipeUp() {
                super.onSwipeUp()
                categoryBottomDialog.show()
            }

            override fun onClick() {
                super.onClick()
                categoryBottomDialog.show()
            }

        })

        //setting page
        more.setOnClickListener { this.launchActivity<SettingsActivity> {} }

    }

    //TODO: initial setups
    private fun setupUserInfo() {
        user_avatar.load("${Constants.userImageUrl}${UserInfo.avatar}.jpg") {
            crossfade(true)
            diskCachePolicy(CachePolicy.ENABLED)
            transformations(CircleCropTransformation())
        }
        user_points.setNumber(UserInfo.points)
    }

    private fun updateUserPointsAndHeaderTitle(_responseText: String) {
        _responseText.split("=").apply {
            header_title.text = this[1]
            homeActivityViewModel.updateUserPointsPref(this[0].toInt())
            user_points.setNumber(UserInfo.points)
        }
    }

    private fun setUpCategoriesBottomDialog() {
        categoryBottomDialog = BottomDialog.builder(this, show = false) {
            toolbar {
                title = getString(R.string.select_category_title)
                round = true
                navIconId = R.drawable.ic_arrow_down
                onIconClick = {
                    it.dismiss()
                }
            }
            oneButton(
                getString(R.string.select_all_categories),
                bgColorId = R.color.colorPrimaryDark,
                textColorId = R.color.white,
                autoDismiss = true,
                listener = {
                    this.onClick {
                        getAllItems()
                    }
                })
            theme(R.style.BottomDialog_Dark)
            content(CategoriesViewBuilder())
        }

        categoryBottomDialog.listenStatus(object : StatusCallback {
            override fun onCollapsed() {
                super.onCollapsed()
                if (!categoriesLoaded) getCategories()
            }

            override fun onSlide(slideOffset: Float) {}
        })
    }

    private fun setUpItemDetailsBottomDialog() {
        itemDetailsBottomDialog = BottomDialog.builder(this, show = false) {
            toolbar {
                title = "Info"
                navIconId = R.drawable.ic_info
                round = true
            }
            content(itemDetailsViewBuilder)
            theme(R.style.BottomDialog_Dark)
        }
    }

    //TODO: api calls
    private fun getAllItems() {
        homeActivityViewModel.getAllItems().observe(this, Observer { networkResource ->
            when (networkResource.state) {
                State.LOADING -> {
                    showProgress(true)
                }
                State.SUCCESS -> {
                    val status = networkResource.status
                    status?.let {
                        when (it) {
                            true -> {
                                showProgress(false)
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

        categoryBottomSheetViewModel.selectAllCategories().observe(this, Observer {
            //App.logger.error("count ${it?.count()}")
            //  it?.forEach { cat ->
            App.logger.error(it?.toString())
            //}
        })

        categoryBottomSheetViewModel.getCategories().observe(this, Observer { networkResource ->
            when (networkResource.state) {
                State.LOADING -> {
                    categoryBottomDialog.contentView.bottom_sheet_progress_bar.visibility =
                        View.VISIBLE
                }
                State.SUCCESS -> {
                    val status = networkResource.status
                    status?.let {
                        when (it) {
                            true -> {
                                categoryBottomDialog.contentView.bottom_sheet_progress_bar.visibility =
                                    View.GONE
                                networkResource.data?.let { response ->
                                    categoriesLoaded = true
                                    categoryBottomDialog.contentView.categories_recyclerview.run {
                                        this.visibility = View.VISIBLE
                                        this.setUp<Category> {
                                            withLayoutManager(GridLayoutManager(context, 2))
                                            withLayoutResId(R.layout.recyclerview_list_category)
                                            withItems(response as MutableList<Category>)
                                            bindIndexed { category, _ ->
                                                user_avatar.load("${Constants.categoryImageUrl}${category.id}.png") {
                                                    crossfade(true)
                                                    placeholder(R.color.colorPrimaryDark)
                                                }
                                                category_title.text = category.title
                                                setOnClickListener {
                                                    App.logger.error(category.id.toString())
                                                    getItemsByCategory(category)
                                                    categoryBottomDialog.dismiss()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            false -> {
                                categoryBottomDialog.contentView.bottom_sheet_progress_bar.visibility =
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
                    categoryBottomDialog.contentView.bottom_sheet_progress_bar.visibility =
                        View.GONE
                    Alerts.showAlertDialogWithDefaultButton(
                        "Error",
                        networkResource.message!!, "Try Again", this
                    )
                }
            }
        })
    }

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
                        showProgress(true)
                    }
                    State.SUCCESS -> {
                        val status = networkResource.status
                        status?.let {
                            when (it) {
                                true -> {
                                    showProgress(false)
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

    //TODO: adapters and select handlers
    private fun setUpItems() {
        adapter = recyclerView.setUp<Any> {
            withLayoutResId(R.layout.recyclerview_list_item)
            withItems(allPairs)
            bindIndexed { pair, position ->

                with(pair as ArrayList<*>) {
                    (this[0] as Int).let { leftItemIndex ->
                        left_item_imageview_full.load("${Constants.itemsImageUrl}${allItems[leftItemIndex].id}.jpg") {
                            placeholder(R.color.colorPrimaryDark)
                            diskCachePolicy(CachePolicy.ENABLED)
                        }
                        left_item_imageview.load("${Constants.itemsImageUrl}${allItems[leftItemIndex].id}.jpg") {
                            crossfade(true)
                            placeholder(R.drawable.placeholder_trans)
                            diskCachePolicy(CachePolicy.ENABLED)
                        }
                    }

                    (this[1] as Int).let { rightItemIndex ->
                        right_item_imageview_full.load("${Constants.itemsImageUrl}${allItems[rightItemIndex].id}.jpg") {
                            placeholder(R.color.colorPrimary)
                            diskCachePolicy(CachePolicy.ENABLED)
                        }
                        right_item_imageview.load("${Constants.itemsImageUrl}${allItems[rightItemIndex].id}.jpg") {
                            crossfade(true)
                            placeholder(R.drawable.placeholder_trans)
                            diskCachePolicy(CachePolicy.ENABLED)
                        }
                    }
                }

                resetView(this)

                //like button single click
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

                //gesture
                left_item_imageview.setOnTouchListener(object :
                    OnSwipeTouchListener(this@HomeActivity) {
                    override fun onSwipeUp() {
                        super.onSwipeUp()
                        categoryBottomDialog.show()
                    }

                    override fun onSwipeRight() {
                        super.onSwipeRight()
                        handleSelectedView(
                            "left",
                            allItems[pair[0] as Int].id,
                            position,
                            this@bindIndexed
                        )
                    }

                    override fun onLongClick() {
                        super.onLongClick()
                        itemDetailsBottomDialog.show()
                        itemDetailsBottomDialog.contentView.run {
                            this.item_logo.load("${Constants.itemsImageUrl}${allItems[pair[0] as Int].id}.jpg") {
                                crossfade(true)
                                placeholder(R.color.colorPrimary)
                                diskCachePolicy(CachePolicy.ENABLED)
                            }
                            this.item_title.text = allItems[pair[0] as Int].title
                            this.item_description.text = allItems[pair[0] as Int].description
                        }
                    }

                    override fun onDoubleClick() {
                        super.onDoubleClick()
                        handleSelectedView(
                            "left",
                            allItems[pair[0] as Int].id,
                            position,
                            this@bindIndexed
                        )
                    }
                })

                right_item_imageview.setOnTouchListener(object :
                    OnSwipeTouchListener(this@HomeActivity) {
                    override fun onSwipeUp() {
                        super.onSwipeUp()
                        categoryBottomDialog.show()
                    }

                    override fun onSwipeLeft() {
                        super.onSwipeLeft()
                        handleSelectedView(
                            "right",
                            allItems[pair[1] as Int].id,
                            position,
                            this@bindIndexed
                        )
                    }

                    override fun onLongClick() {
                        super.onLongClick()
                        itemDetailsBottomDialog.show()
                        itemDetailsBottomDialog.contentView.run {
                            this.item_logo.load("${Constants.itemsImageUrl}${allItems[pair[1] as Int].id}.jpg") {
                                crossfade(true)
                                placeholder(R.color.colorPrimary)
                                diskCachePolicy(CachePolicy.ENABLED)
                            }
                            this.item_title.text = allItems[pair[1] as Int].title
                            this.item_description.text = allItems[pair[1] as Int].description
                        }
                    }

                    override fun onDoubleClick() {
                        super.onDoubleClick()
                        handleSelectedView(
                            "right",
                            allItems[pair[1] as Int].id,
                            position,
                            this@bindIndexed
                        )
                    }
                })

                //click item
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
        _selectedView.separator.visibility = View.GONE
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

    private fun showProgress(_show: Boolean) {
        when (_show) {
            true -> {
                header_title.visibility = View.GONE
                tashie_loader_progress.visibility = View.VISIBLE
            }
            false -> {
                header_title.visibility = View.VISIBLE
                tashie_loader_progress.visibility = View.GONE
            }
        }
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
                        showProgress(true)
                    }
                    State.SUCCESS -> {
                        val status = networkResource.status
                        status?.let { respStatus ->
                            when (respStatus) {
                                true -> {
                                    showProgress(false)
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
        showProgress(false)
        header_title.text = getString(R.string.which_one)
        _view.separator.visibility = View.VISIBLE
        no_more_items_layout.visibility = View.GONE
        _view.right_item_imageview_full.visibility = View.GONE
        _view.left_item_imageview_full.visibility = View.GONE

    }

    private fun updateAdapter(_position: Int) {
        recyclerView.postDelayed({
            adapter update {
                it.removeAt(_position)
                adapter.notifyItemRemoved(_position)
                adapter.notifyItemRangeChanged(_position, it.size)
                if (it.isEmpty()) {
                    header_title.text = getString(R.string.no_more_items)
                    no_more_items_layout.visibility = View.VISIBLE
                }
            }

        }, 1000)
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