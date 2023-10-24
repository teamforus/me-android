package io.forus.me.android.presentation.view.screens.dashboard
/*
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.forus.me.android.domain.models.account.Account
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.firestore_logging.FirestoreTokenManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoggingViewModelFactory(private val firestoreTokenManager: FirestoreTokenManager) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoggingViewModel::class.java)) {
            return LoggingViewModel(firestoreTokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class LoggingViewModel constructor(private val firestoreTokenManager: FirestoreTokenManager) : ViewModel() {


    fun authorizeFirestore(){
        firestoreTokenManager.authorizeFirestore{
            Log.d("FirestoreLogger","Firestore success authorized!")
        }
    }

}

*/
