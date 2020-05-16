/*
 * Creator: Kamran Noorinejad on 5/16/20 3:13 PM
 * Last modified: 5/16/20 3:13 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.utils

/**
 * Created by Kamran Noorinejad on 5/16/2020 AD 15:13.
 * Edited by Kamran Noorinejad on 5/16/2020 AD 15:13.
 */

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.kmno.leftorite.App
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NetworkInfo(private val context: Context) : BroadcastReceiver() {

    private lateinit var network: Network

    // collection of listeners
    private val listeners = mutableSetOf<NetworkInfoListener>()

    // constructor
    init {
        context.registerReceiver(this, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        network = Network()
    }

    // receive network changes
    override fun onReceive(context: Context, intent: Intent) = runBlocking {
        App.logger.warn("onReceive")
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        val job = launch {
            // verify network availability
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
                App.logger.warn("Network available")
                // verify internet access
                if (hostAvailable("google.com", 80)) {
                    // internet access
                    App.logger.warn("Internet Access Detected")
                    network.status = NetworkStatus.INTERNET
                } else {
                    // no internet access
                    App.logger.warn("Unable to access Internet nor Nauta Mail")
                    network.status = NetworkStatus.OFFLINE
                }
                // get network type
                when (activeNetwork.type) {
                    ConnectivityManager.TYPE_MOBILE -> {
                        // mobile network
                        App.logger.warn("Connectivity: MOBILE")
                        network.type = NetworkType.MOBILE
                    }
                    ConnectivityManager.TYPE_WIFI -> {
                        // wifi network
                        App.logger.warn("Connectivity: WIFI")
                        network.type = NetworkType.WIFI
                    }
                    else -> {
                        // no network available
                        App.logger.warn("Network not available")
                        network.type = NetworkType.NONE
                    }
                }
            } else {
                // no network available
                App.logger.warn("Network not available")
                network.type = NetworkType.NONE
                network.status = NetworkStatus.OFFLINE
            }
        }
        job.join()
        notifyNetworkChangeToAll()
    }

    // verify host availability
    private fun hostAvailable(host: String, port: Int): Boolean {
        App.logger.warn("Verifying host availability: $host:$port")
        try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(host, port), 2000)
                socket.close()
                // host available
                App.logger.warn("Host: $host:$port is available")
                return true
            }
        } catch (e: IOException) {
            // host unreachable or timeout
            App.logger.warn("Host: $host:$port is not available")
            return false
        }
    }

    // notify network change to all listeners
    private fun notifyNetworkChangeToAll() {
        App.logger.warn("notifyStateToAll")
        for (listener in listeners) {
            notifyNetworkChange(listener)
        }
    }

    // notify network change
    private fun notifyNetworkChange(listener: NetworkInfoListener) {
        App.logger.warn("notifyState")
        listener.networkStatusChange(network)
    }

    // add a listener
    fun addListener(listener: NetworkInfoListener) {
        App.logger.warn("addListener")
        listeners.add(listener)
        notifyNetworkChange(listener)
    }

    // remove a listener
    fun removeListener(listener: NetworkInfoListener) {
        App.logger.warn("removeListener")
        listeners.remove(listener)
    }

    // get current network information
    fun getNetwork(): Network {
        return network
    }

    // static content
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var ns: NetworkInfo? = null

        // get a singleton
        fun getInstance(ctx: Context): NetworkInfo {
            if (ns == null) {
                ns = NetworkInfo(ctx.applicationContext)
            }
            return ns as NetworkInfo
        }
    }

    // interface that represent the [NetworkStatusListener]
    interface NetworkInfoListener {
        fun networkStatusChange(network: Network)
    }

    data class Network(
        var type: NetworkType = NetworkType.NONE,
        var status: NetworkStatus = NetworkStatus.OFFLINE
    )

    enum class NetworkType { NONE, WIFI, MOBILE }

    enum class NetworkStatus { OFFLINE, INTERNET }
}