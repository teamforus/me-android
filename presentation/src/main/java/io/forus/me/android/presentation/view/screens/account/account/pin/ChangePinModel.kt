package io.forus.me.android.presentation.view.screens.account.account.pin

data class ChangePinModel(
        val state: State = State.CONFIRM_OLD_PIN,
        val prevState: State = State.CONFIRM_OLD_PIN,
        val passcodeOld: String? = null,
        val passcodeNew: String? = null
)
{
    enum class State {
        CONFIRM_OLD_PIN, CHECKING_OLD_PIN, WRONG_OLD_PIN, CREATE_NEW_PIN, CONFIRM_NEW_PIN, PASS_NOT_MATCH, CHANGING_PIN, CHANGE_PIN_ERROR,
    }

    fun changeState(newState: State = this.state, passcodeOld: String? = this.passcodeOld, passcodeNew: String? = this.passcodeNew): ChangePinModel = copy(prevState = state, state = newState, passcodeOld = passcodeOld, passcodeNew = passcodeNew)

    val valid: Boolean
        get() {
            return passcodeOld != null && passcodeOld.length == 4 && passcodeNew != null && passcodeNew.length == 4
        }

}