package io.forus.me.android.presentation.view.screens.privacy_and_security

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import io.forus.me.android.presentation.R
import kotlinx.android.synthetic.main.fragment_web_view.*


private const val EXTRA_URL = "EXTRA_URL"

class WebViewFragment : Fragment() {
    private var url: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            url = it.getString(EXTRA_URL)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(url: String) =
                WebViewFragment().apply {
                    arguments = Bundle().apply {
                        putString(EXTRA_URL, url)

                    }
                }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webview.settings.javaScriptEnabled = true

        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }


        webview.loadUrl(url)

    }
}