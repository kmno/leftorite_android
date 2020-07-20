/*
 * Creator: Kamran Noorinejad on 5/27/20 11:27 AM
 * Last modified: 5/27/20 11:27 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.utils

import com.andrognito.flashbar.Flashbar
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import com.kmno.leftorite.R
import com.kmno.leftorite.core.App
import com.kmno.leftorite.ui.base.BaseActivity

/**
 * Created by Kamran Noorinejad on 5/27/2020 AD 11:26.
 * Edited by Kamran Noorinejad on 5/27/2020 AD 11:26.
 */
object Alerts {

    var alert: AlertView? = null
    private var flashbar: Flashbar? = null
    private var flashbarConfig: Flashbar.Builder? = null
    private var flashbarProgress: Flashbar? = null
    private var flashbarProgressConfig: Flashbar.Builder? = null

    private const val default_message = "Are You Sure?"
    private const val default_positive_button_text = "Yes"
    private const val default_negative_button_text = "No"

    fun showAlertDialogWithDefaultButton(
        title: String, msg: String = default_message,
        action: String = default_positive_button_text,
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
        title: String, msg: String = default_message,
        actionPositiveTitle: String = default_positive_button_text,
        actionNegativeTitle: String = default_negative_button_text,
        actionPositiveCallback: () -> Unit = {},
        actionNegativeCallback: () -> Unit = {},
        activity: BaseActivity
    ) {
        alert = AlertView(title, msg, AlertStyle.DIALOG)
        alert?.addAction(
            AlertAction(
                actionPositiveTitle,
                AlertActionStyle.POSITIVE
            ) { actionPositiveCallback() })
        alert?.addAction(
            AlertAction(
                actionNegativeTitle,
                AlertActionStyle.NEGATIVE
            ) { actionNegativeCallback() })
        alert?.show(activity)
    }

    fun showVersionAlertDialogWithTwoActionButton(
        title: String,
        msg: String,
        actionPositiveTitle: String = App.resourcesCtx.getString(R.string.update_now),
        actionNegativeTitle: String = App.resourcesCtx.getString(R.string.update_later),
        actionPositiveCallback: () -> Unit = {},
        actionNegativeCallback: () -> Unit = {},
        activity: BaseActivity
    ) {
        alert = AlertView(title, msg, AlertStyle.DIALOG)
        alert?.addAction(
            AlertAction(
                actionPositiveTitle,
                AlertActionStyle.POSITIVE
            ) { actionPositiveCallback() })
        alert?.addAction(
            AlertAction(
                actionNegativeTitle,
                AlertActionStyle.NEGATIVE
            ) { actionNegativeCallback() })
        alert?.show(activity)
    }

    fun showBottomSheetWithActionButton(
        title: String, msg: String = default_message,
        actionPositiveTitle: String = default_positive_button_text,
        actionPositiveCallback: () -> Unit = {},
        activity: BaseActivity
    ) {
        alert = AlertView(title, msg, AlertStyle.IOS)
        if (actionPositiveTitle.isNotEmpty()) {
            alert?.addAction(
                AlertAction(
                    actionPositiveTitle,
                    AlertActionStyle.NEGATIVE
                ) { actionPositiveCallback() })
        }
        alert?.show(activity)
    }

    fun showBottomSheetErrorWithActionButton(
        title: String = App.resourcesCtx.getString(R.string.network_request_error_title),
        msg: String = App.resourcesCtx.getString(R.string.network_request_error_msg),
        actionPositiveTitle: String = "",
        actionPositiveCallback: () -> Unit = {},
        activity: BaseActivity
    ) {
        alert = AlertView(title, msg, AlertStyle.IOS)
        if (actionPositiveTitle.isNotEmpty()) {
            alert?.addAction(
                AlertAction(
                    actionPositiveTitle,
                    AlertActionStyle.NEGATIVE
                ) { actionPositiveCallback() })
        }
        alert?.show(activity)
    }

    fun showBottomSheetWithTwoActionButton(
        title: String, msg: String = default_message,
        actionPositiveTitle: String = default_positive_button_text,
        actionNegativeTitle: String = default_negative_button_text,
        actionPositiveCallback: () -> Unit = {}, actionNegativeCallback: () -> Unit = {},
        activity: BaseActivity
    ) {
        alert = AlertView(title, msg, AlertStyle.IOS)
        alert?.addAction(
            AlertAction(
                actionPositiveTitle,
                AlertActionStyle.POSITIVE
            ) { actionPositiveCallback() })
        alert?.addAction(
            AlertAction(
                actionNegativeTitle,
                AlertActionStyle.NEGATIVE
            ) { actionNegativeCallback() })
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

    fun showNoNetworkFlashbar(
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
            // .showOverlay()
            .overlayBlockable()
            .overlayColorRes(R.color.overlay_color)
            .castShadow(false)

        if (duration != 0) flashbarConfig?.duration(((duration * 1000).toLong()))
        flashbar = flashbarConfig?.build()
        flashbar?.show()
    }

    fun showFlashbarWithProgress(activity: BaseActivity) {
        flashbarProgressConfig = Flashbar.Builder(activity)
            .gravity(Flashbar.Gravity.TOP)
            .backgroundColorRes(R.color.colorPrimaryDark)
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
        if (flashbar != null) flashbar?.dismiss()
    }

    fun dismissProgressFlashbar() {
        if (flashbarProgress != null) flashbarProgress?.dismiss()
    }
}