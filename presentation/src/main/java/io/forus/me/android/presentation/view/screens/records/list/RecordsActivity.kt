package io.forus.me.android.presentation.view.screens.records.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.ToolbarActivity

class RecordsActivity : ToolbarActivity() {


    companion object {

        val ID_EXTRA = "CATEGORY_EXTRA"

        fun getCallingIntent(context: Context, recordCategory: RecordCategory): Intent {
            val intent = Intent(context, RecordsActivity::class.java)
            intent.putExtra(ID_EXTRA, recordCategory)
            return intent
        }
    }

    lateinit var recordCategory: RecordCategory

    lateinit var fragment : RecordsFragment

    override val toolbarType: ToolbarType
        get() = ToolbarType.Small

    override val viewID: Int
        get() = R.layout.activity_toolbar

    override val toolbarTitle: String
        get() = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            recordCategory = intent.getSerializableExtra(ID_EXTRA) as RecordCategory
            setToolbarTitle(recordCategory.name)

            fragment = RecordsFragment.newIntent(recordCategory)
            addFragment(R.id.fragmentContainer, fragment)
        }
    }
}
