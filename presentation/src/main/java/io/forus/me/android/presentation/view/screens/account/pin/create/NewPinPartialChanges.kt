package io.forus.me.android.presentation.view.screens.account.pin.create

import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange

sealed class NewPinPartialChanges : PartialChange {

    data class PinOnComplete(val passcode: String) : NewPinPartialChanges()

    data class PinOnChange(val passcode: String) : NewPinPartialChanges()

    data class CreateIdentityEnd(val void: Unit) : NewPinPartialChanges()

    data class CreateIdentityError(val void: Unit) : NewPinPartialChanges()

}