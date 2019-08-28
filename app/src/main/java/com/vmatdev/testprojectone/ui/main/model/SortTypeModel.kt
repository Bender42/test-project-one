package com.vmatdev.testprojectone.ui.main.model

import android.graphics.Color
import android.view.View
import com.vmatdev.testprojectone.R
import kotlinx.android.synthetic.main.fragment_post_list.view.*

class SortTypeModel(val sortTypeContainer: View, defaultSortType: SortType = SortType.SORT_TYPE_SERVER, val callback: (SortType) -> Unit) {

    init {
        setSortType(defaultSortType)
        sortTypeContainer.sort_type_server.setOnClickListener {
            setSortType(SortType.SORT_TYPE_SERVER)
            callback(SortType.SORT_TYPE_SERVER)
        }
        sortTypeContainer.sort_type_date.setOnClickListener {
            setSortType(SortType.SORT_TYPE_DATE)
            callback(SortType.SORT_TYPE_DATE)
        }
    }

    private fun setSortType(sortType: SortType) {
        when (sortType) {
            SortType.SORT_TYPE_SERVER -> {
                sortTypeContainer.sort_type_server.setTextColor(Color.parseColor("#FFFFFF"))
                sortTypeContainer.sort_type_date.setTextColor(Color.parseColor("#2196F3"))
                sortTypeContainer.sort_type_server.setBackgroundResource(R.drawable.bg_sort_type_selected)
                sortTypeContainer.sort_type_date.setBackgroundResource(R.drawable.bg_sort_type_not_selected)
            }
            SortType.SORT_TYPE_DATE -> {
                sortTypeContainer.sort_type_server.setTextColor(Color.parseColor("#2196F3"))
                sortTypeContainer.sort_type_date.setTextColor(Color.parseColor("#FFFFFF"))
                sortTypeContainer.sort_type_server.setBackgroundResource(R.drawable.bg_sort_type_not_selected)
                sortTypeContainer.sort_type_date.setBackgroundResource(R.drawable.bg_sort_type_selected)
            }
        }
    }
}

enum class SortType {
    SORT_TYPE_SERVER,
    SORT_TYPE_DATE
}