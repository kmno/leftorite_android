/*
 * Creator: Kamran Noorinejad on 5/13/20 12:54 PM
 * Last modified: 5/13/20 12:53 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.activities

import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.activity.viewModels
import com.kmno.leftorite.App
import com.kmno.leftorite.R
import com.kmno.leftorite.di.TestClass
import com.kmno.leftorite.ui.base.BaseActivity
import com.kmno.leftorite.ui.viewmodels.SplashActivityViewModel
import com.kmno.leftorite.utils.NetworkInfo
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity() {

    private val viewModel: SplashActivityViewModel by viewModels()
    val test: TestClass by inject()

    override fun getResId(): Int {
        return R.layout.activity_splash
    }

    override fun afterCreate() {

        with(viewModel.appVersionText) {
            app_version_text.text = this
        }

        App.logger.error(test.itsReturnTest("kjehkdhkjdhkjshdkjshdjk"))

        goToDestinationActivity()

        imageView.setOnClickListener {
        }
    }

    private fun goToDestinationActivity() {
        if (isNetworkAvailable) {
            splash_progress_bar.visibility = View.VISIBLE
            App.logger.error("ok")
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
                finish()
            }, 2000)
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
        if (state) goToDestinationActivity()
        else {

        }
    }

    override fun networkStatusChange(network: NetworkInfo.Network) {

    }


}