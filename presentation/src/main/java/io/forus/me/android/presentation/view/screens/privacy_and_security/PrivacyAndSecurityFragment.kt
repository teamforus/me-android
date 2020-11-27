package io.forus.me.android.presentation.view.screens.privacy_and_security

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.text.HtmlCompat
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import kotlinx.android.synthetic.main.privacy_and_security_fragment.*

class PrivacyAndSecurityFragment : Fragment() {

    companion object {
        fun newInstance() = PrivacyAndSecurityFragment()
    }

    private lateinit var viewModel: PrivacyAndSecurityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.privacy_and_security_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(PrivacyAndSecurityViewModel::class.java)


        closeBt.setOnClickListener {
            activity?.finish()
        }

        initUI()
    }

    fun initUI(){

        val txt1 = getString(R.string.privacy_send_email_to)
        val txt2 = getString(R.string.support_email)
        val text = "$txt1 <b>$txt2</b>"
        sendEmailToSupportBt.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)

        sendEmailToSupportBt.setOnClickListener {

        }
    }

}