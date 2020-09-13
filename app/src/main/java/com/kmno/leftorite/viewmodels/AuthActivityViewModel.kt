/*
 * Creator: Kamran Noorinejad on 5/17/20 10:47 AM
 * Last modified: 5/17/20 10:47 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.viewmodels

import android.content.Context
import android.os.Build
import android.os.Build.MANUFACTURER
import android.os.Build.MODEL
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.chibatching.kotpref.blockingBulk
import com.kmno.leftorite.R
import com.kmno.leftorite.core.Constants.apiKey
import com.kmno.leftorite.data.api.ApiClientProvider
import com.kmno.leftorite.data.api.Resource
import com.kmno.leftorite.data.model.User
import com.kmno.leftorite.utils.UserInfo
import com.kmno.leftorite.utils.UserInfo.loggedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

/**
 * Created by Kamran Noorinejad on 5/17/2020 AD 10:47.
 * Edited by Kamran Noorinejad on 5/17/2020 AD 10:47.
 */
class AuthActivityViewModel(private val context: Context, apiProvider: ApiClientProvider) :
    ViewModel() {

    private val api = apiProvider.createApiClient()

    init {
        var test = CoroutineScope(Dispatchers.IO).launch {

        }
    }

    fun setLoggedInPref(_loggedIn: Boolean) {
        loggedIn = _loggedIn
    }

    //sign in
    fun signInUser(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.signInUser(email, password)
            if (response.isSuccessful) {
                emit(
                    Resource.success(
                        response.body()?.status,
                        response.body()?.response?.message,
                        response.body()?.response?.data
                    )
                )
            } else {
                emit(
                    Resource.error(
                        response.body()?.status,
                        response.body()?.response?.message,
                        null
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                Resource.error(
                    false,
                    context.getString(R.string.network_error_text),
                    null
                )
            )
        }

    }

    //signup
    fun signUpUser(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        try {
            val response = api.signUpUser(
                email,
                password,
                "ANDROID-${Build.VERSION.RELEASE}",
                getDeviceName(),
                getLocalIpAddress(),
                apiKey
            )
            if (response.isSuccessful) {
                emit(
                    Resource.success(
                        response.body()?.status,
                        response.body()?.response?.message,
                        response.body()?.response?.data
                    )
                )
            } else {
                emit(
                    Resource.error(
                        response.body()?.status,
                        response.body()?.response?.message,
                        null
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(
                Resource.error(
                    false,
                    context.getString(R.string.network_error_text),
                    null
                )
            )
        }

    }

    fun storeUserPrefs(user: User) {
        try {
            UserInfo.blockingBulk {
                id = user.id
                nickname = user.nickname
                email = user.email
                points = user.points
                token = user.token
                //avatar = "default_avatar_${(1..4).random()}"
                avatar = user.avatar_id
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getDeviceName(): String =
        (if (MODEL.startsWith(MANUFACTURER, ignoreCase = true)) {
            MODEL
        } else {
            "$MANUFACTURER $MODEL"
        }).capitalize()

    private fun getLocalIpAddress(): String {
        try {
            val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf: NetworkInterface = en.nextElement()
                val enumIpAddr: Enumeration<InetAddress> = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress: InetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        return inetAddress.hostAddress.toString()
                    }
                }
            }
        } catch (ex: java.lang.Exception) {
            Log.e("IP Address", ex.toString())
        }
        return "unknown"
    }
}