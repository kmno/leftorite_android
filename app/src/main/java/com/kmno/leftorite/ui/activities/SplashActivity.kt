/*
 * Creator: Kamran Noorinejad on 5/13/20 12:54 PM
 * Last modified: 5/13/20 12:53 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.activities

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.Observer
import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.State
import com.kmno.leftorite.ui.base.BaseActivity
import com.kmno.leftorite.utils.Alerts
import com.kmno.leftorite.utils.ConfigPref
import com.kmno.leftorite.utils.launchActivity
import com.kmno.leftorite.viewmodels.SplashActivityViewModel
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.viewmodel.ext.android.viewModel


class SplashActivity : BaseActivity() {

    private val splashActivityViewModel: SplashActivityViewModel by viewModel()

    override fun getResId(): Int {
        return R.layout.activity_splash
    }

    override fun afterCreate() {

        setUpScreen()

        setStatusBarTextColorWhite()

        with(splashActivityViewModel.appVersionText) {
            app_version_text.text = this
        }

        retry_button.setOnClickListener {
            callInitialConfigApi()
        }
    }

    override fun ready() {
    }

    private fun goToDestinationActivity(_loggedIn: Boolean) {
        when (_loggedIn) {
            true -> {
                this.launchActivity<HomeActivity>(finish = true)
            }
            false -> {
                this.launchActivity<AuthActivity>(finish = true)
            }
        }
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
    }

    private fun callInitialConfigApi() {
        lazy_loader_progress.visibility = View.VISIBLE
        retry_button.visibility = View.GONE

        splashActivityViewModel.getInitialConfig().observe(this,
            Observer { networkResource ->
                when (networkResource?.state) {
                    State.LOADING -> {
                        lazy_loader_progress.visibility = View.VISIBLE
                    }
                    State.SUCCESS -> {
                        val status = networkResource.status
                        status?.let {
                            when (it) {
                                true -> {
                                    networkResource.data?.let { response ->
                                        splashActivityViewModel.storeConfigPrefs(response)
                                    }
                                    if (!splashActivityViewModel.checkIfNewVersionAvailable())
                                        goToDestinationActivity(splashActivityViewModel.isUserLoggedIn)
                                    else {
                                        lazy_loader_progress.visibility = View.GONE
                                        Alerts.showVersionAlertDialogWithTwoActionButton(
                                            title = getString(
                                                R.string.new_version_dialog_title,
                                                ConfigPref.new_version_title
                                            ),
                                            msg = ConfigPref.new_version_changelog,
                                            actionPositiveCallback = {
                                                val intent = Intent(Intent.ACTION_VIEW)
                                                intent.data =
                                                    Uri.parse("market://details?id=${application.packageName}")
                                                startActivity(intent)
                                            },
                                            actionNegativeCallback = {
                                                goToDestinationActivity(splashActivityViewModel.isUserLoggedIn)
                                            },
                                            activity = this
                                        )
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

    private fun onNetworkFail(errorMessage: String) {
        lazy_loader_progress.visibility = View.GONE
        retry_button.visibility = View.VISIBLE
        //call error dialog from parent
        handleNetworkErrors(errorMessage, ::callInitialConfigApi)
    }

    override fun networkStatus(state: Boolean) {
        if (state) {
            callInitialConfigApi()
        }
    }

}