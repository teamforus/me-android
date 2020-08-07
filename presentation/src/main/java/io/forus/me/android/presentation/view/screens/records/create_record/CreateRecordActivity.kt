package io.forus.me.android.presentation.view.screens.records.create_record

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.exception.RetrofitExceptionMapper
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.NoInternetDialog
import io.forus.me.android.presentation.view.screens.records.create_record.create_record_fragment.CreateRecordFragment
import io.forus.me.android.presentation.view.screens.records.create_record.create_record_fragment.CreateRecordFragment.Companion.RECORD_TYPE_NAME_EXTRA
import io.forus.me.android.presentation.view.screens.records.create_record.create_record_fragment.CreateRecordFragment.Companion.RECORD_INPUT_FIELD_TYPE_EXTRA
import io.forus.me.android.presentation.view.screens.records.create_record.create_record_fragment.CreateRecordModel
import io.forus.me.android.presentation.view.screens.records.create_record.dialog.CreateRecordErrorDialog
import io.forus.me.android.presentation.view.screens.records.create_record.dialog.CreateRecordSuccessDialog
import io.forus.me.android.presentation.view.screens.records.create_record.dialog.WaitDialog
import io.forus.me.android.presentation.view.screens.records.types.RecordTypesFragment
import kotlinx.android.synthetic.main.activity_create_category_flow.*
import java.lang.Exception


class CreateRecordActivity : AppCompatActivity(), RecordTypesFragment.OnItemSelected, CreateRecordFragment.OnInputRecordNameText {


    companion object {

        fun getCallingIntent(context: Context): Intent {
            val intent = Intent(context, CreateRecordActivity::class.java)

            return intent
        }
    }

    var step = 1
    var recordType: io.forus.me.android.domain.models.records.RecordType? = null

    var recordNameText: String? = null

    private var retrofitExceptionMapper: RetrofitExceptionMapper = Injection.instance.retrofitExceptionMapper

    var waitDialog: WaitDialog? = null

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
                if (recordType != null) {
                   // args.putString(RECORD_TYPE_KEY_EXTRA, recordType!!.key)
                    args.putString(RECORD_TYPE_NAME_EXTRA, recordType!!.name)
                    args.putString(RECORD_INPUT_FIELD_TYPE_EXTRA, recordType!!.type)
                    nav_host_fragment.findNavController().navigate(R.id.createRecordFragment, args, navOptions)
                    step = 2

                    statusNextButton(false)
                    statusCurrentStep(step)
                }
            } else if (step == 2) {
                if (recordNameText != null) {
                    waitDialog = WaitDialog(this@CreateRecordActivity)
                    waitDialog!!.show()
                    val model = CreateRecordModel(Injection.instance.recordsRepository)
                    model.createRecord(NewRecordRequest(recordType, null, mutableListOf(), recordNameText!!), { createRecordResponse ->
                        if(waitDialog!=null)waitDialog!!.dismiss()
                        showSuccessDialog(recordType!!.name, recordNameText!!)
                    }, { error ->
                        if(waitDialog!=null)waitDialog!!.dismiss()
                        parseError(error)
                    })
                }
            }
        }




        closeBt.setOnClickListener {
            finish()

        }
    }

    private fun showSuccessDialog(recordType: String, recordName: String) {
        CreateRecordSuccessDialog.display(supportFragmentManager, recordType,
                recordName) { finish() }
    }

    private fun parseError(error: Throwable) {

        if (error is io.forus.me.android.data.exception.RetrofitException && error.kind == RetrofitException.Kind.NETWORK) {
            NoInternetDialog(this@CreateRecordActivity) { }.show();
        } else {

            if (error is RetrofitException && error.kind == RetrofitException.Kind.HTTP) {


                try {
                    val detailsError = retrofitExceptionMapper.mapToDetailsApiError(error)


                    CreateRecordErrorDialog(this@CreateRecordActivity,detailsError.message,detailsError.errorsString
                    ) {}.show()



                } catch (e: Exception) {
                }

            } else {
            }

        }
    }


    override fun onItemSelected(item: io.forus.me.android.domain.models.records.RecordType) {
        recordType = item
        statusNextButton(true)
    }

    override fun onRecordTypesFragmentResume() {
        step = 1
        statusNextButton(false)
        statusCurrentStep(step)
    }


    override fun onTextInput(text: String) {
        recordNameText = text
        if (recordNameText!!.isNotEmpty() && recordNameText!!.length > 0) {
            statusNextButton(true)
        } else {
            statusNextButton(false)
        }
    }

    override fun onRecordTypesLoaded(list: List<RecordType>) {
    }

    private fun statusNextButton(isActive: Boolean) {
        nextBt.isEnabled = isActive
        nextBt.background = if (isActive) ContextCompat.getDrawable(this@CreateRecordActivity, R.drawable.button_main_round_blue)
        else ContextCompat.getDrawable(this@CreateRecordActivity, R.drawable.button_main_raund_reverse)
        nextBt.setTextColor(if (isActive) ContextCompat.getColor(this@CreateRecordActivity, R.color.colorAccent)
        else ContextCompat.getColor(this@CreateRecordActivity, R.color.body_1_38))
    }

    private fun statusCurrentStep(step: Int) {

        step1View.background = if (step == 1) ContextCompat.getDrawable(this@CreateRecordActivity, R.drawable.button_main_raund)
        else ContextCompat.getDrawable(this@CreateRecordActivity, R.drawable.button_main_raund_reverse)
        step2View.background = if (step == 2) ContextCompat.getDrawable(this@CreateRecordActivity, R.drawable.button_main_raund)
        else ContextCompat.getDrawable(this@CreateRecordActivity, R.drawable.button_main_raund_reverse)

        nextBt.text = if (step == 1) getString(R.string.next_step) else getString(R.string.submit)

    }


}
