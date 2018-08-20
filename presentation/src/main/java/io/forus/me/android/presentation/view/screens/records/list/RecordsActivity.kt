package io.forus.me.android.presentation.view.screens.records.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.CommonActivity

class RecordsActivity : CommonActivity() {


    companion object {

        val CATEGORY_ID_EXTRA = "CATEGORY_ID_EXTRA"
        val CATEGORY_NAME_EXTRA = "CATEGORY_NAME_EXTRA"

        fun getCallingIntent(context: Context, recordCategory: RecordCategory): Intent {
            val intent = Intent(context, RecordsActivity::class.java)
            intent.putExtra(CATEGORY_ID_EXTRA, recordCategory.id)
            intent.putExtra(CATEGORY_NAME_EXTRA, recordCategory.name)
            return intent
        }
    }

    lateinit var fragment : RecordsFragment


    override val viewID: Int
        get() = R.layout.activity_toolbar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val recordCategoryId = intent.getLongExtra(CATEGORY_ID_EXTRA, 0)
            val recordCategoryName = intent.getStringExtra(CATEGORY_NAME_EXTRA)

            fragment = RecordsFragment.newIntent(recordCategoryId, recordCategoryName)
            addFragment(R.id.fragmentContainer, fragment)
        }
    }
}
