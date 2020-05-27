/*
 * Creator: Kamran Noorinejad on 5/17/20 10:40 AM
 * Last modified: 5/17/20 10:39 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.activities

import android.view.View
import androidx.lifecycle.Observer
import com.kmno.leftorite.App
import com.kmno.leftorite.R
import com.kmno.leftorite.data.api.State
import com.kmno.leftorite.data.model.User
import com.kmno.leftorite.ui.base.BaseActivity
import com.kmno.leftorite.ui.viewmodels.AuthActivityViewModel
import com.kmno.leftorite.utils.Alerts.Companion.dismissFlashbar
import com.kmno.leftorite.utils.Alerts.Companion.showAlertDialogWithDefaultButton
import com.kmno.leftorite.utils.Alerts.Companion.showFlashbar
import com.kmno.leftorite.utils.Alerts.Companion.showFlashbarWithProgress
import com.kmno.leftorite.utils.launchActivity
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.android.viewmodel.ext.android.viewModel

class AuthActivity : BaseActivity() {

    private val authActivityViewModel: AuthActivityViewModel by viewModel()
    private var loginState = true

    private var email = ""
    private var pass = ""
    private var rpass = ""

    override fun getResId(): Int {
        return R.layout.activity_auth
    }

    override fun afterCreate() {

        requestPermission()

        auth_action_btn.setOnClickListener {
            email = email_edit_text_field.text.toString().trim()
            pass = password_edit_text_field.text.toString().trim()
            rpass = password_confirm_edit_text_field.text.toString().trim()
            if (validate()) {
                if (loginState) {
                    authActivityViewModel.signInUser(
                        email_edit_text_field.text.toString(),
                        password_edit_text_field.text.toString()
                    )
                        .observe(this, Observer { networkResource ->
                            when (networkResource.state) {
                                State.LOADING -> {
                                    showFlashbarWithProgress(this)
                                }
                                State.SUCCESS -> {
                                    val status = networkResource.status
                                    status?.let {
                                        when (it) {
                                            true -> {
                                                dismissFlashbar()
                                                val user = networkResource.data as User
                                                authActivityViewModel.storeUserPrefs(user)
                                                authActivityViewModel.setLoggedInPref(true)
                                                this.launchActivity<HomeActivity>(finish = true)
                                            }
                                            false -> {
                                                showAlertDialogWithDefaultButton(
                                                    "Error",
                                                    networkResource.message!!,
                                                    "Try Again",
                                                    this
                                                )
                                            }
                                        }

                                    }
                                }
                                State.ERROR -> {
                                    showAlertDialogWithDefaultButton(
                                        "Error",
                                        networkResource.message!!, "Try Again", this
                                    )
                                }
                            }
                        })
                } else {
                    authActivityViewModel.signUpUser(
                        email_edit_text_field.text.toString(),
                        password_edit_text_field.text.toString()
                    )
                        .observe(this, Observer { networkResource ->
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
                                                authActivityViewModel.storeUserPrefs(user)
                                                authActivityViewModel.setLoggedInPref(true)
                                                this.launchActivity<HomeActivity>(finish = true)
                                            }
                                            false -> {
                                                showAlertDialogWithDefaultButton(
                                                    "Error",
                                                    networkResource.message!!,
                                                    "Try Again",
                                                    this
                                                )
                                            }
                                        }

                                    }
                                }
                                State.ERROR -> {
                                    showAlertDialogWithDefaultButton(
                                        "Error",
                                        networkResource.message!!,
                                        "Try Again", this
                                    )
                                }
                            }
                        })
                }
            }
        }

        dont_have_account_sign_up_text_field.setOnClickListener {
            when (loginState) {
                true -> {
                    dont_have_account_sign_up_text_field.text =
                        getString(R.string.have_account_sign_in_text)
                    auth_action_btn.text = getString(R.string.signup_button_text)
                    password_confirm_edit_text_field.visibility = View.VISIBLE
                    loginState = false
                }
                false -> {
                    dont_have_account_sign_up_text_field.text =
                        getString(R.string.dont_have_account_sign_up_text)
                    auth_action_btn.text = getString(R.string.sigin_btn_text)
                    password_confirm_edit_text_field.visibility = View.GONE
                    rpass = ""
                    password_confirm_edit_text_field.text.clear()
                    loginState = true
                }
            }
        }

    }

    private fun validate(): Boolean {
        if (pass.isNotEmpty()) {
            if (pass.length >= 8) {
                if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches()
                ) {
                    if (!loginState) {
                        return if (rpass != pass) {
                            showFlashbar(
                                R.color.error,
                                R.string.diff_passwords,
                                R.string.diff_passwords_desc,
                                3,
                                this
                            )
                            false
                        } else {
                            true
                        }
                    } else return true
                } else
                    showFlashbar(
                        R.color.error,
                        R.string.invalid_email,
                        R.string.invalid_email_desc,
                        3,
                        this
                    )
            } else
                showFlashbar(
                    R.color.error,
                    R.string.invalid_pass,
                    R.string.invalid_pass_desc,
                    3,
                    this
                )
        } else
            showFlashbar(R.color.error, R.string.empty_fields, R.string.empty_fields_desc, 3, this)
        return false
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
