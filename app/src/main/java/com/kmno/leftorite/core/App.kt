/*
 * Creator: Kamran Noorinejad on 6/15/20 3:59 PM
 * Last modified: 6/15/20 3:57 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.core

import android.app.Activity
import android.app.Application
import android.content.res.Resources
import android.os.Bundle
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import coil.util.CoilUtils
import com.kmno.leftorite.R
import com.kmno.leftorite.di.apiModule
import com.kmno.leftorite.di.utilsModule
import com.kmno.leftorite.di.viewModelModule
import mu.KLogging
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Created by Kamran Noorinejad on 5/11/2020 AD 11:45.
 * Edited by Kamran Noorinejad on 5/11/2020 AD 11:45.
 */
open class App : Application(), ImageLoaderFactory, Application.ActivityLifecycleCallbacks {

    companion object : KLogging() {
        lateinit var resourcesCtx: Resources
    }

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
        resourcesCtx = resources
        Coil.setImageLoader(newImageLoader())
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

    //create default imageloader
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this@App)
            .availableMemoryPercentage(0.25)
            //.crossfade(true)
            .allowHardware(false)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .placeholder(R.drawable.placeholder_trans)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(this@App))
                    .build()
            }
            .build()
    }
}