/*
 * Creator: Kamran Noorinejad on 6/14/20 11:15 AM
 * Last modified: 6/14/20 11:15 AM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.builders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.vove7.bottomdialog.interfaces.ContentBuilder
import com.kmno.leftorite.App
import com.kmno.leftorite.R

/**
 * Created by Kamran Noorinejad on 6/14/2020 AD 11:15.
 * Edited by Kamran Noorinejad on 6/14/2020 AD 11:15.
 */
class ItemDetailsViewBuilder : ContentBuilder() {

    override val layoutRes: Int
        get() = R.layout.item_detail_bottom_sheet

    lateinit var itemLogo: ImageView
    lateinit var itemDescription: TextView

    override fun init(view: View) {
        App.logger.error("init")
        itemLogo = view.findViewById(R.id.item_logo)
        itemDescription = view.findViewById(R.id.item_description)
    }

    override fun updateContent(type: Int, data: Any?) {
        App.logger.error("updateContent")

    }

    override fun onAfterShow() {
        super.onAfterShow()
        App.logger.error("onAfterShow")
    }
}