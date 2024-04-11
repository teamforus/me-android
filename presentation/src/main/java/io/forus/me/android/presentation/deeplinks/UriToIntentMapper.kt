package io.forus.me.android.presentation.deeplinks

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import io.forus.me.android.presentation.navigation.Navigator

class UriToIntentMapper(private val mContext: Context, private val navigator: Navigator) {

    fun dispatchIntent(intent: Intent) {
        val uri = intent.data
        var dispatchIntent: Intent? = null

        if (uri == null) throw IllegalArgumentException("Uri cannot be null")

        Log.d("forus", "UriToIntentMapper=$uri")

        val scheme = uri.scheme!!.toLowerCase()

        if ("meapp" == scheme) {
            dispatchIntent = mapAppLink(uri)
        }

        if (dispatchIntent != null) {
            mContext.startActivity(dispatchIntent)
        }
    }

    private fun mapAppLink(uri: Uri): Intent? {
        val host = uri.host!!.toLowerCase()

        Log.d("forus", "host=$host") // host=identity-restore
        when (host) {
            "identity-restore" -> {
                val bQuery = uri.getQueryParameter("token")
                bQuery?.let { navigator.navigateToAccountRestoreByEmailExchangeToken(mContext, it) }


            }
            "identity-confirmation" -> {
                val bQuery = uri.getQueryParameter("token")
                bQuery?.let { navigator.navigateToResoreAccountSuccess(mContext, it, true) }
            }
        }
        return null
    }
}