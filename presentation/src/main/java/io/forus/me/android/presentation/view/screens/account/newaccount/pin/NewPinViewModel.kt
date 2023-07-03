package io.forus.me.android.presentation.view.screens.account.newaccount.pin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewPinViewModel: ViewModel() {

    private var _accessToken =  MutableLiveData<String>()
    val accessToken get() = _accessToken
    fun setAccessToken(accessToken: String){
        _accessToken.value = accessToken
    }
}