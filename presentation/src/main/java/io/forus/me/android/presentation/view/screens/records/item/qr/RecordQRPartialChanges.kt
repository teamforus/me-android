package io.forus.me.android.presentation.view.screens.records.item.qr

import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange

sealed class RecordQRPartialChanges : PartialChange {

    class RecordValidated() : RecordQRPartialChanges()

}