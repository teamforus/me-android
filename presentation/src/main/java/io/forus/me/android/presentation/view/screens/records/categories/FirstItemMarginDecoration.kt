package io.forus.me.android.presentation.view.screens.records.categories

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class FirstItemMarginDecoration(private val topMargin: Int, private val bottomMargin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = topMargin
                bottom = bottomMargin
            }
        }
    }
}