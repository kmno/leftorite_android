/*
 * Creator: Kamran Noorinejad on 5/11/20 11:45 AM
 * Last modified: 5/11/20 11:45 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite

import android.app.Application
import com.kmno.leftorite.di.utilsModule
import mu.KLogging
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by Kamran Noorinejad on 5/11/2020 AD 11:45.
 * Edited by Kamran Noorinejad on 5/11/2020 AD 11:45.
 */
open class App : Application() {

    companion object : KLogging()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(utilsModule)
        }
    }
}