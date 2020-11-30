/*
 * Creator: Kamran Noorinejad on 5/13/20 5:54 PM
 * Last modified: 5/13/20 5:54 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.di

import coil.ImageLoader
import com.kmno.leftorite.utils.NetworkState
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created by Kamran Noorinejad on 5/13/2020 AD 17:54.
 * Edited by Kamran Noorinejad on 5/13/2020 AD 17:54.
 */
val utilsModule = module {
    single { NetworkState(androidApplication()) }
    single { ImageLoader(androidContext()) }
}