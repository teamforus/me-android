package io.forus.me.android.presentation.view.screens.privacy_and_security

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.text.HtmlCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.screens.privacy_and_security.pdfViewer.ReportPdfActivity
import io.forus.me.android.presentation.R
import kotlinx.android.synthetic.main.privacy_and_security_fragment.*



class PrivacyAndSecurityFragment : Fragment() {

    val privacyUrl = "https://media.forus.io/static/Privacybeleid.pdf"

    val supportEmail = "support@forus.io"

    val dataProtectionAuthorityUrl = "https://autoriteitpersoonsgegevens.nl/nl/onderwerpen/avg-europese-privacywetgeving"

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
            composeEmail()
        }

        dataProtectionAuthority.setOnClickListener {
            val uris = Uri.parse(dataProtectionAuthorityUrl)
            val intents = Intent(Intent.ACTION_VIEW, uris)
            val b = Bundle()
            b.putBoolean("new_window", false)
            intents.putExtras(b)
            requireActivity().startActivity(intents)
        }

        privacyBt.setOnClickListener {

            /*val uris = Uri.parse(privacyUrl)
            val intents = Intent(Intent.ACTION_VIEW, uris)
            val b = Bundle()
            b.putBoolean("new_window", false)
            intents.putExtras(b)*/
            requireActivity().startActivity(ReportPdfActivity.getCallingIntent(requireContext(),privacyUrl))

            /*val containerViewId = (requireActivity() as PrivacyAndSecurityActivity)
                    .getFragmentContainerId()
            val webViewFragment = WebViewFragment.newInstance(privacyUrl)
            fragmentManager!!
                    .beginTransaction()
                    .replace(containerViewId, webViewFragment)
                    .addToBackStack(null)
                    .commit()*/
        }
    }


    fun composeEmail() {
        val uri = Uri.parse("mailto:$supportEmail")
                .buildUpon()
                .appendQueryParameter("subject", "")
                .appendQueryParameter("body", "")
                .build()

        val emailIntent = Intent(Intent.ACTION_SENDTO, uri)
        startActivity(Intent.createChooser(emailIntent, ""))
    }

}