/*
 * Creator: Kamran Noorinejad on 6/10/20 12:21 PM
 * Last modified: 6/10/20 12:21 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.activities

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.kmno.leftorite.BuildConfig
import com.kmno.leftorite.R
import com.kmno.leftorite.ui.base.BaseActivity
import com.kmno.leftorite.ui.viewmodels.SettingActivityViewModel
import com.kmno.leftorite.utils.Alerts
import com.kmno.leftorite.utils.launchActivity
import kotlinx.android.synthetic.main.toolbar_with_close_button.*
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsActivity : BaseActivity() {

    private val settingActivityViewModel: SettingActivityViewModel by viewModel()

    override fun getResId(): Int {
        return R.layout.activity_settings
    }

    override fun afterCreate() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment(settingActivityViewModel))
            .commit()

        close_button.setOnClickListener {
            onBackPressed()
        }
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
    }

    override fun networkStatus(state: Boolean) {
    }

    class SettingsFragment(_settingActivityViewModelParam: SettingActivityViewModel) :
        PreferenceFragmentCompat() {

        private var _settingActivityViewModel: SettingActivityViewModel =
            _settingActivityViewModelParam

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val dataSaver: SwitchPreferenceCompat? = findPreference("data_saver")
            dataSaver?.isChecked = _settingActivityViewModel.dataSaver

            val appVersion: Preference? = findPreference("app_version")
            appVersion?.summary = BuildConfig.VERSION_NAME

        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            when (preference?.key) {
                "data_saver" -> {
                    _settingActivityViewModel.setDataSaverPref((preference as SwitchPreferenceCompat).isChecked)
                }
                "sign_out" -> {
                    Alerts.showAlertDialogWithTwoActionButton("Sign Out", "Are You Sure?", "", "",
                        {
                            _settingActivityViewModel.signOutUser()
                            activity?.launchActivity<AuthActivity>(finishAffinity = true)
                        }, {}, activity = activity as BaseActivity
                    )
                }
            }
            return super.onPreferenceTreeClick(preference)
        }

    }



}