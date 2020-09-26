/*
 * Creator: Kamran Noorinejad on 5/19/20 1:12 PM
 * Last modified: 5/19/20 1:12 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.di

import com.kmno.leftorite.ui.viewmodels.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Kamran Noorinejad on 5/19/2020 AD 13:12.
 * Edited by Kamran Noorinejad on 5/19/2020 AD 13:12.
 */
val viewModelModule = module {
    viewModel { SplashActivityViewModel(androidContext(), get()) }
    viewModel { SettingActivityViewModel(androidContext()) }
    viewModel { AuthActivityViewModel(androidContext(), get()) }
    viewModel { HomeActivityViewModel(androidContext(), get(), get()) }
    viewModel { ProfileActivityViewModel(androidContext(), get(), get()) }
    viewModel { CategoryViewModel(androidContext(), get()) }
}