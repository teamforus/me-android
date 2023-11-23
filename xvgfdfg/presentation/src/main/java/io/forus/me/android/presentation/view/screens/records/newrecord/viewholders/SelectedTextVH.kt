package io.forus.me.android.presentation.view.screens.records.newrecord.viewholders

import android.view.View
import android.widget.RelativeLayout
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.presentation.R

class SelectedTextVH(root: View) {

    var tv_name: io.forus.me.android.presentation.view.component.text.TextView
    var rl_image: RelativeLayout

    init {
        tv_name = root.findViewById(R.id.tv_name)
        rl_image = root.findViewById(R.id.rl_image)
    }

    fun render(item: String) {
        tv_name.text = item
        rl_image.visibility = View.INVISIBLE
    }
}