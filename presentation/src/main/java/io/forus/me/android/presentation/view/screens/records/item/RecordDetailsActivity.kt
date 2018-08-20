package io.forus.me.android.presentation.view.screens.records.item

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.interfaces.SlidingToolbarFragmentActionListener
import io.forus.me.android.presentation.view.activity.CommonActivity

import kotlinx.android.synthetic.main.activity_toolbar_sliding_panel.*

class RecordDetailsActivity : CommonActivity() {


    companion object {
        private val RECORD_ID_EXTRA = "RECORD_ID_EXTRA"

        fun getCallingIntent(context: Context, record: Record): Intent {
            val intent = Intent(context, RecordDetailsActivity::class.java)
            intent.putExtra(RECORD_ID_EXTRA, record.id)
            return intent
        }
    }

    override val viewID: Int
        get() = R.layout.activity_toolbar_sliding_panel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val fragment = RecordDetailsFragment.newIntent(intent.getLongExtra(RECORD_ID_EXTRA, 0))
            fragment.slidingToolbarFragmentActionListener = object : SlidingToolbarFragmentActionListener {
                override fun openPanel() {
                    sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                }
            }
            addFragment(R.id.fragmentContainer, fragment)

            addFragment(R.id.fragmentPanelContainer, fragment.slidingFragment)
            sliding_panel_title.text = fragment.slidingPanelTitle
        }
    }
}