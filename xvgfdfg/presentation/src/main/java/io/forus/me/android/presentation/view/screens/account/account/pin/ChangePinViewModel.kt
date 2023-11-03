package io.forus.me.android.presentation.view.screens.account.account.pin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.forus.me.android.presentation.models.ChangePinMode

class ChangePinViewModel: ViewModel() {

    private var _changePinMode =  MutableLiveData<ChangePinMode>()
    val changePinMode get() = _changePinMode
    fun setPinMode(changePinMode: ChangePinMode){
        _changePinMode.value = changePinMode
    }
}