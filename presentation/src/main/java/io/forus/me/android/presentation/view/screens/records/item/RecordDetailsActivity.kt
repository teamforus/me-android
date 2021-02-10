package io.forus.me.android.presentation.view.screens.records.item

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.SlidingPanelActivity
import io.forus.me.android.presentation.view.screens.records.item.qr.RecordQRFragment
import kotlinx.android.synthetic.main.activity_toolbar_sliding_panel.*

class RecordDetailsActivity : SlidingPanelActivity() {


    companion object {
        private val RECORD_ID_EXTRA = "RECORD_ID_EXTRA"

        fun getCallingIntent(context: Context, record: Record): Intent {
            val intent = Intent(context, RecordDetailsActivity::class.java)
            intent.putExtra(RECORD_ID_EXTRA, record.id)
            return intent
        }
    }

    override val height: Float
        get() = 400f

    private lateinit var fragment: RecordDetailsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            fragment = RecordDetailsFragment.newIntent(intent.getLongExtra(RECORD_ID_EXTRA, 0))
            addFragment(R.id.fragmentContainer, fragment)
        }
    }

    fun showPopupQRFragment(recordId: Long, recordName: String?) {
        addPopupFragment(RecordQRFragment.newIntent(recordId, recordName ?: ""), "QR code")
    }

    fun closeQRFragment() {
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        fragment.updateModel()
    }
}