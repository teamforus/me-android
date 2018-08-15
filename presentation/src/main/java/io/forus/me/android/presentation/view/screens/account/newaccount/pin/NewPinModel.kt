package io.forus.me.android.presentation.view.screens.account.newaccount.pin

data class NewPinModel(
        val state: State = State.CREATE,
        val prevState: State = State.CREATE,
        val passcode: String? = null,
        val access_token: String? = null
)
{
    enum class State {
        CREATE, CONFIRM, PASS_NOT_MATCH, CREATING_IDENTITY, CREATING_IDENTITY_ERROR,
    }

    val valid: Boolean
        get() {
            return passcode != null && passcode.length == 4 && access_token != null && access_token.isNotEmpty()
        }
}