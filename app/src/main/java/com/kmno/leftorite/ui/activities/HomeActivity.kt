/*
 * Creator: Kamran Noorinejad on 5/13/20 12:54 PM
 * Last modified: 5/13/20 12:53 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.activities

import android.animation.Animator
import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.vove7.bottomdialog.BottomDialog
import cn.vove7.bottomdialog.StatusCallback
import cn.vove7.bottomdialog.builder.oneButton
import cn.vove7.bottomdialog.toolbar
import coil.Coil.imageLoader
import coil.api.load
import coil.request.CachePolicy
import coil.request.LoadRequest
import coil.size.ViewSizeResolver
import coil.transform.CircleCropTransformation
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
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
import com.kmno.leftorite.utils.Alerts
import com.kmno.leftorite.utils.ConfigPref
import com.kmno.leftorite.utils.UserInfo
import com.kmno.leftorite.utils.launchActivity
import com.kmno.leftorite.viewmodels.CategoryBottomSheetViewModel
import com.kmno.leftorite.viewmodels.HomeActivityViewModel
import com.link184.kidadapter.setUp
import com.link184.kidadapter.simple.SingleKidAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home_content.*
import kotlinx.android.synthetic.main.activity_home_loading.*
import kotlinx.android.synthetic.main.category_bottom_sheet.view.*
import kotlinx.android.synthetic.main.item_detail_bottom_sheet.view.*
import kotlinx.android.synthetic.main.recycleriew_list_item_splitted_view.*
import kotlinx.android.synthetic.main.recycleriew_list_item_splitted_view.view.*
import kotlinx.android.synthetic.main.recyclerview_list_category.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import xyz.hanks.library.bang.SmallBangView

@Suppress("UNCHECKED_CAST")
class HomeActivity : BaseActivity() {

    private var _xDelta = 0

    private val homeActivityViewModel: HomeActivityViewModel by viewModel()
    private val categoryBottomSheetViewModel: CategoryBottomSheetViewModel by viewModel()

    private lateinit var itemsAdapter: SingleKidAdapter<Any>
    private lateinit var categoriesAdapter: SingleKidAdapter<Category>
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

        //set config params
        setUpConfigValues()

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

        //generate fcm token for push notifications
        generateFCMToken()

        //when a network request failed, user clicks retry
        home_retry_button.setOnClickListener {
            callHomeActivityApi()
        }

    }

    //set retrieved config values and messages
    private fun setUpConfigValues() {
        ConfigPref.let {
            header_title.text = it.top_text
            category_text.text = it.bottom_text
        }
    }

    private fun showNewMessagesDialog() {
        if (ConfigPref.new_msg_id != UserInfo.latestMsgId) {
            UserInfo.latestMsgId = ConfigPref.new_msg_id
            Alerts.showAlertDialogWithDefaultButton(
                title = ConfigPref.new_msg_title,
                msg = ConfigPref.new_msg_content,
                action = getString(R.string.new_message_button_text),
                activity = this
            )
        }
    }

    override fun ready() {
        //show app usage tips
        setUpShowcase()

        //show message dialog after the showcase
        if (homeActivityViewModel.checkIfWelcomeDialogIsShown())
            showNewMessagesDialog()
    }

    @SuppressLint("StringFormatInvalid")
    private fun setUpShowcase() {
        if (!homeActivityViewModel.checkIfWelcomeDialogIsShown()) {
            Alerts.showAlertDialogWithTwoActionButton(getString(R.string.welcome),
                getString(R.string.welcome_description),
                getString(R.string.letsgo), getString(R.string.show_me_how),
                {
                    showNewMessagesDialog()
                }, {
                    BubbleShowCaseSequence()
                        .addShowCase(
                            homeActivityViewModel.showCaseBuilder(
                                this,
                                _title = getString(R.string.how_like),
                                _desc = getString(R.string.how_like_description)
                            )
                        )
                        .addShowCase(
                            homeActivityViewModel.showCaseBuilder(
                                this,
                                select_left_item_button_split,
                                getString(R.string.like_sides_title, "'Left'"),
                                getString(R.string.like_sides_description, "left")
                            )
                        )
                        .addShowCase(
                            homeActivityViewModel.showCaseBuilder(
                                this,
                                select_right_item_button_split,
                                getString(R.string.like_sides_title, "'Right'"),
                                getString(R.string.like_sides_description, "right")
                            )
                        )
                        .addShowCase(
                            homeActivityViewModel.showCaseBuilder(
                                this,
                                user_points,
                                getString(R.string.your_points),
                                getString(R.string.points_description)
                            )
                        )
                        .addShowCase(
                            homeActivityViewModel.showCaseBuilder(
                                this,
                                change_category_layout,
                                getString(R.string.categories_list),
                                getString(R.string.categories_list_description),
                                closeAction = {
                                    showNewMessagesDialog()
                                }
                            )
                        )
                        .show()
                },
                this
            )
            homeActivityViewModel.setWelcomeDialogIsShown()
            homeActivityViewModel.setAppTipIsShown()
        }
    }

    //initial setups
    private fun setupUserInfo() {
        category_avatar.load("${Constants.userImageUrl}${UserInfo.avatar}") {
            crossfade(true)
            diskCachePolicy(CachePolicy.ENABLED)
            allowHardware(false)
            transformations(CircleCropTransformation())
        }
        user_points_text.text = "★ " + UserInfo.points.toString()
    }

    private fun updateUserPointsAndHeaderTitle(_responseText: String) {
        _responseText.split("=").apply {
            header_title.text = this[1]
            homeActivityViewModel.updateUserPointsPref(this[0].toInt())
            user_points.likeAnimation()
            user_points_text.text = "★ " + UserInfo.points.toString()
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
                        homeActivityViewModel.setLastSelectedCategoryData(
                            -1,
                            -1,
                            "All"
                        )
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

    //api calls
    private fun getAllItems() {
        homeActivityViewModel.getAllItems().observe(this, Observer { networkResource ->
            when (networkResource.state) {
                State.LOADING -> {
                    showFullscreenProgress(true)
                    showProgress(true)
                }
                State.SUCCESS -> {
                    val status = networkResource.status
                    status?.let {
                        when (it) {
                            true -> {
                                showFullscreenProgress(false)
                                showProgress(false)
                                networkResource.data?.let { response ->
                                    current_category_text.text =
                                        homeActivityViewModel.getLastSelectedCategoryTitle()
                                    allItems = response.items as MutableList<Item>
                                    allPairs = response.finalPairs as MutableList<Any>
                                    //setUpItems()
                                    setUpItemsSplited()
                                }
                            }
                            false -> {
                                onNetworkFail(networkResource.message.toString())
                            }
                        }
                    }
                }
                State.ERROR -> {
                    onNetworkFail(networkResource.message.toString())
                }
            }
        })
    }

    private fun getCategories() {
        categoryBottomSheetViewModel.selectAllCategories()
            .observe(this, Observer { networkResource ->
                when (networkResource?.state) {
                    State.LOADING -> {
                        categoryBottomDialog.contentView.categories_retry_button.visibility =
                            View.GONE
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
                                    categoryBottomDialog.contentView.categories_retry_button.visibility =
                                        View.GONE
                                    networkResource.data?.let { response ->
                                        categoriesLoaded = true
                                        categoryBottomDialog.contentView.categories_recyclerview.run {
                                            this.visibility = View.VISIBLE
                                            categoriesAdapter = this.setUp<Category> {
                                                withLayoutManager(GridLayoutManager(context, 2))
                                                withLayoutResId(R.layout.recyclerview_list_category)
                                                withItems(response as MutableList<Category>)
                                                bindIndexed { category, index ->
                                                    category_avatar.load("${Constants.categoryImageUrl}${category.id}.png") {
                                                        crossfade(true)
                                                        placeholder(R.color.colorPrimaryDark)
                                                    }
                                                    category_title.text = category.title
                                                    category_new_badge.visibility = View.GONE
                                                    category_layout.setBackgroundResource(R.drawable.category_circular_item)
                                                    if (index == homeActivityViewModel.getLastSelectedCategoryIndex())
                                                        category_layout.setBackgroundResource(R.drawable.category_circular_item_selected)
                                                    if (category.is_new == 1) category_new_badge.visibility =
                                                        View.VISIBLE
                                                    setOnClickListener {
                                                        homeActivityViewModel.setLastSelectedCategoryData(
                                                            index,
                                                            category.id,
                                                            category.title
                                                        )
                                                        getItemsByCategory(category)
                                                        categoryBottomDialog.dismiss()
                                                    }
                                                }
                                            }
                                        }
                                        categoryBottomDialog.contentView.categories_retry_button.setOnClickListener {
                                            getCategories()
                                        }
                                    }
                                }
                                false -> {
                                    categoryBottomDialog.contentView.bottom_sheet_progress_bar.visibility =
                                        View.GONE
                                    categoryBottomDialog.contentView.categories_retry_button.visibility =
                                        View.VISIBLE
                                    /*Alerts.showBottomSheetErrorWithActionButton(
                                        msg = networkResource.message!!,
                                        actionPositiveTitle = getString(R.string.error_dialog_try_again_button_text),
                                        activity = this
                                    )*/
                                }
                            }
                        }
                    }
                    State.ERROR -> {
                        categoryBottomDialog.contentView.bottom_sheet_progress_bar.visibility =
                            View.GONE
                        categoryBottomDialog.contentView.categories_retry_button.visibility =
                            View.VISIBLE
                        /*Alerts.showBottomSheetErrorWithActionButton(
                            msg = networkResource.message!!,
                            actionPositiveTitle = getString(R.string.error_dialog_try_again_button_text),
                            activity = this
                        )*/
                    }
                }
            })
    }

    private fun getItemsByCategory(_category: Category) {
        if (this::categoriesAdapter.isInitialized) categoriesAdapter.notifyDataSetChanged()
        if (this::itemsAdapter.isInitialized)
            itemsAdapter update {
                it.removeAll(it)
                itemsAdapter.clear()
                allItems.clear()
                allPairs.clear()
            }
        homeActivityViewModel.getItemsByCategory(_category.id)
            .observe(this, Observer { networkResource ->
                when (networkResource.state) {
                    State.LOADING -> {
                        showProgress(true)
                        showFullscreenProgress(true)
                    }
                    State.SUCCESS -> {
                        val status = networkResource.status
                        status?.let {
                            when (it) {
                                true -> {
                                    showProgress(false)
                                    showFullscreenProgress(false)
                                    networkResource.data?.let { response ->
                                        current_category_text.text = _category.title
                                        allItems = response.items as MutableList<Item>
                                        allPairs = response.finalPairs as MutableList<Any>
                                        //setUpItems()
                                        setUpItemsSplited()
                                    }
                                }
                                false -> {
                                    onNetworkFail(networkResource.message.toString())
                                }
                            }
                        }
                    }
                    State.ERROR -> {
                        onNetworkFail(networkResource.message.toString())
                    }
                }
            })
    }

    private fun preloadImagesIntoMemory(_imageUrl: String, _target: ImageView): LoadRequest {
        return LoadRequest.Builder(this)
            .data(_imageUrl)
            .target(_target)
            .size(ViewSizeResolver(_target))
            .build()
    }

    //adapters and select handlers
    private fun setUpItems() {
        /*itemsAdapter = recyclerView.setUp<Any> {
            //withLayoutResId(R.layout.recyclerview_list_item)
            withLayoutResId(R.layout.recycleriew_list_item_splitted_view)
            withItems(allPairs)
            bindIndexed { pair, position ->
                with(pair as ArrayList<*>) {
                    (this[0] as Int).let { leftItemIndex ->
                        imageLoader(context).execute(
                            preloadImagesIntoMemory(
                                "${Constants.itemsImageUrl}${allItems[leftItemIndex].id}.jpg",
                                left_item_imageview_full
                            )
                        )
                        imageLoader(context).execute(
                            preloadImagesIntoMemory(
                                "${Constants.itemsImageUrl}${allItems[leftItemIndex].id}.jpg",
                                left_item_imageview
                            )
                        )
                        *//* left_item_imageview_full.load("${Constants.itemsImageUrl}${allItems[leftItemIndex].id}.jpg") {
                             placeholder(R.drawable.placeholder_trans)
                             diskCachePolicy(CachePolicy.ENABLED)
                             allowHardware(false)
                         }
                         left_item_imageview.load("${Constants.itemsImageUrl}${allItems[leftItemIndex].id}.jpg") {
                             crossfade(true)
                             placeholder(R.drawable.placeholder_trans)
                             allowHardware(false)
                             diskCachePolicy(CachePolicy.ENABLED)
                         }*//*
                    }
                    (this[1] as Int).let { rightItemIndex ->
                        imageLoader(context).execute(
                            preloadImagesIntoMemory(
                                "${Constants.itemsImageUrl}${allItems[rightItemIndex].id}.jpg",
                                right_item_imageview_full
                            )
                        )
                        imageLoader(context).execute(
                            preloadImagesIntoMemory(
                                "${Constants.itemsImageUrl}${allItems[rightItemIndex].id}.jpg",
                                right_item_imageview
                            )
                        )
                        *//*right_item_imageview_full.load("${Constants.itemsImageUrl}${allItems[rightItemIndex].id}.jpg") {
                            placeholder(R.drawable.placeholder_trans)
                            allowHardware(false)
                            diskCachePolicy(CachePolicy.ENABLED)
                        }
                        right_item_imageview.load("${Constants.itemsImageUrl}${allItems[rightItemIndex].id}.jpg") {
                            crossfade(true)
                            allowHardware(false)
                            placeholder(R.drawable.placeholder_trans)
                            diskCachePolicy(CachePolicy.ENABLED)
                        }*//*
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

                //zoom
                left_item_imageview.engine.addListener(object : ZoomEngine.Listener {
                    override fun onIdle(engine: ZoomEngine) {
                        Log.e("onIdle:", engine.toString())
                        // engine.zoomTo(1.0f,true)
                        engine.moveToCenter(1.0f, true)
                    }

                    override fun onUpdate(engine: ZoomEngine, matrix: Matrix) {
                        //Log.e("onUpdate: ", matrix.toShortString())
                    }

                })

                //gesture
                left_item_imageview.setOnTouchListener(object :
                    OnSwipeTouchListener(this@HomeActivity) {
                    override fun onSwipeUp() {
                        super.onSwipeUp()
                        categoryBottomDialog.show()
                    }

                    override fun onSwipeRight() {
                        super.onSwipeRight()

                        //left_item_imageview.zoomBy(1.5f, true)
                        //left_item_imageview.zoomIn()

                        *//*handleSelectedView(
                            "left",
                            allItems[pair[0] as Int].id,
                            position,
                            this@bindIndexed
                        )*//*
                    }

                    override fun onLongClick() {
                        super.onLongClick()
                        itemDetailsBottomDialog.show()
                        itemDetailsBottomDialog.contentView.run {
                            imageLoader(context).execute(
                                preloadImagesIntoMemory(
                                    "${Constants.itemsImageUrl}${allItems[pair[0] as Int].id}.jpg",
                                    this.item_logo
                                )
                            )
                            *//*this.item_logo.load("${Constants.itemsImageUrl}${allItems[pair[0] as Int].id}.jpg") {
                                crossfade(true)
                                placeholder(R.color.colorPrimary)
                                diskCachePolicy(CachePolicy.ENABLED)
                            }*//*
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
                            imageLoader(context).execute(
                                preloadImagesIntoMemory(
                                    "${Constants.itemsImageUrl}${allItems[pair[1] as Int].id}.jpg",
                                    this.item_logo
                                )
                            )
                            *//*this.item_logo.load("${Constants.itemsImageUrl}${allItems[pair[1] as Int].id}.jpg") {
                                crossfade(true)
                                placeholder(R.color.colorPrimary)
                                diskCachePolicy(CachePolicy.ENABLED)
                            }*//*
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
        }*/

        //disable vertical scroll
        /*recyclerView.layoutManager = object : LinearLayoutManager(applicationContext) {
            override fun canScrollVertically(): Boolean = false
        }*/
    }

    //adapters and select handlers
    private fun setUpItemsSplited() {
        itemsAdapter = recyclerView.setUp<Any> {
            withLayoutResId(R.layout.recycleriew_list_item_splitted_view)
            withItems(allPairs)
            bindIndexed { pair, position ->
                recyclerView.recycledViewPool.setMaxRecycledViews(0, 0)

                with(pair as ArrayList<*>) {
                    (this[0] as Int).let { leftItemIndex ->
                        left_item_imageview_split?.let {
                            it.load("${Constants.itemsImageUrl}${allItems[leftItemIndex].id}.jpg") {
                                crossfade(true)
                                //placeholder(R.drawable.placeholder_trans)
                                allowHardware(false)
                                diskCachePolicy(CachePolicy.ENABLED)
                            }
                        }
                    }
                    (this[1] as Int).let { rightItemIndex ->
                        right_item_imageview_split?.let {
                            it.load("${Constants.itemsImageUrl}${allItems[rightItemIndex].id}.jpg") {
                                crossfade(true)
                                allowHardware(false)
                                //placeholder(R.drawable.placeholder_trans)
                                diskCachePolicy(CachePolicy.ENABLED)
                            }
                        }

                    }
                }

                resetView(this)

                //like button single click
                select_right_item_button_split.setOnClickListener {
                    handleSelectedView(
                        "right",
                        allItems[pair[1] as Int].id,
                        position,
                        this@bindIndexed
                    )
                }

                select_left_item_button_split.setOnClickListener {
                    handleSelectedView(
                        "left",
                        allItems[pair[0] as Int].id,
                        position,
                        this@bindIndexed
                    )
                }

                //gesture
                left_item_imageview_split.setOnTouchListener(object :
                    OnSwipeTouchListener(this@HomeActivity) {
                    override fun onSwipeUp() {
                        super.onSwipeUp()
                        //categoryBottomDialog.show()
                    }

                    override fun onSwipeRight() {
                        super.onSwipeRight()
                        /*handleSelectedView(
                            "left",
                            allItems[pair[0] as Int].id,
                            position,
                            this@bindIndexed
                        )*/
                    }

                    override fun onLongClick() {
                        super.onLongClick()
                        itemDetailsBottomDialog.show()
                        itemDetailsBottomDialog.contentView.run {
                            imageLoader(context).execute(
                                preloadImagesIntoMemory(
                                    "${Constants.itemsImageUrl}${allItems[pair[0] as Int].id}.jpg",
                                    this.item_logo
                                )
                            )
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

                right_item_imageview_split.setOnTouchListener(object :
                    OnSwipeTouchListener(this@HomeActivity) {
                    override fun onSwipeUp() {
                        super.onSwipeUp()
                        // categoryBottomDialog.show()
                    }

                    override fun onSwipeLeft() {
                        super.onSwipeLeft()
                        /*handleSelectedView(
                            "right",
                            allItems[pair[1] as Int].id,
                            position,
                            this@bindIndexed
                        )*/
                    }

                    override fun onLongClick() {
                        super.onLongClick()
                        itemDetailsBottomDialog.show()
                        itemDetailsBottomDialog.contentView.run {
                            imageLoader(context).execute(
                                preloadImagesIntoMemory(
                                    "${Constants.itemsImageUrl}${allItems[pair[1] as Int].id}.jpg",
                                    this.item_logo
                                )
                            )
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
        var selectedSideFullFab = _selectedView.left_item_fab_full_split
        _selectedView.separator_split.visibility = View.GONE
        when (_selectedSide) {
            "right" -> {
                _selectedView.split_view_parent.maximizeRightContent()
                selectedSideFullFab = _selectedView.right_item_fab_full_split
            }
            "left" -> {
                _selectedView.split_view_parent.maximizeLeftContent()
                selectedSideFullFab = _selectedView.left_item_fab_full_split
            }
        }
        setSelectedItem(_selectedItemId, _position, _selectedView, selectedSideFullFab)
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

    private fun showFullscreenProgress(_show: Boolean) {
        when (_show) {
            true -> {
                activity_loading.visibility = View.VISIBLE
                home_progress.visibility = View.VISIBLE
                home_retry_button.visibility = View.GONE
                enableDisableInnerViews(false)
            }
            false -> {
                enableDisableInnerViews(true)
                activity_loading.visibility = View.GONE
            }
        }
    }

    private fun disableEnableControls(enable: Boolean, vg: ViewGroup) {
        vg.isEnabled = enable
        for (i in 0 until vg.childCount) {
            val child = vg.getChildAt(i)
            if (child.id != R.id.home_retry_button) {
                child.isEnabled = enable
                child.isClickable = enable
                if (child is ViewGroup) {
                    disableEnableControls(enable, child)
                }
            }
        }
    }

    private fun enableDisableInnerViews(enable: Boolean) {
        when (enable) {
            true -> {
                change_category_layout.isEnabled = true
                change_category_layout.isClickable = true
                more.isEnabled = true
                more.isClickable = true
                recyclerView.isEnabled = true
                recyclerView.isClickable = true
            }
            false -> {
                change_category_layout.isEnabled = false
                change_category_layout.isClickable = false
                more.isEnabled = false
                more.isClickable = false
                recyclerView.isEnabled = false
                recyclerView.isClickable = false
            }
        }
    }

    private fun setSelectedItem(
        _selectedItemId: Int,
        _position: Int,
        _selectedView: View,
        _selectedSideFullFab: View
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
                                    likeAnimationAndContinue(
                                        networkResource.data as String,
                                        _position, _selectedSideFullFab
                                    )
                                }
                                false -> {
                                    resetView(_selectedView)
                                    Alerts.showBottomSheetErrorWithActionButton(
                                        msg = networkResource.message!!,
                                        actionPositiveTitle = getString(R.string.error_dialog_try_again_button_text),
                                        activity = this
                                    )
                                }
                            }
                        }
                    }
                    State.ERROR -> {
                        resetView(_selectedView)
                        Alerts.showBottomSheetErrorWithActionButton(
                            msg = networkResource.message!!,
                            actionPositiveTitle = getString(R.string.error_dialog_try_again_button_text),
                            activity = this
                        )
                    }
                }
            })
    }

    private fun likeAnimationAndContinue(
        _netResponse: String, _position: Int,
        _selectedSideFullFab: View
    ) {
        showProgress(false)
        updateUserPointsAndHeaderTitle(_netResponse)
        (_selectedSideFullFab as SmallBangView).likeAnimation(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}
            override fun onAnimationEnd(p0: Animator?) {
                updateAdapter(_position)
            }

            override fun onAnimationCancel(p0: Animator?) {}
            override fun onAnimationStart(p0: Animator?) {}
        })
    }

    private fun resetView(_view: View) {
        App.logger.error("resetView $_view")
        showProgress(false)
        header_title.text = getString(R.string.which_one)
        _view.separator_split.visibility = View.VISIBLE
        no_more_items_layout.visibility = View.GONE
        /*    val params = _view.split_view_parent.left_item_layout.layoutParams as LinearLayout.LayoutParams
            params.weight = 1f
            _view.split_view_parent.left_item_layout.layoutParams = params
        App.logger.error("left_item_layout ${params.weight}")

            val params2 = _view.split_view_parent.right_item_layout.layoutParams as LinearLayout.LayoutParams
            params2.weight = 1f
            _view.split_view_parent.right_item_layout.layoutParams = params2
        App.logger.error("right_item_layout ${params2.weight}")*/
    }

    private fun updateAdapter(_position: Int) {
        // recyclerView.postDelayed({
            itemsAdapter update {
                it.removeAt(_position)
                //itemsAdapter.notifyItemRemoved(_position)
                itemsAdapter.notifyItemRangeChanged(_position, it.size)
                //itemsAdapter.notifyDataSetChanged()
                if (it.isEmpty()) {
                    header_title.text = getString(R.string.no_more_items)
                    no_more_items_layout.visibility = View.VISIBLE
                }
            }
        //}, 800)
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
    }

    override fun networkStatus(state: Boolean) {
        if (state) callHomeActivityApi()
    }

    private fun callHomeActivityApi() {
        setupUserInfo()
        if (homeActivityViewModel.getLastSelectedCategoryIndex() == -1)
            getAllItems()
        else getItemsByCategory(homeActivityViewModel.getLastSelectedCategoryObject())
    }

    private fun onNetworkFail(errorMessage: String) {
        home_progress.visibility = View.GONE
        home_retry_button.visibility = View.VISIBLE
        home_retry_button.isEnabled = true
        //call error dialog from parent
        handleNetworkErrors(errorMessage)
    }

    //FCM
    private fun generateFCMToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    App.logger.error("getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
            })
    }
}