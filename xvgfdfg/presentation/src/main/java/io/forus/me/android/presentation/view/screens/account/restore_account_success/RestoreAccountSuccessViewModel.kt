package io.forus.me.android.presentation.view.screens.account.restore_account_success

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RestoreAccountSuccessViewModel: ViewModel() {

    private var _token =  MutableLiveData<String>()
    val token get() = _token
    fun setToken(token: String){
        _token.value = token
    }

    private var _isExchangeToken =  MutableLiveData<Boolean>(true)
    val isExchangeToken get() = _isExchangeToken
    fun setIsExchangeToken(isExchangeToken: Boolean){
        _isExchangeToken.value = isExchangeToken
    }


}