package io.forus.me.android.presentation.view.screens.records.create_record

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.screens.records.categories.RecordCategoriesFragment
import io.forus.me.android.presentation.view.screens.records.create_record.create_record_fragment.CreateRecordFragment
import io.forus.me.android.presentation.view.screens.records.create_record.create_record_fragment.CreateRecordFragment.Companion.RECORD_GROUP_ID_EXTRA
import io.forus.me.android.presentation.view.screens.records.create_record.create_record_fragment.CreateRecordFragment.Companion.RECORD_GROUP_NAME_EXTRA
import kotlinx.android.synthetic.main.activity_create_category_flow.*


class CreateCategoryFlowActivity : AppCompatActivity(), RecordCategoriesFragment.OnItemSelected, CreateRecordFragment.OnInputRecordNameText {


    companion object {

        fun getCallingIntent(context: Context): Intent {
            val intent = Intent(context, CreateCategoryFlowActivity::class.java)

            return intent
        }
    }

    var step = 1
    var recordCategory: RecordCategory? = null

    var recordNameText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category_flow)

        val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.nav_default_enter_anim)
                .setExitAnim(R.anim.nav_default_exit_anim)
                .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
                .build()

        statusNextButton(false)
        statusCurrentStep(step)

        nextBt.setOnClickListener {
            if (step == 1) {
                val args = Bundle()
                if (recordCategory != null) {
                    args.putLong(RECORD_GROUP_ID_EXTRA, recordCategory!!.id)
                    args.putString(RECORD_GROUP_NAME_EXTRA, recordCategory!!.name)
                    nav_host_fragment.findNavController().navigate(R.id.createRecordFragment, args, navOptions)
                    step = 2
                    statusNextButton(false)
                    statusCurrentStep(step)
                }
            } else if (step == 2) {
                //TODO
            }
        }

        closeBt.setOnClickListener {
            finish()

        }
    }

    override fun onItemSelected(item: RecordCategory) {
        recordCategory = item
        statusNextButton(true)
    }

    override fun onTextInput(text: String) {
        recordNameText = text
        Log.d("forus", "text = $text")
        if(recordNameText!!.isNotEmpty()&&recordNameText!!.length>2){
            statusNextButton(true)
        }else{
            statusNextButton(false)
        }
    }

    private fun statusNextButton(isActive: Boolean) {
        nextBt.isEnabled = isActive
        nextBt.background = if (isActive) ContextCompat.getDrawable(this@CreateCategoryFlowActivity, R.drawable.button_main_round_blue)
        else ContextCompat.getDrawable(this@CreateCategoryFlowActivity, R.drawable.button_main_raund_reverse)
        nextBt.setTextColor(if (isActive) ContextCompat.getColor(this@CreateCategoryFlowActivity, R.color.colorAccent)
        else ContextCompat.getColor(this@CreateCategoryFlowActivity, R.color.body_1_38))
    }

    private fun statusCurrentStep(step: Int) {

        step1View.background = if (step == 1) ContextCompat.getDrawable(this@CreateCategoryFlowActivity, R.drawable.button_main_raund)
        else ContextCompat.getDrawable(this@CreateCategoryFlowActivity, R.drawable.button_main_raund_reverse)
        step2View.background = if (step == 2) ContextCompat.getDrawable(this@CreateCategoryFlowActivity, R.drawable.button_main_raund)
        else ContextCompat.getDrawable(this@CreateCategoryFlowActivity, R.drawable.button_main_raund_reverse)

    }


}
