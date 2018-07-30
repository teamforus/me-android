package io.forus.me.android.presentation.view.screens.records.newrecord

import android.content.Context
import android.content.Intent
import android.os.Bundle
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.ToolbarActivity

/**
 * Record creation screen
 */
class NewRecordActivity : ToolbarActivity() {


    companion object {



        fun getCallingIntent(context: Context ): Intent {
            val intent = Intent(context, NewRecordActivity::class.java)
            return intent
        }
    }

    lateinit var fragment : NewRecordFragment

    override val toolbarType: ToolbarType
        get() = ToolbarType.Small

    override val viewID: Int
        get() = R.layout.activity_toolbar

    override val toolbarTitle: String
        get() = getString(R.string.title_choose_category)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {


            fragment = NewRecordFragment.newIntent()

            addFragment(R.id.fragmentContainer, fragment)
        }
    }

    fun changeToolbarTitle(position: Int){
        val title =
        when (position) {
            0 -> getString(R.string.title_choose_category)
            1 -> getString(R.string.title_choose_type)
            2 -> getString(R.string.title_choose_text)
            else -> getString(R.string.title_new_record)
        }
        this.setToolbarTitle(title)

    }

    override fun onBackPressed() {
        if(fragment.onBackPressed())
            super.onBackPressed()
    }
}