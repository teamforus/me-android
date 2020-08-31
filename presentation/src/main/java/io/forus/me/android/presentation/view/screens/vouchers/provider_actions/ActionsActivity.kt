package io.forus.me.android.presentation.view.screens.vouchers.provider_actions

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ActivityActionsBinding
import io.forus.me.android.presentation.view.screens.vouchers.provider_actions.adapter.ActionsAdapter
import io.forus.me.android.presentation.view.screens.vouchers.provider_actions.model.ActionsViewModel


class ActionsActivity : AppCompatActivity() {


    companion object {

        //val CATEGORY_ID_EXTRA = "CATEGORY_ID_EXTRA"
        //val CATEGORY_NAME_EXTRA = "CATEGORY_NAME_EXTRA"

        fun getCallingIntent(context: Context): Intent {
            val intent = Intent(context, ActionsActivity::class.java)
            /*if(recordCategory!=null) {
                intent.putExtra(CATEGORY_ID_EXTRA, recordCategory.id)
                intent.putExtra(CATEGORY_NAME_EXTRA, recordCategory.name)
            }*/
            return intent
        }
    }

    lateinit var mainViewModel: ActionsViewModel

    lateinit var binding: ActivityActionsBinding


    var transactionsAdapter: ActionsAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_actions)
        setSupportActionBar(findViewById(R.id.toolbar))

        mainViewModel = ViewModelProviders.of(this).get(ActionsViewModel::class.java)// ViewModelProvider(this).get(TransactionsStoryViewModel::class.java)
        // mainViewModel.docId = docId!!
        // mainViewModel.docType = docType

        binding = setContentView(R.layout.activity_actions)//iew<ActivityActionsBinding>(this@ActionsActivity, R.layout.activity_actions)
        binding.lifecycleOwner = this
        binding.model = mainViewModel

    }
}