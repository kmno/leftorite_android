/*
 * Creator: Kamran Noorinejad on 5/13/20 12:54 PM
 * Last modified: 5/13/20 12:53 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.activities

import android.os.Handler
import android.view.View
import com.kmno.leftorite.R
import com.kmno.leftorite.core.App
import com.kmno.leftorite.ui.base.BaseActivity
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
        with(splashActivityViewModel.appVersionText) {
            app_version_text.text = this
        }

        with(splashActivityViewModel.isUserLoggedIn) {
            // goToDestinationActivity(this)
        }

    }

    override fun ready() {}

    private fun goToDestinationActivity(_loggedIn: Boolean) {
        if (isNetworkAvailable) {
            // splash_progress_bar.visibility = View.VISIBLE
            lazy_loader_progress.visibility = View.VISIBLE
            when (_loggedIn) {
                true -> {
                    App.logger.error("HomeActivity")
                    Handler().postDelayed({
                        this.launchActivity<HomeActivity>(finish = true)
                    }, 2000)
                }
                false -> {
                    App.logger.error("AuthActivity")
                    Handler().postDelayed({
                        this.launchActivity<AuthActivity>(finish = true)
                    }, 2000)
                }
            }
        } else {
            lazy_loader_progress.visibility = View.GONE
            //   splash_progress_bar.visibility = View.GONE
        }
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
    }

    override fun networkStatus(state: Boolean) {
        if (state) goToDestinationActivity(splashActivityViewModel.isUserLoggedIn)
    }

}