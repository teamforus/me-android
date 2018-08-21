package io.forus.me.android.presentation.view.screens.records.newrecord.viewholders

import android.view.View
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.presentation.R

class SelectedTypeVH(root: View) {

    var tv_name: io.forus.me.android.presentation.view.component.text.TextView

    init {
        tv_name = root.findViewById(R.id.tv_name)
    }

    fun render(item: RecordType) {
        tv_name.text = item.name
    }
}