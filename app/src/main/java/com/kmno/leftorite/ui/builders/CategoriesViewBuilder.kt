/*
 * Creator: Kamran Noorinejad on 6/8/20 2:49 PM
 * Last modified: 6/8/20 2:49 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.builders

import android.view.View
import cn.vove7.bottomdialog.interfaces.ContentBuilder
import com.kmno.leftorite.R

/**
 * Created by Kamran Noorinejad on 6/8/2020 AD 14:49.
 * Edited by Kamran Noorinejad on 6/8/2020 AD 14:49.
 */
class CategoriesViewBuilder : ContentBuilder() {

    override val layoutRes: Int
        get() = R.layout.category_bottom_sheet

//    lateinit var prog: ProgressBar
    // lateinit var recyclerview: RecyclerView

    override fun init(view: View) {
        // prog = view.findViewById(R.id.bottom_sheet_progress_bar)
        //  recyclerview = view.findViewById(R.id.categories_recyclerview)
    }

    override fun updateContent(type: Int, data: Any?) {
    }

    override fun onAfterShow() {
        super.onAfterShow()
    }


}
