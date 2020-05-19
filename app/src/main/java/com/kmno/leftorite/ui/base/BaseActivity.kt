/*
 * Creator: Kamran Noorinejad on 5/13/20 11:55 AM
 * Last modified: 5/13/20 11:55 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andrognito.flashbar.Flashbar
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import com.kmno.leftorite.App
import com.kmno.leftorite.R


abstract class BaseActivity : AppCompatActivity() {

    companion object {
        var isNetworkAvailable: Boolean = false
    }

    var flashbar: Flashbar? = null
    var flashbarConfig: Flashbar.Builder? = null
    var alert: AlertView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getResId())
        afterCreate()
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected = intent.getBooleanExtra(
                ConnectivityManager
                    .EXTRA_NO_CONNECTIVITY, false
            )
            if (notConnected) {
                onNetDisConnected()
            } else {
                onNetConnected()
            }
        }
    }

    abstract fun getResId(): Int

    abstract fun afterCreate()

    abstract fun resume()

    abstract fun pause()

    abstract fun destroy()

    abstract fun networkStatus(state: Boolean)

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onResume() {
        super.onResume()
        resume()
    }

    override fun onPause() {
        super.onPause()
        pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroy()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
        flashbar?.dismiss()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun onNetConnected() {
        isNetworkAvailable = true
        networkStatus(true)
        App.logger.error("onNetConnected")
        dismissFlashbar()
    }

    fun onNetDisConnected() {
        isNetworkAvailable = false
        networkStatus(false)
        App.logger.error("onNetDisConnected")
        // val alert = AlertView("No Network!", "check your internet connection...", AlertStyle.BOTTOM_SHEET)
        // alert.show(this)
        showFlashbar(R.color.error, R.string.no_network, R.string.no_network_desc, 0)
    }

    fun showFlashbar(bgColor: Int, title: Int, msg: Int, duration: Int) {
        dismissFlashbar()
        flashbarConfig = Flashbar.Builder(this)
            .gravity(Flashbar.Gravity.TOP)
            .backgroundColorRes(bgColor)
            .messageSizeInSp(16f)
            .showIcon()
            .title(title)
            .message(msg)
            .castShadow(false)

        if (duration != 0) flashbarConfig?.duration(((duration * 1000).toLong()))
        flashbar = flashbarConfig?.build()
        flashbar?.show()
    }

    fun dismissFlashbar() {
        if (flashbar != null)
            if (flashbar!!.isShown()) flashbar?.dismiss()
    }

    fun showAlert(title: String, msg: String, action: String) {
        alert = AlertView(title, msg, AlertStyle.DIALOG)
        // positiveAction.let {
        //   alert?.addAction(AlertAction(positiveAction!!, AlertActionStyle.DEFAULT) { action -> })
        // }
        //negativeAction.let {
        alert?.addAction(AlertAction(action, AlertActionStyle.NEGATIVE) { action -> })
        //  }
        alert?.show(this)
    }
}
