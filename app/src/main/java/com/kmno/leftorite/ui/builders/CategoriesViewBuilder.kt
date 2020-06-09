/*
 * Creator: Kamran Noorinejad on 6/8/20 2:49 PM
 * Last modified: 6/8/20 2:49 PM
 * Copyright: All rights reserved Ⓒ 2020
 * http://www.itskamran.ir/
 */

package com.kmno.leftorite.ui.builders

import android.content.Intent
import android.content.pm.ResolveInfo
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.vove7.bottomdialog.builder.BindView
import cn.vove7.bottomdialog.builder.ListAdapterBuilder
import cn.vove7.bottomdialog.builder.OnItemClick
import cn.vove7.bottomdialog.interfaces.ContentBuilder
import cn.vove7.bottomdialog.util.ObservableList
import com.kmno.leftorite.App
import com.kmno.leftorite.R
import kotlinx.android.synthetic.main.recyclerview_list_category.view.*
import kotlin.concurrent.thread

/**
 * Created by Kamran Noorinejad on 6/8/2020 AD 14:49.
 * Edited by Kamran Noorinejad on 6/8/2020 AD 14:49.
 */
class CategoriesViewBuilder(
    val intent: Intent,
    items: ObservableList<ResolveInfo> = ObservableList(),
    onItemClick: OnItemClick<ResolveInfo>?
) : ListAdapterBuilder<ResolveInfo>(
    items, true, onItemClick
) {
    override val itemView: (type: Int) -> Int = { R.layout.recyclerview_list_category }
    override val layoutManager: RecyclerView.LayoutManager by lazy {
        GridLayoutManager(
            context,
            4
        )
    }

    override fun init(view: View) {
        super.init(view)
        load()
    }


    val pm get() = dialog.context.packageManager

    private fun load() {
        thread {
            val list = pm.queryIntentActivities(intent, 0).filter {
                it.activityInfo.name != null && it.activityInfo.exported
                true
            }
            items.addAll(list)
        }
    }

    override val bindView: BindView<ResolveInfo> = { view, item ->
        view.category_icon.setImageDrawable(item.loadIcon(pm))
        view.category_title.setText(item.loadLabel(pm))
    }
}

class MarkdownContentBuilder() : ContentBuilder() {

    override val layoutRes: Int
        get() = R.layout.modal_bottom_sheet

    lateinit var prog: ProgressBar
    lateinit var recyclerview: RecyclerView

    override fun init(view: View) {
        App.logger.error("init")
        prog = view.findViewById(R.id.bottom_sheet_progress_bar)
        recyclerview = view.findViewById(R.id.categories_recyclerview)
    }

    override fun updateContent(type: Int, data: Any?) {
        App.logger.error("updateContent")
    }

    override fun onAfterShow() {
        super.onAfterShow()
        App.logger.error("onAfterShow")
    }

    fun load() {

    }
}
