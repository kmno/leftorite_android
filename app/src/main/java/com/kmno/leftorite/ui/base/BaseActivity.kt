/*
 * Creator: Kamran Noorinejad on 5/13/20 11:55 AM
 * Last modified: 5/13/20 11:55 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.base

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.dsl.extension.requestPermissions
import com.kmno.leftorite.App
import com.kmno.leftorite.R
import com.kmno.leftorite.utils.Alerts.Companion.dismissFlashbar
import com.kmno.leftorite.utils.Alerts.Companion.showFlashbar


abstract class BaseActivity : AppCompatActivity() {

    companion object {
        var isNetworkAvailable: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getResId())
        afterCreate()
    }

    protected fun requestPermission() {
        requestPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) {
            requestCode = 4
            resultCallback = {
                when (this) {
                    is PermissionResult.PermissionGranted -> {
                        App.logger.error("Add your logic here after user grants permission(s)")
                    }
                    is PermissionResult.PermissionDenied -> {
                        // showAlert("Sorry!", "We Need this Permission ...","OK")
                    }
                    is PermissionResult.PermissionDeniedPermanently -> {
                        //Add your logic here if user denied permission(s) permanently.
                        //Ideally you should ask user to manually go to settings and enable permission(s)
                        //showAlert("Sorry!", "We Need this Permission ...","OK")
                    }
                    is PermissionResult.ShowRational -> {
                        //If user denied permission frequently then she/he is not clear about why you are asking this permission.
                        //This is your chance to explain them why you need permission.
                        // showAlert("Sorry!", "We Need this Permission ...","OK")
                    }
                }
            }
        }
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
        dismissFlashbar()
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
        showFlashbar(R.color.error, R.string.no_network, R.string.no_network_desc, 0, this)
    }



}
