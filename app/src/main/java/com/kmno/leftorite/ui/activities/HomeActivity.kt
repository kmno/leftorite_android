/*
 * Creator: Kamran Noorinejad on 5/13/20 12:54 PM
 * Last modified: 5/13/20 12:53 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.activities

import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.vove7.bottomdialog.BottomDialog
import cn.vove7.bottomdialog.StatusCallback
import cn.vove7.bottomdialog.toolbar
import coil.api.load
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
import com.link184.kidadapter.setUp
import com.link184.kidadapter.simple.SingleKidAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item_intent.view.*
import kotlinx.android.synthetic.main.modal_bottom_sheet.view.*
import kotlinx.android.synthetic.main.recyclerview_list_item.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {

    private val homeActivityViewModel: HomeActivityViewModel by viewModel()

    private lateinit var adapter: SingleKidAdapter<Any>
    private var allItems = emptyList<Item>()
    private lateinit var bb: BottomDialog

    var timeToLive: Long = 2000
    var minSpeed: Float = 4f
    var maxSpeed: Float = 7f
    var colors =
        intArrayOf(R.color.lt_yellow, R.color.lt_orange, R.color.lt_purple, R.color.lt_pink)

    override fun getResId(): Int {
        return R.layout.activity_home
    }

    override fun afterCreate() {

        setUpScreen()

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
                                networkResource.data?.let { it ->
                                    current_category_text.text = "Current Category : All"
                                    allItems = it.items
                                    setUpItems(it.finalPairs as MutableList<Any>)
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

        change_category_layout.setOnClickListener {

            bb = BottomDialog.builder(this) {
                toolbar {
                    title = getString(R.string.select_category_title)
                    round = true
                    navIconId = R.drawable.ic_arrow_down
                    onIconClick = {
                        it.dismiss()
                    }
                }
                theme(R.style.BottomDialog_Dark)
                content(MarkdownContentBuilder(true))
            }

            bb.listenStatus(object : StatusCallback {
                override fun onExpand() {
                    super.onExpand()
                    App.logger.error("onExpand")
                }

                override fun onCollapsed() {
                    App.logger.error("onCollapsed")
                    super.onCollapsed()
                    getCategories()
                }

                override fun onSlide(slideOffset: Float) {}
            })
            bb.show()
        }

    }

    private fun getCategories() {
        homeActivityViewModel.getCategories().observe(this, Observer { networkResource ->
            when (networkResource.state) {
                State.LOADING -> {
                    bb.contentView.progressBar.visibility = View.VISIBLE
                }
                State.SUCCESS -> {
                    val status = networkResource.status
                    status?.let {
                        when (it) {
                            true -> {
                                bb.contentView.progressBar.visibility = View.GONE
                                networkResource.data?.let { response ->
                                    App.logger.error(response.toString())
                                    bb.contentView.recycleriew.run {
                                        this.visibility = View.VISIBLE
                                        this.setUp<Category> {
                                            withLayoutManager(GridLayoutManager(context, 2))
                                            withLayoutResId(R.layout.item_intent)
                                            withItems(response as MutableList<Category>)
                                            bindIndexed { category, position ->
                                                icon.setImageResource(R.mipmap.ic_launcher)
                                                text.text = category.title
                                                setOnClickListener {
                                                    App.logger.error(category.id.toString())
                                                    bb.dismiss()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            false -> {
                                bb.contentView.progressBar.visibility = View.GONE
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
                    bb.contentView.progressBar.visibility = View.GONE
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

    private fun setUpItems(
        finalPairs: MutableList<Any>
    ) {
        adapter = recyclerView.setUp<Any> {
            withLayoutResId(R.layout.recyclerview_list_item)
            withItems(finalPairs)
            bindIndexed { item, position ->

                with(item as ArrayList<*>) {
                    left_item_imageview.load("${Constants.itemsImageUrl}${this[0]}.jpg") {
                        crossfade(true)
                        placeholder(R.color.colorPrimaryDark)
                    }
                    right_item_imageview.load("${Constants.itemsImageUrl}${this[1]}.jpg") {
                        crossfade(true)
                        placeholder(R.color.colorPrimary)
                    }
                    right_item_imageview_full.load("${Constants.itemsImageUrl}${this[1]}.jpg")
                    left_item_imageview_full.load("${Constants.itemsImageUrl}${this[0]}.jpg")
                }

                resetView(this)

                //button single click
                select_right_item_button.setOnClickListener {
                    handleSelectedView(
                        "right",
                        item[1] as Int,
                        position,
                        this@bindIndexed
                    )
                }

                select_left_item_button.setOnClickListener {
                    handleSelectedView(
                        "left",
                        item[0] as Int,
                        position,
                        this@bindIndexed
                    )
                }

                //double click
                right_item_imageview.setOnClickListener(
                    DoubleTapListener(
                        callback = object : DoubleTapListener.Callback {
                            override fun doubleClicked() {
                                App.logger.error("doubleClicked ---------> right_item_imageview + item id =${item[1]}")
                                handleSelectedView(
                                    "right",
                                    item[1] as Int,
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
                                item[0] as Int,
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
        _selectedView.separator.visibility = View.GONE
        _selectedView.right_item_imageview.visibility = View.GONE
        _selectedView.left_item_imageview.visibility = View.GONE
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
                                    //Alerts.dismissProgressFlashbar()
                                    header_title.text = networkResource.data as String
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
        _view.separator.visibility = View.VISIBLE
        _view.right_item_imageview.visibility = View.VISIBLE
        _view.left_item_imageview.visibility = View.VISIBLE
        _view.right_item_imageview_full.visibility = View.GONE
        _view.left_item_imageview_full.visibility = View.GONE

    }

    private fun updateAdapter(_position: Int) {
        recyclerView.postDelayed({
            adapter update {
                it.removeAt(_position)
                //    adapter.remove(position)
                adapter.notifyItemRemoved(_position)
                adapter.notifyItemRangeChanged(_position, it.size)
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

    }

}