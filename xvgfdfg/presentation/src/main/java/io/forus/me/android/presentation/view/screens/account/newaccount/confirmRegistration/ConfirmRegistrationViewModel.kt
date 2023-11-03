package io.forus.me.android.presentation.view.screens.account.newaccount.confirmRegistration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfirmRegistrationViewModel: ViewModel() {

    private var _token =  MutableLiveData<String>("")
    val token get() = _token
    fun setToken(token: String){
        _token.value = token
    }
}