package io.forus.me.android.presentation.view.screens.privacy_and_security.pdfViewer

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.forus.me.android.presentation.R
import kotlinx.android.synthetic.main.activity_pdf_viewer.*
import java.io.InputStream
import java.lang.ref.WeakReference
import java.net.URL


class ReportPdfActivity : AppCompatActivity() {


    companion object {

        private val URL_EXTRA = "URL_EXTRA"

        fun getCallingIntent(context: Context?, urlStr: String): Intent? {
            val intent = Intent(context, ReportPdfActivity::class.java)
            intent.putExtra(URL_EXTRA, urlStr)
            return intent
        }

        class PdfAsyncTask internal constructor(context: ReportPdfActivity, urlStr: String) : AsyncTask<Unit, Unit, InputStream>() {
            private val activityReference: WeakReference<ReportPdfActivity> = WeakReference(context)
            private val urlReference: WeakReference<String> = WeakReference(urlStr)
            override fun onPreExecute() {
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                //activity.progressBar.visibility = View.VISIBLE
            }

            override fun doInBackground(vararg params: Unit): InputStream {
                return URL(urlReference.get()).openStream()
            }

            override fun onPostExecute(result: InputStream) {
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
               // activity.progressBar.visibility = View.GONE
                activity.pdfView.fromStream(result).load()
            }

        }
    }

    var urlStr: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)
        setSupportActionBar(findViewById(R.id.toolbar))

        val b = intent.extras
        if (b != null) {
            try {
                urlStr = b.getString(URL_EXTRA)
            } catch (ignored: Exception) {
            }

        }

        closeView.setOnClickListener { finish() }


        loadPdf()

    }

    private fun loadPdf() {
        PdfAsyncTask(this, urlStr!!).execute()
    }




}