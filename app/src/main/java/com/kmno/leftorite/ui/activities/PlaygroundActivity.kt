/*
 * Creator: Kamran Noorinejad on 9/9/20 10:15 AM
 * Last modified: 9/9/20 10:15 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.activities

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.kmno.leftorite.R
import com.kmno.leftorite.core.App
import java.util.*

class PlaygroundActivity : AppCompatActivity() {

    private var mLiveDataTimerViewModel: LiveDataTimerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playground)

        mLiveDataTimerViewModel = ViewModelProviders.of(this).get(
            LiveDataTimerViewModel::class.java
        )

        subscribe()
    }

    private fun subscribe() {
        mLiveDataTimerViewModel!!.elapsedTime.observe(this, Observer {
            App.logger.error(it.toString())
        })
    }

    //ViewModel Class
    class LiveDataTimerViewModel : ViewModel() {
        private val mElapsedTime = MutableLiveData<Long>()
        private val mInitialTime: Long = SystemClock.elapsedRealtime()
        val elapsedTime: LiveData<Long>
            get() = mElapsedTime

        companion object {
            private const val ONE_SECOND = 1000L
        }

        init {
            val timer = Timer()
            // Update the elapsed time every second.
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val newValue = (SystemClock.elapsedRealtime() - mInitialTime) / 1000
                    // setValue() cannot be called from a background thread so postValue is used.
                    mElapsedTime.postValue(newValue)
                }
            }, ONE_SECOND, ONE_SECOND)
        }
    }
}
