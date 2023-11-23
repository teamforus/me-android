package io.forus.me.android.presentation.view.screens.lock.pin

data class PinLockModel(
        val state: State = State.CONFIRM,
        val prevState: State = State.CONFIRM
)
{
    enum class State {
        CONFIRM, CHECKING, WRONG_PIN, SUCCESS
    }

    fun changeState(newState: State = this.state): PinLockModel = copy(prevState = state, state = newState)
}