/*
 * Creator: Kamran Noorinejad on 5/17/20 10:47 AM
 * Last modified: 5/17/20 10:47 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.viewmodels

import android.os.Build
import android.os.Build.MANUFACTURER
import android.os.Build.MODEL
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.chibatching.kotpref.blockingBulk
import com.kmno.leftorite.core.Constants.apiKey
import com.kmno.leftorite.data.model.User
import com.kmno.leftorite.data.repository.AuthRepository
import com.kmno.leftorite.utils.UserInfo
import com.kmno.leftorite.utils.UserInfo.loggedIn
import kotlinx.coroutines.Dispatchers
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

/**
 * Created by Kamran Noorinejad on 5/17/2020 AD 10:47.
 * Edited by Kamran Noorinejad on 5/17/2020 AD 10:47.
 */
class AuthActivityViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun setLoggedInPref(_loggedIn: Boolean) {
        loggedIn = _loggedIn
    }

    //sign in
    fun signInUser(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(authRepository.signInUser(email, password))
    }

    //sign up user
    fun signUpUser(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(
            authRepository.signUpUser(
                email,
                password,
                "ANDROID-${Build.VERSION.RELEASE}",
                getDeviceName(),
                getLocalIpAddress(),
                apiKey
            )
        )
    }

    fun storeUserPrefs(user: User) {
        try {
            UserInfo.blockingBulk {
                id = user.id
                nickname = user.nickname
                email = user.email
                points = user.points
                token = user.token
                avatar = user.avatar_id
                lastLoginDate = user.last_login_date
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
        }).capitalize(Locale.ROOT)

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