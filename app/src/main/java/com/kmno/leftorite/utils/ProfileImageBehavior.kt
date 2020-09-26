/*
 * Creator: Kamran Noorinejad on 9/23/20 12:13 PM
 * Last modified: 9/23/20 12:13 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.utils

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout


/**
 * Created by Kamran Noorinejad on 9/23/2020 AD 12:13.
 * Edited by Kamran Noorinejad on 9/23/2020 AD 12:13.
 */
class ProfileImageBehavior(context: Context, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<ImageView?>() {
    /*  private val mContext: Context
      private var mCustomFinalYPosition = 0f
      private var mCustomStartXPosition = 0f
      private var mCustomStartToolbarPosition = 0f
      private var mCustomStartHeight = 0f
      private var mCustomFinalHeight = 0f
      private var mAvatarMaxSize = 0f
      private val mFinalLeftAvatarPadding: Float
      private val mStartPosition = 0f
      private var mStartXPosition = 0
      private var mStartToolbarPosition = 0f
      private var mStartYPosition = 0
      private var mFinalYPosition = 0
      private var mStartHeight = 0
      private var mFinalXPosition = 0
      private var mChangeBehaviorPoint = 0f
      private fun init() {
          bindDimensions()
      }

      private fun bindDimensions() {
          mAvatarMaxSize = mContext.getResources().getDimension(R.dimen.)
      }

      fun layoutDependsOn(
          parent: CoordinatorLayout?,
          child: ImageView?,
          dependency: View?
      ): Boolean {
          return dependency is Toolbar
      }

      fun onDependentViewChanged(
          parent: CoordinatorLayout?,
          child: ImageView,
          dependency: View
      ): Boolean {
          maybeInitProperties(child, dependency)
          val maxScrollDistance = mStartToolbarPosition.toInt()
          val expandedPercentageFactor: Float = dependency.getY() / maxScrollDistance
          if (expandedPercentageFactor < mChangeBehaviorPoint) {
              val heightFactor =
                  (mChangeBehaviorPoint - expandedPercentageFactor) / mChangeBehaviorPoint
              val distanceXToSubtract: Float = ((mStartXPosition - mFinalXPosition)
                      * heightFactor) + child.getHeight() / 2
              val distanceYToSubtract: Float = ((mStartYPosition - mFinalYPosition)
                      * (1f - expandedPercentageFactor)) + child.getHeight() / 2
              child.setX(mStartXPosition - distanceXToSubtract)
              child.setY(mStartYPosition - distanceYToSubtract)
              val heightToSubtract = (mStartHeight - mCustomFinalHeight) * heightFactor
              val lp = child.getLayoutParams() as CoordinatorLayout.LayoutParams
              lp.width = (mStartHeight - heightToSubtract).toInt()
              lp.height = (mStartHeight - heightToSubtract).toInt()
              child.setLayoutParams(lp)
          } else {
              val distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                      * (1f - expandedPercentageFactor)) + mStartHeight / 2
              child.setX(mStartXPosition - child.getWidth() / 2)
              child.setY(mStartYPosition - distanceYToSubtract)
              val lp = child.getLayoutParams() as CoordinatorLayout.LayoutParams
              lp.width = mStartHeight
              lp.height = mStartHeight
              child.setLayoutParams(lp)
          }
          return true
      }

      private fun maybeInitProperties(child: ImageView, dependency: View) {
          if (mStartYPosition == 0) mStartYPosition = dependency.getY()
          if (mFinalYPosition == 0) mFinalYPosition = dependency.getHeight() / 2
          if (mStartHeight == 0) mStartHeight = child.getHeight()
          if (mStartXPosition == 0) mStartXPosition = (child.getX() + child.getWidth() / 2)
          if (mFinalXPosition == 0) mFinalXPosition = mContext.getResources()
              .getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + mCustomFinalHeight.toInt() / 2
          if (mStartToolbarPosition == 0f) mStartToolbarPosition = dependency.getY()
          if (mChangeBehaviorPoint == 0f) {
              mChangeBehaviorPoint =
                  (child.getHeight() - mCustomFinalHeight) / (2f * (mStartYPosition - mFinalYPosition))
          }
      }

      val statusBarHeight: Int
          get() {
              var result = 0
              val resourceId: Int =
                  mContext.getResources().getIdentifier("status_bar_height", "dimen", "android")
              if (resourceId > 0) {
                  result = mContext.getResources().getDimensionPixelSize(resourceId)
              }
              return result
          }

      companion object {
          private const val MIN_AVATAR_PERCENTAGE_SIZE = 0.3f
          private const val EXTRA_FINAL_AVATAR_PADDING = 80
          private const val TAG = "behavior"
      }

      init {
          mContext = context
          if (attrs != null) {
              val a: TypedArray =
                  context.obtainStyledAttributes(attrs, R.styleable.AvatarImageBehavior)
              mCustomFinalYPosition =
                  a.getDimension(R.styleable.AvatarImageBehavior_finalYPosition, 0f)
              mCustomStartXPosition =
                  a.getDimension(R.styleable.AvatarImageBehavior_startXPosition, 0f)
              mCustomStartToolbarPosition =
                  a.getDimension(R.styleable.AvatarImageBehavior_startToolbarPosition, 0f)
              mCustomStartHeight = a.getDimension(R.styleable.AvatarImageBehavior_startHeight, 0f)
              mCustomFinalHeight = a.getDimension(R.styleable.AvatarImageBehavior_finalHeight, 0f)
              a.recycle()
          }
          init()
          mFinalLeftAvatarPadding = context.getResources().getDimension(
              R.dimen.spacing_normal
          )
      }*/
}