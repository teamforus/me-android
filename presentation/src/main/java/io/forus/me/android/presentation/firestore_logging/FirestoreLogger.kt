package io.forus.me.android.presentation.firestore_logging

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.math.BigDecimal

object FirestoreLogger {

    private const val TAG = "FirestoreLogger"

    fun writeTransaction(address: String, amount: BigDecimal, note: String?,
                         organizationId: Long, success: Boolean, error: String?)
    {

        val currentUser = FirebaseAuth.getInstance().currentUser

        if(currentUser == null){
            Log.d(TAG, "Firebase user is null")
            return
        }

        val db = FirebaseFirestore.getInstance()

        val transaction = hashMapOf(
            "address" to address,
            "amount" to amount.toFloat(),
            "note" to note,
            "organizationId" to organizationId,
            "success" to success,
            "error" to error,
            "user" to currentUser.uid,
            "dateTime" to Timestamp.now()
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
}