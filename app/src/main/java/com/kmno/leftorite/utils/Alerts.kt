/*
 * Creator: Kamran Noorinejad on 5/27/20 11:27 AM
 * Last modified: 5/27/20 11:27 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.utils

import android.os.Handler
import com.andrognito.flashbar.Flashbar
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import com.kmno.leftorite.App
import com.kmno.leftorite.R
import com.kmno.leftorite.ui.base.BaseActivity

/**
 * Created by Kamran Noorinejad on 5/27/2020 AD 11:26.
 * Edited by Kamran Noorinejad on 5/27/2020 AD 11:26.
 */
class Alerts {

    companion object {

        var alert: AlertView? = null
        private var flashbar: Flashbar? = null
        private var flashbarConfig: Flashbar.Builder? = null
        private var flashbarProgress: Flashbar? = null
        private var flashbarProgressConfig: Flashbar.Builder? = null

        fun showAlertDialogWithDefaultButton(
            title: String, msg: String, action: String,
            activity: BaseActivity
        ) {
            alert = AlertView(title, msg, AlertStyle.DIALOG)
            alert?.addAction(AlertAction(action, AlertActionStyle.DEFAULT) {
                dismissFlashbar()
                dismissProgressFlashbar()
            })
            alert?.show(activity)
        }

        fun showAlertDialogWithTwoActionButton(
            title: String, msg: String,
            actionPositiveTitle: String, actionNegativeTitle: String,
            actionPositiveCallback: () -> Unit, actionNegativeCallback: () -> Unit,
            activity: BaseActivity
        ) {
            alert = AlertView(title, msg, AlertStyle.DIALOG)
            alert?.addAction(
                AlertAction(
                    actionPositiveTitle,
                    AlertActionStyle.POSITIVE
                ) { action -> actionPositiveCallback() })
            alert?.addAction(
                AlertAction(
                    actionNegativeTitle,
                    AlertActionStyle.NEGATIVE
                ) { action -> actionNegativeCallback() })
            alert?.show(activity)
        }

        fun showBottomSheetWithActionButton(
            title: String, msg: String,
            actionPositiveTitle: String,
            activity: BaseActivity
        ) {
            alert = AlertView(title, msg, AlertStyle.BOTTOM_SHEET)
            alert?.addAction(
                AlertAction(
                    actionPositiveTitle,
                    AlertActionStyle.DEFAULT
                ) { action -> })
            alert?.show(activity)
        }

        fun showBottomSheetWithTwoActionButton(
            title: String, msg: String,
            actionPositiveTitle: String, actionNegativeTitle: String,
            actionPositiveCallback: Handler.Callback, actionNegativeCallback: Handler.Callback,
            activity: BaseActivity
        ) {
            alert = AlertView(title, msg, AlertStyle.BOTTOM_SHEET)
            alert?.addAction(
                AlertAction(
                    actionPositiveTitle,
                    AlertActionStyle.POSITIVE
                ) { action -> actionPositiveCallback })
            alert?.addAction(
                AlertAction(
                    actionNegativeTitle,
                    AlertActionStyle.NEGATIVE
                ) { action -> actionNegativeCallback })
            alert?.show(activity)
        }

        fun showFlashbar(
            bgColor: Int,
            title: Int,
            msg: Int,
            duration: Int,
            activity: BaseActivity
        ) {
            dismissFlashbar()
            flashbarConfig = Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                .backgroundColorRes(bgColor)
                .messageSizeInSp(16f)
                .showIcon()
                .title(title)
                .message(msg)
                .castShadow(false)

            if (duration != 0) flashbarConfig?.duration(((duration * 1000).toLong()))
            flashbar = flashbarConfig?.build()
            flashbar?.show()
        }

        fun showFlashbarWithProgress(activity: BaseActivity) {
            flashbarProgressConfig = Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                .backgroundColorRes(R.color.colorAccent)
                .messageSizeInSp(16f)
                .title(R.string.loading_title)
                .message(R.string.loading_desc)
                .showProgress(Flashbar.ProgressPosition.LEFT)
                .showOverlay()
                .overlayBlockable()
                .overlayColorRes(R.color.overlay_color)
                .castShadow(false)
            flashbarProgress = flashbarProgressConfig?.build()
            flashbarProgress?.show()
        }

        fun dismissFlashbar() {
            if (flashbar != null) {
                if (flashbar!!.isShowing()) flashbar?.dismiss()
            }
        }

        fun dismissProgressFlashbar() {
            App.logger.error("1")
            if (flashbarProgress != null) {
                App.logger.error("2")
                //  if (flashbarProgress!!.isShowing()) {
                App.logger.error("3")
                flashbarProgress?.dismiss()
                // }
            }
        }

    }
}