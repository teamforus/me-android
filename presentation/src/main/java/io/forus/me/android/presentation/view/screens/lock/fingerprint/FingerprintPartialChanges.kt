package io.forus.me.android.presentation.view.screens.lock.fingerprint

import io.forus.me.android.presentation.view.base.lr.PartialChange

sealed class FingerprintPartialChanges : PartialChange {

    data class UnlockSuccess(val void: Unit) : FingerprintPartialChanges()

    data class UnlockFail(val reason: String) : FingerprintPartialChanges()

    data class Exit(val void: Unit) : FingerprintPartialChanges()

}