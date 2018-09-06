package io.forus.me.android.presentation.view.screens.account.newaccount.pin

import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class NewPinPartialChanges : PartialChange {

    data class PinOnComplete(val passcode: String) : NewPinPartialChanges()

    data class PinOnChange(val passcode: String) : NewPinPartialChanges()

    data class CreateIdentityEnd(val void: Unit) : NewPinPartialChanges()

    data class CreateIdentityError(val void: Unit) : NewPinPartialChanges()

    class SkipPin() : NewPinPartialChanges()

}