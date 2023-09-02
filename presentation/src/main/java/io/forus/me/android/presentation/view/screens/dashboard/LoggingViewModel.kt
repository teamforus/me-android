package io.forus.me.android.presentation.view.screens.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.forus.me.android.domain.models.account.Account
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.view.screens.account.account.AccountModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoggingViewModelFactory(private val accountRepository: AccountRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoggingViewModel::class.java)) {
            return LoggingViewModel(accountRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class LoggingViewModel constructor(private val accountRepository: AccountRepository): ViewModel() {

    val TAG = "FirestoreLogger"

    private var _account =  MutableLiveData<Account>()
    val account get() = _account

    private var _firestoreToken =  MutableLiveData<String>()
    val firestoreToken get() = _firestoreToken

    fun getAccountData(){

        accountRepository. getAccount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { account ->
                    // onNext() event, called when the Account data is available.
                    // 'account' here is an instance of Account emitted by the Observable.
                    Log.d(TAG, "Account: $account")
                    this._account.postValue(account)

                    getFirestoreToken(account.email)
                },
                { error ->
                    // onError() event, called when an error occurs.
                    // 'error' here is the error throwable.
                    Log.e(TAG, "Error: ${error.message}")
                },
                {
                    // onComplete() event, called when the Observable has emitted all items.
                    Log.d(TAG, "Completed")
                }
            )
    }


    fun getFirestoreToken(userUid: String){
        accountRepository.getFirestoreToken(userUid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { firestoreToken ->
                    // onNext() event, called when the Account data is available.
                    // 'account' here is an instance of Account emitted by the Observable.
                    Log.d(TAG, "Firestore token: $firestoreToken")
                    this._firestoreToken.value = firestoreToken


                },
                { error ->
                    // onError() event, called when an error occurs.
                    // 'error' here is the error throwable.
                    Log.e(TAG, "Error: ${error.message}")
                },
                {
                    // onComplete() event, called when the Observable has emitted all items.
                    Log.d(TAG, "Completed")
                }
            )
    }




   /* fun getTransactions(){
        val db = FirebaseFirestore.getInstance()

        db.collection("Transactions").get().addOnCompleteListener {
            if(it.isSuccessful){
                val result = it.result.documents
                Log.d(TAG, "Writing document ... $result")
            }
        }
    }

    fun writeTransaction(){

        val db = FirebaseFirestore.getInstance()


        val transaction = hashMapOf(
            "voucher_id" to "123",
            "type" to "product",
            "user" to "user1",
            "amount" to 200,
            "dateTime" to Timestamp.now()
        )
        Log.w(TAG, "Writing document ... ")
        db.collection("Transactions")
            .add(transaction)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }*/


}


