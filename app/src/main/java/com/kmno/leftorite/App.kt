/*
 * Creator: Kamran Noorinejad on 5/11/20 11:45 AM
 * Last modified: 5/11/20 11:45 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.kmno.leftorite.di.apiModule
import com.kmno.leftorite.di.utilsModule
import com.kmno.leftorite.di.viewModelModule
import mu.KLogging
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Created by Kamran Noorinejad on 5/11/2020 AD 11:45.
 * Edited by Kamran Noorinejad on 5/11/2020 AD 11:45.
 */
open class App : Application(), Application.ActivityLifecycleCallbacks {

    companion object : KLogging()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                viewModelModule,
                utilsModule,
                apiModule
            )
        }

        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
    }

    override fun onActivityResumed(p0: Activity) {
    }
}