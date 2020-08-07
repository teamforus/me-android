package io.forus.me.android.presentation.view.screens.records.create_record

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
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
import io.forus.me.android.presentation.view.screens.records.create_record.create_record_fragment.CreateRecordFragment.Companion.RECORD_INPUT_FIELD_TYPE_EXTRA
import io.forus.me.android.presentation.view.screens.records.create_record.create_record_fragment.CreateRecordFragment.Companion.RECORD_TYPE_NAME_EXTRA
import io.forus.me.android.presentation.view.screens.records.create_record.create_record_fragment.CreateRecordFragment.Companion.RECORD_TYPE_VALUE_EXTRA
import io.forus.me.android.presentation.view.screens.records.create_record.create_record_fragment.CreateRecordModel
import io.forus.me.android.presentation.view.screens.records.create_record.dialog.CreateRecordErrorDialog
import io.forus.me.android.presentation.view.screens.records.create_record.dialog.CreateRecordSuccessDialog
import io.forus.me.android.presentation.view.screens.records.create_record.dialog.WaitDialog
import io.forus.me.android.presentation.view.screens.records.types.RecordTypesFragment
import kotlinx.android.synthetic.main.activity_create_category_flow.*


class EditRecordActivity : AppCompatActivity(), RecordTypesFragment.OnItemSelected, CreateRecordFragment.OnInputRecordNameText {


    companion object {


        val RECORD_ID = "RECORD_ID"
        val RECORD_TYPE = "RECORD_TYPE"
        val RECORD_VALUE = "RECORD_VALUE"

        fun getCallingIntent(context: Context, recordId: Long,recordType: String, recordValue: String): Intent {
            val intent = Intent(context, EditRecordActivity::class.java)

            intent.putExtra(RECORD_ID, recordId)
            intent.putExtra(RECORD_TYPE, recordType)
            intent.putExtra(RECORD_VALUE, recordValue)
            return intent
        }
    }

    var waitDialog: WaitDialog? = null

    var recordType: io.forus.me.android.domain.models.records.RecordType? = null

    var recordId: Long? = null
    var recordName = ""
    var recordValue = ""

    var recordNameText: String? = null

    private var retrofitExceptionMapper: RetrofitExceptionMapper = Injection.instance.retrofitExceptionMapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category_flow)

        val navOptions = NavOptions.Builder()
                .setEnterAnim(R.anim.nav_default_enter_anim)
                .setExitAnim(R.anim.nav_default_exit_anim)
                .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
                .build()

        if (savedInstanceState == null) {
            recordId = intent.getLongExtra(RECORD_ID,-1)
            recordName = intent.getStringExtra(RECORD_TYPE)
            recordValue = intent.getStringExtra(RECORD_VALUE)
        }


        val navController = findNavController(R.id.nav_host_fragment)
        val bundle = Bundle()
        bundle.putInt("showList", 0)

        navController.setGraph(navController.graph, bundle)




        statusNextButton(false)
        statusCurrentStep(2)

        nextBt.setOnClickListener {

            waitDialog = WaitDialog(this@EditRecordActivity)
            waitDialog!!.show()
            if (recordNameText != null) {
                val model = CreateRecordModel(Injection.instance.recordsRepository)
                model.createRecord(NewRecordRequest(recordType, null, mutableListOf(), recordNameText!!), { createRecordResponse ->
                    if(recordId != null) {
                        model.deleteRecord(recordId!!, {
                            showSuccessDialog(recordType!!.name, recordNameText!!)
                        }, { error ->
                            if(waitDialog!=null)waitDialog!!.dismiss()
                            parseError(error)
                        })
                    }
                }, { error ->
                    if(waitDialog!=null)waitDialog!!.dismiss()
                    parseError(error)
                })
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
            NoInternetDialog(this@EditRecordActivity) { }.show();
        } else {

            if (error is RetrofitException && error.kind == RetrofitException.Kind.HTTP) {

                try {
                    val detailsError = retrofitExceptionMapper.mapToDetailsApiError(error)


                    CreateRecordErrorDialog(this@EditRecordActivity, detailsError.message, detailsError.errorsString,
                            MaterialDialog.SingleButtonCallback { _, _ ->

                            }).show()


                } catch (e: Exception) {
                }

            }

        }
    }


    override fun onRecordTypesLoaded(list: List<RecordType>) {

        for (type in list) {
            if (type.name == recordName) {
                this.recordType = type
                break
            }
        }

        val args = Bundle()
        args.putString(RECORD_TYPE_NAME_EXTRA, recordName)
        args.putString(RECORD_TYPE_VALUE_EXTRA, recordValue)
        if (recordType != null) args.putString(RECORD_INPUT_FIELD_TYPE_EXTRA, recordType!!.type)
        nav_host_fragment.findNavController().navigate(R.id.createRecordFragment, args)
        statusCurrentStep(2)


    }


    override fun onItemSelected(item: io.forus.me.android.domain.models.records.RecordType) {
        recordType = item
        statusNextButton(true)
    }

    override fun onRecordTypesFragmentResume() {

        statusNextButton(false)
        statusCurrentStep(2)
    }


    override fun onTextInput(text: String) {
        Log.d("forus","onTextInput = $text")
        recordNameText = text
        if (recordNameText!!.isNotEmpty() && recordNameText!!.length > 0) {
            statusNextButton(true)
        } else {
            statusNextButton(false)
        }
    }

    private fun statusNextButton(isActive: Boolean) {
        nextBt.isEnabled = isActive
        nextBt.background = if (isActive) ContextCompat.getDrawable(this@EditRecordActivity, R.drawable.button_main_round_blue)
        else ContextCompat.getDrawable(this@EditRecordActivity, R.drawable.button_main_raund_reverse)
        nextBt.setTextColor(if (isActive) ContextCompat.getColor(this@EditRecordActivity, R.color.colorAccent)
        else ContextCompat.getColor(this@EditRecordActivity, R.color.body_1_38))
    }

    private fun statusCurrentStep(step: Int) {

        step1View.background = if (step == 1) ContextCompat.getDrawable(this@EditRecordActivity, R.drawable.button_main_raund)
        else ContextCompat.getDrawable(this@EditRecordActivity, R.drawable.button_main_raund_reverse)
        step2View.background = if (step == 2) ContextCompat.getDrawable(this@EditRecordActivity, R.drawable.button_main_raund)
        else ContextCompat.getDrawable(this@EditRecordActivity, R.drawable.button_main_raund_reverse)

        nextBt.text = if (step == 1) getString(R.string.next_step) else getString(R.string.submit)

    }



}
