/*
 * Creator: Kamran Noorinejad on 5/19/20 1:12 PM
 * Last modified: 5/19/20 1:12 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.di

import com.kmno.leftorite.ui.viewmodels.*
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Kamran Noorinejad on 5/19/2020 AD 13:12.
 * Edited by Kamran Noorinejad on 5/19/2020 AD 13:12.
 */

val viewModelModule = module {

    viewModel { SplashActivityViewModel(get()) }
    viewModel { AuthActivityViewModel(get()) }
    viewModel { HomeActivityViewModel(get()) }
    viewModel { CategoryViewModel(androidApplication(), get()) }
    viewModel { ProfileActivityViewModel(get()) }
    viewModel { SettingActivityViewModel() }

}