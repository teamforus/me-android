package io.forus.me.android.presentation.view.screens.records.newrecord

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.activity.ToolbarActivity

/**
 * Record creation screen
 */
class NewRecordActivity : ToolbarActivity() {


    companion object {

        val ID_EXTRA = "ID_EXTRA"

        fun getCallingIntent(context: Context, id: String): Intent {
            val intent = Intent(context, NewRecordActivity::class.java)
            intent.putExtra(ID_EXTRA, id)
            return intent
        }
    }



    override val toolbarType: ToolbarType
        get() = ToolbarType.Small

    override val viewID: Int
        get() = R.layout.activity_toolbar

    override val toolbarTitle: String
        get() = getString(R.string.new_record)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            var categoryId = "1"
            if (intent.hasExtra(NewRecordActivity.ID_EXTRA)) {
                categoryId = intent.getStringExtra(NewRecordActivity.ID_EXTRA)
            }

            val fragment = NewRecordFragment.newIntent(categoryId)

            addFragment(R.id.fragmentContainer, fragment)
        }
    }


}