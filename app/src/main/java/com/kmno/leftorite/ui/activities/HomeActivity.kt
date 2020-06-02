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
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.kmno.leftorite.App
import com.kmno.leftorite.R
import com.kmno.leftorite.data.Constants
import com.kmno.leftorite.data.api.State
import com.kmno.leftorite.data.model.Item
import com.kmno.leftorite.ui.base.BaseActivity
import com.kmno.leftorite.ui.viewmodels.HomeActivityViewModel
import com.kmno.leftorite.utils.Alerts
import com.kmno.leftorite.utils.DoubleTapListener
import com.link184.kidadapter.setUp
import com.link184.kidadapter.simple.SingleKidAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.recyclerview_list_item.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {

    private val homeActivityViewModel: HomeActivityViewModel by viewModel()

    //private lateinit var adapter: SingleKidAdapter<Item>
    private lateinit var adapter: SingleKidAdapter<Any>

    override fun getResId(): Int {
        return R.layout.activity_home
    }

    override fun afterCreate() {

        makeUIFullscreen()

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
                                    iterateItems(
                                        it.items as MutableList<Item>,
                                        it.finalPairs as MutableList<Any>
                                    )
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

    private fun makeUIFullscreen() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = /*getColor(R.color.statusBarTransparentColor)*/Color.TRANSPARENT
    }

    private fun iterateItems(
        items: MutableList<Item>,
        finalPairs: MutableList<Any>
    ) {

        /*   adapter = recyclerView.setUp<Item> {
               withLayoutResId(R.layout.recyclerview_list_item)
               withItems(items)
               bindIndexed { item, position ->
                   left_item_imageview.load("${Constants.itemsImageUrl}${item.id}.jpg"){
                       crossfade(true)
                       placeholder(R.color.colorPrimaryDark)
                   }
                   right_item_imageview.load("${Constants.itemsImageUrl}${item.id+1}.jpg") {
                       crossfade(true)
                       placeholder(R.color.colorPrimary)
                   }

                   //double click
                   right_item_imageview.setOnClickListener ( DoubleTapListener(
                       callback = object : DoubleTapListener.Callback {
                           override fun doubleClicked() {
                               App.logger.error("doubleClicked ---------> right_item_imageview")
                               updateAdapter(position)
                           }
                       }
                   ))

                   left_item_imageview.setOnClickListener ( DoubleTapListener(
                       callback = object : DoubleTapListener.Callback {
                           override fun doubleClicked() {
                               App.logger.error("doubleClicked ---------> left_item_imageview")
                               updateAdapter(position)
                           }
                       }
                   ))

                   setOnClickListener {
                       Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                   }
               }
           }*/

        adapter = recyclerView.setUp<Any> {
            withLayoutResId(R.layout.recyclerview_list_item)
            withItems(finalPairs)
            bindIndexed { item, position ->
                App.logger.error("${item::class.simpleName}")
                with(item as ArrayList<*>) {

                    App.logger.error("${Constants.itemsImageUrl}${this[0]}.jpg")
                    App.logger.error("${Constants.itemsImageUrl}${this[1]}.jpg")

                    left_item_imageview.load("${Constants.itemsImageUrl}${this[0]}.jpg") {
                        crossfade(true)
                        placeholder(R.color.colorPrimaryDark)
                    }
                    right_item_imageview.load("${Constants.itemsImageUrl}${this[1]}.jpg") {
                        crossfade(true)
                        placeholder(R.color.colorPrimary)
                    }
                }


                //double click
                right_item_imageview.setOnClickListener(DoubleTapListener(
                    callback = object : DoubleTapListener.Callback {
                        override fun doubleClicked() {
                            App.logger.error("doubleClicked ---------> right_item_imageview")
                            updateAdapter(position)
                        }
                    }
                ))

                left_item_imageview.setOnClickListener(DoubleTapListener(
                    callback = object : DoubleTapListener.Callback {
                        override fun doubleClicked() {
                            App.logger.error("doubleClicked ---------> left_item_imageview")
                            updateAdapter(position)
                        }
                    }
                ))

                setOnClickListener {
                    Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        //disable vertical scroll
        recyclerView.layoutManager = object : LinearLayoutManager(applicationContext) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    private fun updateAdapter(position: Int) {
        recyclerView.postDelayed({ adapter.remove(position) }, 2000)

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