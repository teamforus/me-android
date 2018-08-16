package io.forus.me.android.presentation.view.screens.pinlock

import com.ocrv.ekasui.mrm.ui.loadRefresh.PartialChange

sealed class PinLockPartialChanges : PartialChange {

    data class PinOnComplete(val passcode: String) : PinLockPartialChanges()

    data class PinOnChange(val passcode: String) : PinLockPartialChanges()

    data class CheckPinSuccess(val void: Unit) : PinLockPartialChanges()

    data class CheckPinError(val void: Unit) : PinLockPartialChanges()

    data class Exit(val void: Unit) : PinLockPartialChanges()

}