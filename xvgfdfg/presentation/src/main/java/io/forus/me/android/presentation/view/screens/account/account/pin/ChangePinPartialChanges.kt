package io.forus.me.android.presentation.view.screens.account.account.pin

import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class ChangePinPartialChanges : PartialChange {

    data class PinOnComplete(val passcode: String) : ChangePinPartialChanges()

    data class PinOnChange(val passcode: String) : ChangePinPartialChanges()

    data class CheckPinSuccess(val void: Unit) : ChangePinPartialChanges()

    data class CheckPinError(val void: Unit) : ChangePinPartialChanges()

    data class ChangePinEnd(val void: Unit) : ChangePinPartialChanges()

    data class ChangePinError(val void: Unit) : ChangePinPartialChanges()

}