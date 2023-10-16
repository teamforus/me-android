package io.forus.me.android.presentation.view.screens.vouchers.dialogs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.DialogFullscreenBinding


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

    private lateinit var binding: DialogFullscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogFullscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent != null) {
            titleTxt = intent.getStringExtra(TITLE_EXTRA)?:""
            descriptionTxt = intent.getStringExtra(DESCRIPTION_EXTRA)?:""
            submitButtonText = intent.getStringExtra(SUBMIT_EXTRA)?:""
        }


        binding.titleTV.text = titleTxt
        binding.description.text = descriptionTxt
        binding.submitButton.text = submitButtonText
        binding.submitButton.setOnClickListener { finish() }


    }

}
