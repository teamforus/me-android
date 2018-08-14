package io.forus.me.android.presentation.view.screens.records.item

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.domain.models.records.Record
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.ToolbarActivity

class RecordDetailsActivity : ToolbarActivity() {


    companion object {
        private val RECORD_ID_EXTRA = "RECORD_ID_EXTRA"

        fun getCallingIntent(context: Context, record: Record): Intent {
            val intent = Intent(context, RecordDetailsActivity::class.java)
            intent.putExtra(RECORD_ID_EXTRA, record.id)
            return intent
        }
    }

    lateinit var record: Record

    lateinit var fragment : RecordDetailsFragment

    override val toolbarType: ToolbarType
        get() = ToolbarType.Small

    override val viewID: Int
        get() = R.layout.activity_toolbar

    override val toolbarTitle: String
        get() = getString(R.string.title_record_detail)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            fragment = RecordDetailsFragment.newIntent(intent.getLongExtra(RECORD_ID_EXTRA, 0))
            addFragment(R.id.fragmentContainer, fragment)
        }
    }
}