package io.forus.me

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.IdentityService

import kotlinx.android.synthetic.main.activity_create_identity.*
import java.util.*
import java.util.concurrent.Callable

class CreateIdentityActivity : AppCompatActivity() {

    private val canCancel: Boolean
    get() = ThreadHelper.await(Callable {
        IdentityService.anyExists()
    })

    fun cancel(view: View) {
        if (canCancel) {
            val intent = Intent()
            setResult(ResultCode.CANCEL, intent)
            finish()
        }
    }

    fun create(view: View) {
        if (name_view.text.isEmpty()) {
            name_view.error = String.format(getString(R.string.value_cannot_be_empty), getString(R.string.name))
            return
        }
        val text = name_view.text.toString()
        if (ThreadHelper.await(Callable {
            IdentityService.anyExists(withName = text) }))
        {
            name_view.error = String.format(getString(R.string.value_already_exists), getString(R.string.name))
            return
        }
        setContentView(R.layout.loading)
        val isFirst = !canCancel // Can't cancel because it is your first identity
        val newIdentity = ThreadHelper.await(Callable {

            // TODO Make actual address
            IdentityService.newIdentity(Calendar.getInstance().timeInMillis.toString(), text)
        })
        IdentityService.setCurrentIdentity(this, newIdentity)
        val intent = Intent()
        intent.putExtra(Result.IS_FIRST, isFirst)
        setResult(ResultCode.CREATED, intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_identity)
        setSupportActionBar(toolbar)

        if (!canCancel) {
            cancelButton.visibility = View.GONE
        }
    }

    class RequestCode {
        companion object {
            const val REQUEST_IDENTITY = 301
        }
    }

    class ResultCode {
        companion object {
            const val CANCEL = 312
            const val CREATED = 311
        }
    }

    class Result {
        companion object {
            const val IS_FIRST = "isFirst"
        }
    }

}
