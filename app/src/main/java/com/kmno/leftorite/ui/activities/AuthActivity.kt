/*
 * Creator: Kamran Noorinejad on 5/17/20 10:40 AM
 * Last modified: 5/17/20 10:39 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.activities

import androidx.lifecycle.Observer
import com.kmno.leftorite.App
import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.State
import com.kmno.leftorite.data.model.User
import com.kmno.leftorite.ui.base.BaseActivity
import com.kmno.leftorite.ui.viewmodels.AuthActivityViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class AuthActivity : BaseActivity() {

    private val authActivityViewModel: AuthActivityViewModel by viewModel()

    override fun getResId(): Int {
        return R.layout.activity_auth
    }

    override fun afterCreate() {

        authActivityViewModel.loadData().observe(this, Observer { networkResource ->
            when (networkResource.state) {
                State.LOADING -> {
                    App.logger.error("loading data from network")
                }
                State.SUCCESS -> {
                    val status = networkResource.status
                    status?.let {
                        when (it) {
                            true -> {
                                val user = networkResource.data as User
                                showAlert("Welcome!", networkResource.message!!, "Continue...")
                            }
                            false -> {
                                showAlert("Error", networkResource.message!!, "Try Again")
                            }
                        }

                    }
                }
                State.ERROR -> {
                    showAlert("Error", networkResource.message!!, "Try Again")
                }
            }
        })

    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
    }

    override fun networkStatus(state: Boolean) {
    }

}
