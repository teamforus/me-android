package io.forus.me

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import io.forus.me.entities.*
import io.forus.me.entities.base.EthereumItem
import io.forus.me.helpers.QrHelper
import io.forus.me.helpers.ThreadHelper
import io.forus.me.services.*
import kotlinx.android.synthetic.main.activity_qr_result.*
import java.util.concurrent.Callable

/**
 * Created by martijn.doornik on 03/05/2018.
 */
class QrResultActivity : AppCompatActivity() {
    companion object {
        const val RESULT = "result"
    }

    private fun cancel() {
        setResult(ResultCodes.CANCEL)
        finish()
    }

    override fun onResume() {
        super.onResume()
        val data = intent.getStringExtra(RESULT)
        if (data != null) {
            this.setContentView(R.layout.loading)
            val item = EthereumItem.fromString(data)
            if (item != null) {
                this.setupEthereumItemView(item)
            }
        }
    }

    private fun positiveResult(item: EthereumItem, force: Boolean = false) {
        if (item is Record) {
            if(item.recordCategoryId == RecordService.CategoryIdentifier.OTHER && !force) {
                val categories = RecordService.getRecordCategoriesByIdentity()
                val options: Array<String> = categories.map { resources.getString(it.labelResource) }.toTypedArray()
                val dialog = AlertDialog.Builder(this)
                        .setSingleChoiceItems(options, categories.indexOf(categories.singleOrNull { it.id == item.recordCategoryId }?:0), { container, selected ->
                            val chosen = categories[selected]
                            item.recordCategoryId = chosen.id
                            container.dismiss()
                            // Even if the person has chosen other as the chosen one, do not care for it.
                            positiveResult(item, true)
                        })
                val titleView = LayoutInflater.from(this).inflate(R.layout.qr_result_record_category_selection_title, null)
                dialog.setCustomTitle(titleView)
                dialog.show()
                return
            }
        }
        this.setContentView(R.layout.loading)
        val handler = Handler()
        handler.postDelayed({
            val result = ThreadHelper.await(Callable<Int> {
                when (item) {
                    is Asset -> {
                        DatabaseService.inject.assetDao().insert(item)
                        ResultCodes.NEW_ASSET
                    }
                    is Record -> {
                        DatabaseService.inject.recordDao().insert(item)
                        ResultCodes.NEW_RECORD
                    }
                    is Token -> {
                        DatabaseService.inject.tokenDao().insert(item)
                        ResultCodes.NEW_TOKEN
                    }
                    is Voucher -> {
                        DatabaseService.inject.voucherDao().insert(item)
                        ResultCodes.NEW_VOUCHER
                    }
                    else -> ResultCodes.NO_ACTION
                }
            })
            setResult(result)
            finish()
        }, 200)
    }

    private fun setupEthereumItemView(item: EthereumItem) {
        val task = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<Any?, Any?, Bitmap>() {
            override fun doInBackground(vararg params: Any?): Bitmap {
                return QrHelper.getQrBitmap(baseContext, item.toJson(), QrHelper.Sizes.LARGE)
            }

            override fun onPostExecute(result: Bitmap) {
                super.onPostExecute(result)
                setContentView(R.layout.activity_qr_result)
                toolbar.title = item.name
                addressText.setAddress(item.address)
                infoText.text = String.format(infoText.text.toString(), item.name)
                qrView.setImageBitmap(result)
                positiveButton.setOnClickListener {
                    positiveResult(item)
                }
                negativeButton.setOnClickListener {
                    cancel()
                }
            }
        }
        task.execute()
    }

    class RequestCodes {
        companion object {
            const val NEW_RESULT = 501
        }
    }

    class ResultCodes {
        companion object {
            const val CANCEL = 520
            const val ERROR = 599
            const val NEW_ASSET = 511
            const val NEW_TOKEN = 513
            const val NEW_RECORD = 512
            const val NEW_VOUCHER = 514
            const val NO_ACTION = 515
        }
    }
}