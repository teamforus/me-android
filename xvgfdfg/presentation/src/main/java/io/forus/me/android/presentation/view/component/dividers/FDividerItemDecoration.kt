package io.forus.me.android.presentation.view.component.dividers

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import io.forus.me.android.presentation.helpers.Converter


class FDividerItemDecoration : RecyclerView.ItemDecoration  {

    private val ATTRS = intArrayOf(android.R.attr.listDivider)

    private var divider: Drawable? = null

    private lateinit var context: Context


    constructor (context: Context) {
        val styledAttributes = context.obtainStyledAttributes(ATTRS)
        this.context = context
        divider = styledAttributes.getDrawable(0)
        styledAttributes.recycle()
    }



    constructor(context: Context, resId: Int) {
        this.context = context
        divider = ContextCompat.getDrawable(context, resId)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = Converter.convertDpToPixel(20.0f, this.context)
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + divider!!.intrinsicHeight

            divider!!.setBounds(left, top, right, bottom)
            divider!!.draw(c)
        }
    }
}