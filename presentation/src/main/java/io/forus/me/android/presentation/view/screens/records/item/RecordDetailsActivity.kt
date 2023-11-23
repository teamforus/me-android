package io.forus.me.android.presentation.view.screens.records.item

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.MeBottomSheetDialogFragment
import io.forus.me.android.presentation.view.activity.CommonActivity
import io.forus.me.android.presentation.view.screens.records.item.qr.RecordQRFragment

class RecordDetailsActivity : CommonActivity() {


    companion object {
        private val RECORD_ID_EXTRA = "RECORD_ID_EXTRA"

        fun getCallingIntent(context: Context, record: Record): Intent {
            val intent = Intent(context, RecordDetailsActivity::class.java)
            intent.putExtra(RECORD_ID_EXTRA, record.id)
            return intent
        }
    }


    private lateinit var fragment: RecordDetailsFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            fragment = RecordDetailsFragment.newIntent(intent.getLongExtra(RECORD_ID_EXTRA, 0))
            addFragment(R.id.fragmentContainer, fragment)
        }

    }

    fun showPopupQRFragment(recordId: Long){

        val meBottomSheet = MeBottomSheetDialogFragment.newInstance(
            RecordQRFragment.newIntent(recordId), "QR code")
        meBottomSheet.show(supportFragmentManager, meBottomSheet.tag)
    }

    fun closeQRFragment(){
        fragment.updateModel()
    }
}