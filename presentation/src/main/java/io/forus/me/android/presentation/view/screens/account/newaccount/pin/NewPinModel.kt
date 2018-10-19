package io.forus.me.android.presentation.view.screens.account.newaccount.pin

data class NewPinModel(
        val state: State = State.CREATE,
        val prevState: State = State.CREATE,
        val skipEnabled: Boolean = true,
        val passcode: String? = null,
        val accessToken: String? = null,
        val createIdentityError: Throwable? = null
)
{
    enum class State {
        CREATE, CONFIRM, PASS_NOT_MATCH, CREATING_IDENTITY, CREATING_IDENTITY_ERROR,
    }

    fun changeState(newState: State = this.state, passcode: String? = this.passcode, skipEnabled: Boolean = false): NewPinModel = copy(prevState = state, state = newState, passcode = passcode, skipEnabled = skipEnabled)

    val valid: Boolean
        get() {
            return passcode != null && passcode.length == 4 && accessToken != null && accessToken.isNotEmpty()
        }

}