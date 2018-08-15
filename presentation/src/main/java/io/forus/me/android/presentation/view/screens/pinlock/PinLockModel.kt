package io.forus.me.android.presentation.view.screens.pinlock

data class PinLockModel(
        val state: State = State.CONFIRM,
        val prevState: State = State.CONFIRM
)
{
    enum class State {
        CONFIRM, CHECKING, WRONG_PIN, SUCCESS
    }
}