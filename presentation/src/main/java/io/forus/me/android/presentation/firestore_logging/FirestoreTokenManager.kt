package io.forus.me.android.presentation.firestore_logging

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.forus.me.android.domain.repository.account.AccountRepository
import io.forus.me.android.presentation.BuildConfig
import io.forus.me.android.presentation.extensions.maskStartingFromPreservingTail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal

class FirestoreTokenManager constructor(
    private val accountRepository: AccountRepository) {
    private val TAG = "FirestoreLogger"



    private fun getServerApiKey() =
        if (BuildConfig.SERVER_API_KEY.isNullOrEmpty()) null else BuildConfig.SERVER_API_KEY




    public fun authorizeFirestore(onComplete: (()->(Unit))?) {
        getServerApiKey()?.let { serverApiKey ->
            getFirestoreToken(serverApiKey, onComplete)
        } ?: kotlin.run {
            Log.e(
                TAG,
                "The `SERVER_API_KEY` in the configuration has not been properly initialized, possibly due to the absence of the `secrets.properties` file."
            )
        }
    }

    private fun getFirestoreToken(serverApiKey: String,onComplete: (()->(Unit))?) {
        accountRepository.getFirestoreToken(serverApiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { firestoreToken ->
                    Log.d(TAG, "Firestore token: $firestoreToken")
                    firestoreToken?.let {
                        registerFirestoreUser(firestoreToken, onComplete)
                    }
                },
                { error ->
                    Log.d(TAG, "Firestore token: $error")
                    error.printStackTrace()
                }
            )


    }


    private fun registerFirestoreUser(firestoreToken: String, onComplete: (()->(Unit))?) {

        FirebaseAuth.getInstance().signInWithCustomToken(firestoreToken)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                     Log.d(TAG,"Firestore user uid: ${user?.uid}")
                    onComplete?.invoke()
                }else {
                    Log.w("FirestoreLogger", "signInWithCustomToken:failure", task.exception)
                }
            }
            .addOnFailureListener {
                Log.e( TAG, it.localizedMessage)
            }
    }
    //

    public fun writeTransaction(
        address: String, amount: BigDecimal, note: String?,
        organizationId: Long, success: Boolean, error: String?
    ) {

        if (FirebaseAuth.getInstance().currentUser == null) {
            authorizeFirestore{
                logTransaction(address,amount,note,organizationId,success,error)
            }
        }else{
            logTransaction(address,amount,note,organizationId,success,error)
        }
    }

    private fun logTransaction(
        address: String, amount: BigDecimal, note: String?,
        organizationId: Long, success: Boolean, error: String?
    ) {

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            Log.d(TAG, "Firebase user is null")
            return
        }

        val db = FirebaseFirestore.getInstance()


        val transaction = hashMapOf(
            "request" to hashMapOf(
                "address" to address.maskStartingFromPreservingTail(11, '*', 4),
                "amount" to amount.toFloat(),
                "note" to note,
                "organizationId" to organizationId,
                "user" to currentUser?.uid,
                "dateTime" to Timestamp.now()
            ),
            "response" to when (success) {
                true -> true
                false -> null
            },
            "error" to when (success) {
                true -> null
                false -> hashMapOf("error" to error)
            },
            "meta" to hashMapOf<String, Any>(
                "application_id" to BuildConfig.APPLICATION_ID,
                "version" to BuildConfig.VERSION_CODE
            )
        )

        db.collection("Transactions")
            .add(transaction)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }


    //GetVoucherAsProvider

    public fun writeGetVoucherAsProvider(
        address: String,  success: Boolean, error: String?
    ) {

        if (FirebaseAuth.getInstance().currentUser == null) {
            authorizeFirestore{
                logGetVoucherAsProvider(address,success,error)
            }
        }else{
            logGetVoucherAsProvider(address,success,error)
        }
    }

    private fun logGetVoucherAsProvider(
        address: String,  success: Boolean, error: String?
    ) {

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            Log.d(TAG, "Firebase user is null")
            return
        }

        val db = FirebaseFirestore.getInstance()


        val transaction = hashMapOf(
            "request" to hashMapOf(
                "address" to address.maskStartingFromPreservingTail(11, '*', 4),
                "user" to currentUser?.uid,
                "dateTime" to Timestamp.now()
            ),
            "response" to when (success) {
                true -> address
                false -> null
            },
            "error" to when (success) {
                true -> null
                false -> hashMapOf("error" to error)
            },
            "meta" to hashMapOf<String, Any>(
                "application_id" to BuildConfig.APPLICATION_ID,
                "version" to BuildConfig.VERSION_CODE
            )
        )

        db.collection("GetVoucherAsProvider")
            .add(transaction)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }


    //GetProductVoucherAsProvider

    public fun writeGetProductVoucherAsProvider(
        address: String,  success: Boolean, error: String?
    ) {

        if (FirebaseAuth.getInstance().currentUser == null) {
            authorizeFirestore{
                logGetProductVoucherAsProvider(address,success,error)
            }
        }else{
            logGetProductVoucherAsProvider(address,success,error)
        }
    }

    private fun logGetProductVoucherAsProvider(
        address: String,  success: Boolean, error: String?
    ) {

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            Log.d(TAG, "Firebase user is null")
            return
        }

        val db = FirebaseFirestore.getInstance()


        val transaction = hashMapOf(
            "request" to hashMapOf(
                "address" to address.maskStartingFromPreservingTail(11, '*', 4),
                "user" to currentUser?.uid,
                "dateTime" to Timestamp.now()
            ),
            "response" to when (success) {
                true -> address
                false -> null
            },
            "error" to when (success) {
                true -> null
                false -> hashMapOf("error" to error)
            },
            "meta" to hashMapOf<String, Any>(
                "application_id" to BuildConfig.APPLICATION_ID,
                "version" to BuildConfig.VERSION_CODE
            )
        )

        db.collection("GetProductVoucherAsProvider")
            .add(transaction)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

}