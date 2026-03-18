package io.forus.me.android.presentation.view.screens.vouchers.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.forus.me.android.presentation.firestore_logging.FirestoreTokenManager

class ProviderViewModelFactory(private val firestoreTokenManager: FirestoreTokenManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProviderViewModel::class.java)) {
            return ProviderViewModel(firestoreTokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ProviderViewModel constructor(private val firestoreTokenManager: FirestoreTokenManager) :
    ViewModel() {


}


