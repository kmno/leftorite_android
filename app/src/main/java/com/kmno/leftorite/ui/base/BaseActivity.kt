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
import com.kmno.leftorite.App
import com.kmno.leftorite.R
import com.kmno.leftorite.utils.NetworkInfo
import org.aviran.cookiebar2.CookieBar


abstract class BaseActivity : AppCompatActivity(),
    NetworkInfo.NetworkInfoListener {

    companion object {
        var isNetworkAvailable: Boolean = false
    }

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
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun onNetConnected() {
        isNetworkAvailable = true
        networkStatus(true)
        App.logger.error("onNetConnected")

    }

    fun onNetDisConnected() {
        isNetworkAvailable = false
        networkStatus(false)
        App.logger.error("onNetDisConnected")
        CookieBar.build(this@BaseActivity)
            .setTitle("No Network!")
            .setMessage("check your internet connection...")
            .setBackgroundColor(R.color.error)
            .show()
    }
}

