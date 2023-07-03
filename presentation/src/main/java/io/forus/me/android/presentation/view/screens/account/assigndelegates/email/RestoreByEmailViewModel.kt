package io.forus.me.android.presentation.view.screens.account.assigndelegates.email

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RestoreByEmailViewModel: ViewModel() {

    private var _token =  MutableLiveData<String>("")
    val token get() = _token
    fun setToken(token: String){
        _token.value = token
    }
}