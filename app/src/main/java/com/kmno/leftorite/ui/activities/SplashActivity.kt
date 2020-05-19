/*
 * Creator: Kamran Noorinejad on 5/13/20 12:54 PM
 * Last modified: 5/13/20 12:53 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.activities

import android.os.Handler
import android.view.View
import com.kmno.leftorite.App
import com.kmno.leftorite.R
import com.kmno.leftorite.di.TestClass
import com.kmno.leftorite.ui.base.BaseActivity
import com.kmno.leftorite.ui.viewmodels.SplashActivityViewModel
import com.kmno.leftorite.utils.launchActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity() {

    private val splashActivityViewModel: SplashActivityViewModel by viewModel()
    val test: TestClass by inject()

    override fun getResId(): Int {
        return R.layout.activity_splash
    }

    override fun afterCreate() {

        with(splashActivityViewModel.appVersionText) {
            app_version_text.text = this
        }

        with(splashActivityViewModel.isUserLoggedIn) {
            goToDestinationActivity(this)
        }

        //App.logger.error(test.itsReturnTest("kjehkdhkjdhkjshdkjshdjk"))

    }

    private fun goToDestinationActivity(_loggedIn: Boolean) {
        if (isNetworkAvailable) {
            splash_progress_bar.visibility = View.VISIBLE
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
            App.logger.error("nok")
            splash_progress_bar.visibility = View.GONE
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