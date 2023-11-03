package io.forus.me.android.presentation.view.component.editors

import android.content.Context
import android.util.AttributeSet
import io.forus.me.android.presentation.R

class EditTextOutlined : EditText {

    override val layout: Int
        get() = R.layout.view_edit_text_outlined

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}