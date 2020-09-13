/*
 * Creator: Kamran Noorinejad on 8/10/20 4:50 PM
 * Last modified: 8/10/20 4:50 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */
package com.kmno.leftorite.utils

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.kmno.leftorite.R
import kotlin.math.max
import kotlin.math.min

/**
 * Created by Kamran Noorinejad on 8/10/2020 AD 16:50.
 * Edited by Kamran Noorinejad on 8/10/2020 AD 16:50.
 */
class SplitView(context: Context, attrs: AttributeSet?) :
    LinearLayout(context, attrs), OnTouchListener {
    private val mHandleId: Int
    var handle: View? = null
        private set
    private val mPrimaryContentId: Int
    private var mPrimaryContent: View? = null
    private val mSecondaryContentId: Int
    private var mSecondaryContent: View? = null
    private var mLastPrimaryContentSize = 0
    private var mDragging = false
    private var mDraggingStarted: Long = 0
    private var mDragStartX = 0f
    private var mDragStartY = 0f
    private val mPointerOffset = 0f
    private var primaryDim: View? = null
    private var secondaryDim: View? = null
    private var deviceWidth = 0

    public override fun onFinishInflate() {
        super.onFinishInflate()
        handle = findViewById(mHandleId)
        if (handle == null) {
            val name = resources.getResourceEntryName(mHandleId)
            throw RuntimeException("Your Panel must have a child View whose id attribute is 'R.id.$name'")
        }
        mPrimaryContent = findViewById(mPrimaryContentId)
        if (mPrimaryContent == null) {
            val name = resources.getResourceEntryName(mPrimaryContentId)
            throw RuntimeException("Your Panel must have a child View whose id attribute is 'R.id.$name'")
        }
        mLastPrimaryContentSize = primaryContentSize
        mSecondaryContent = findViewById(mSecondaryContentId)
        if (mSecondaryContent == null) {
            val name = resources.getResourceEntryName(mSecondaryContentId)
            throw RuntimeException("Your Panel must have a child View whose id attribute is 'R.id.$name'")
        }
        // mPrimaryContent!!.setOnTouchListener(this)
        // mSecondaryContent!!.setOnTouchListener(this)
        handle?.setOnTouchListener(this)

        primaryDim = mPrimaryContent?.findViewById<FrameLayout>(R.id.left_item_dim)
        secondaryDim = mSecondaryContent?.findViewById<FrameLayout>(R.id.right_item_dim)
        getDeviceWidth()

    }

    private fun getDeviceWidth() {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        deviceWidth = displayMetrics.widthPixels
    }

    override fun onTouch(view: View, me: MotionEvent): Boolean {
        // Only capture drag events if we start
        // if (view !== handle) {
        //  return false
        // }
        when (me.action) {
            MotionEvent.ACTION_DOWN -> {
                mDragging = true
                mDraggingStarted = SystemClock.elapsedRealtime()
                mDragStartX = me.x
                mDragStartY = me.y
                /*if (getOrientation() == VERTICAL) {
                    mPointerOffset = me.getRawY() - getPrimaryContentSize();
                } else {
                    mPointerOffset = me.getRawX() - getPrimaryContentSize();
                }*/
                return true
            }
            MotionEvent.ACTION_UP -> {
                mDragging = false
                if (mDragStartX < me.x + TAP_DRIFT_TOLERANCE &&
                    mDragStartX > me.x - TAP_DRIFT_TOLERANCE &&
                    mDragStartY < me.y + TAP_DRIFT_TOLERANCE &&
                    mDragStartY > me.y - TAP_DRIFT_TOLERANCE &&
                    SystemClock.elapsedRealtime() - mDraggingStarted < SINGLE_TAP_MAX_TIME
                ) {
                    if (isPrimaryContentMaximized || isSecondaryContentMaximized) {
                        setPrimaryContentSize(mLastPrimaryContentSize)
                    } else {
                        maximizeRightContent()
                    }
                }

                //  handle?.animate()?.x(500f)?.setDuration(500)?.start()
                setPrimaryContentWidth(deviceWidth / 2)
                secondaryDim?.animate()?.alpha(0f)?.start()
                primaryDim?.animate()?.alpha(0f)?.start()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if ((me.rawX - mPointerOffset).toInt() < 150 || (me.rawX - mPointerOffset).toInt() > (deviceWidth - 150)) return false
                setPrimaryContentWidth((me.rawX - mPointerOffset).toInt())
                when {
                    (1 - (me.rawX / 1000)) < 0.45f -> {
                        primaryDim?.animate()?.alpha(0f)?.duration = 100
                        secondaryDim?.animate()?.alpha(((me.rawX / 1000)))?.duration = 100
                    }
                    (1 - (me.rawX / 1000)) > 0.55f -> {
                        secondaryDim?.animate()?.alpha(0f)?.duration = 100
                        primaryDim?.animate()?.alpha((1 - (me.rawX / 1000) * 2))?.duration = 100
                    }
                    else -> {
                        secondaryDim?.animate()?.alpha(0f)?.start()
                        primaryDim?.animate()?.alpha(0f)?.start()
                    }
                }
            }
        }
        return true
    }

    private val primaryContentSize: Int
        get() = if (orientation == VERTICAL) {
            mPrimaryContent!!.measuredHeight
        } else {
            mPrimaryContent!!.measuredWidth
        }

    private fun setPrimaryContentSize(newSize: Int): Boolean {
        return setPrimaryContentWidth(newSize)
        /*if (orientation == VERTICAL) {
            setPrimaryContentHeight(newSize)
        } else {
            setPrimaryContentWidth(newSize)
        //}*/
    }

    private fun setPrimaryContentHeight(_newHeight: Int): Boolean {
        // the new primary content height should not be less than 0 to make the
        // handler always visible
        var newHeight = _newHeight
        newHeight = max(0, newHeight)
        // the new primary content height should not be more than the SplitView
        // height minus handler height to make the handler always visible
        newHeight = Math.min(newHeight, measuredHeight - handle!!.measuredHeight)
        val params = mPrimaryContent?.layoutParams as LayoutParams
        if (mSecondaryContent!!.measuredHeight < 1 && newHeight > params.height) {
            return false
        }
        if (newHeight >= 0) {
            params.height = newHeight
            // set the primary content parameter to do not stretch anymore and
            // use the height specified in the layout params
            params.weight = 0f
        }
        unMinimizeSecondaryContent()
        mPrimaryContent!!.layoutParams = params
        return true
    }

    private fun setPrimaryContentWidth(_newWidth: Int): Boolean {
        // the new primary content width should not be less than 0 to make the
        // handler always visible
        var newWidth = _newWidth
        newWidth = max(0, newWidth)
        // the new primary content width should not be more than the SplitView
        // width minus handler width to make the handler always visible
        newWidth = min(newWidth, measuredWidth - handle!!.measuredWidth)
        val params = mPrimaryContent?.layoutParams as LayoutParams
        if (mSecondaryContent!!.measuredWidth < 1 && newWidth > params.width) {
            return false
        }
        if (newWidth >= 0) {
            params.width = newWidth
            // set the primary content parameter to do not stretch anymore and
            // use the width specified in the layout params
            params.weight = 0f
        }
        unMinimizeSecondaryContent()
        mPrimaryContent!!.layoutParams = params

        return true
    }

    private val isPrimaryContentMaximized: Boolean
        get() = orientation == VERTICAL && mSecondaryContent!!.measuredHeight < MAXIMIZED_VIEW_TOLERANCE_DIP ||
                orientation == HORIZONTAL && mSecondaryContent!!.measuredWidth < MAXIMIZED_VIEW_TOLERANCE_DIP

    private val isSecondaryContentMaximized: Boolean
        get() = orientation == VERTICAL && mPrimaryContent!!.measuredHeight < MAXIMIZED_VIEW_TOLERANCE_DIP ||
                orientation == HORIZONTAL && mPrimaryContent!!.measuredWidth < MAXIMIZED_VIEW_TOLERANCE_DIP

    fun maximizeLeftContent() {
        maximizeContentPane(mPrimaryContent, mSecondaryContent)
    }

    fun maximizeRightContent() {
        maximizeContentPane(mSecondaryContent, mPrimaryContent)
    }

    private fun maximizeContentPane(
        toMaximize: View?,
        toUnMaximize: View?
    ) {
        mLastPrimaryContentSize = primaryContentSize
        val params = toUnMaximize?.layoutParams as LayoutParams
        val secondaryParams = toMaximize?.layoutParams as LayoutParams
        // set the primary content parameter to do not stretch anymore and use
        // the height/width specified in the layout params
        params.weight = 0f
        // set the secondary content parameter to use all the available space
        secondaryParams.weight = 1f
        if (orientation == VERTICAL) {
            params.height = 1
        } else {
            params.width = 1
        }
        toUnMaximize.layoutParams = params
        toMaximize.layoutParams = secondaryParams
    }

    private fun unMinimizeSecondaryContent() {
        val secondaryParams = mSecondaryContent?.layoutParams as LayoutParams
        // set the secondary content parameter to use all the available space
        secondaryParams.weight = 1f
        mSecondaryContent!!.layoutParams = secondaryParams
    }

    companion object {
        private const val MAXIMIZED_VIEW_TOLERANCE_DIP = 30
        private const val TAP_DRIFT_TOLERANCE = 3
        private const val SINGLE_TAP_MAX_TIME = 175
    }

    init {
        val viewAttrs = context.obtainStyledAttributes(attrs, R.styleable.SplitView)
        var e: RuntimeException? = null
        mHandleId = viewAttrs.getResourceId(R.styleable.SplitView_handle, 0)
        if (mHandleId == 0) {
            e = IllegalArgumentException(
                viewAttrs.positionDescription +
                        ": The required attribute handle must refer to a valid child view."
            )
        }
        mPrimaryContentId = viewAttrs.getResourceId(R.styleable.SplitView_primaryContent, 0)
        if (mPrimaryContentId == 0) {
            e = IllegalArgumentException(
                viewAttrs.positionDescription +
                        ": The required attribute primaryContent must refer to a valid child view."
            )
        }
        mSecondaryContentId = viewAttrs.getResourceId(R.styleable.SplitView_secondaryContent, 0)
        if (mSecondaryContentId == 0) {
            e = IllegalArgumentException(
                viewAttrs.positionDescription +
                        ": The required attribute secondaryContent must refer to a valid child view."
            )
        }
        viewAttrs.recycle()
        if (e != null) {
            throw e
        }
    }
}