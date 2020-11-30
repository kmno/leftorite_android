/*
 * Creator: Kamran Noorinejad on 5/13/20 11:55 AM
 * Last modified: 5/13/20 11:55 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.base

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.eazypermissions.common.model.PermissionResult
import com.eazypermissions.dsl.extension.requestPermissions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.kmno.leftorite.R
import com.kmno.leftorite.core.App
import com.kmno.leftorite.utils.Alerts
import com.kmno.leftorite.utils.Alerts.dismissFlashbar
import com.kmno.leftorite.utils.Alerts.showFlashbar
import com.xoxoer.lifemarklibrary.Lifemark
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
abstract class BaseActivity : AppCompatActivity() {

    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
    companion object {
        var isNetworkAvailable: Boolean = false
        lateinit var networkConnection: Lifemark
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getResId())

        afterCreate()

        checkPlayServices()

        val root = window.decorView
        val vto = root.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                root
                    .viewTreeObserver
                    .removeOnGlobalLayoutListener(this)
                ready()
            }
        })

        //network state observable
        networkConnection = Lifemark(applicationContext)
        networkConnection.ObservableNetworkCondition()
            .observe(this, { isConnected ->
                if (isConnected) {
                    onNetConnected()
                } else {
                    onNetDisConnected()
                }
            })

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

    /* private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
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
     }*/

    abstract fun getResId(): Int

    abstract fun afterCreate()

    abstract fun ready()

    abstract fun resume()

    abstract fun pause()

    abstract fun destroy()

    fun handleNetworkErrors(errorMessage: String, retryFunctionRef: () -> Unit = {}) {
        Alerts.showBottomSheetErrorWithActionButton(
            msg = errorMessage,
            actionPositiveTitle = getString(R.string.error_dialog_try_again_button_text),
            actionPositiveCallback = retryFunctionRef,
            activity = this
        )
    }

    abstract fun networkStatus(state: Boolean)

    override fun onStart() {
        super.onStart()
        //registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onResume() {
        super.onResume()
        checkPlayServices()
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
        //unregisterReceiver(broadcastReceiver)
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
        showFlashbar(R.color.error, R.string.no_network, R.string.no_network_desc, 0, this)
    }

    fun setUpScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
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

    fun setStatusBarTextColorWhite() {
        val decorView = this.window.decorView
        var systemUiVisibilityFlags = decorView.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            systemUiVisibilityFlags =
                systemUiVisibilityFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        decorView.systemUiVisibility = systemUiVisibilityFlags
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(
                    this, resultCode,
                    PLAY_SERVICES_RESOLUTION_REQUEST
                )
                    .show()
            } else {
                finish()
            }
            return false
        }
        return true
    }
}
