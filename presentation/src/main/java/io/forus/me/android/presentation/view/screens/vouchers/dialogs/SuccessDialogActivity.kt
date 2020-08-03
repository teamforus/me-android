package io.forus.me.android.presentation.view.screens.vouchers.dialogs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import io.forus.me.android.presentation.R
import kotlinx.android.synthetic.main.dialog_fullscreen.*


class SuccessDialogActivity : AppCompatActivity() {

    companion object {

        val TITLE_EXTRA = "TITLE_EXTRA"
        val DESCRIPTION_EXTRA = "DESCRIPTION_EXTRA"
        val SUBMIT_EXTRA = "SUBMIT_EXTRA"

        fun getCallingIntent(context: Context, title: String, description: String = "", submitButtonText: String? = "OK"): Intent {
            val intent = Intent(context, SuccessDialogActivity::class.java)
            intent.putExtra(TITLE_EXTRA, title)
            intent.putExtra(DESCRIPTION_EXTRA, description)
            intent.putExtra(SUBMIT_EXTRA, submitButtonText)
            return intent
        }
    }


    var titleTxt: String = ""
    var descriptionTxt: String = ""
    var submitButtonText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_fullscreen)

        if(intent != null) {
            titleTxt = intent.getStringExtra(TITLE_EXTRA)
            descriptionTxt = intent.getStringExtra(DESCRIPTION_EXTRA)
            submitButtonText = intent.getStringExtra(SUBMIT_EXTRA)
        }


        titleTV.text = titleTxt
        description.text = descriptionTxt
        submitButton.text = submitButtonText
        submitButton.setOnClickListener { finish() }


    }

}
