package io.forus.me.android.presentation.view.screens.account.login_signup_account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginSignUpViewModel: ViewModel() {

    private var _token =  MutableLiveData<String>("")
    val token get() = _token
    fun setToken(token: String){
        _token.value = token
    }
}