package io.forus.me.android.presentation.view.screens.records.item

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.exception.RetrofitExceptionMapper
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.records.create_record.EditRecordActivity
import io.forus.me.android.presentation.view.screens.records.item.dialogs.DeleteRecordErrorDialog
import io.forus.me.android.presentation.view.screens.records.item.dialogs.RecordModifyDialog
import io.forus.me.android.presentation.view.screens.records.item.validations.ValidationAdapter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_record_detail.*
import kotlinx.android.synthetic.main.toolbar_details_view.*
import java.lang.Exception

class RecordDetailsFragment : ToolbarLRFragment<RecordDetailsModel, RecordDetailsView, RecordDetailsPresenter>(), RecordDetailsView {


    companion object {
        private val RECORD_ID_EXTRA = "RECORD_ID_EXTRA"

        fun newIntent(recordId: Long): RecordDetailsFragment = RecordDetailsFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(RECORD_ID_EXTRA, recordId)
            it.arguments = bundle
        }
    }

    private var recordId: Long = 0
    private lateinit var adapter: ValidationAdapter//ValidatorsAdapter

    private val requestValidation = PublishSubject.create<Long>()
    override fun requestValidation(): Observable<Long> = requestValidation

    private var retrofitExceptionMapper: RetrofitExceptionMapper = Injection.instance.retrofitExceptionMapper


    override fun editRecord(): Observable<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val deleteRecord = PublishSubject.create<Long>()
    override fun deleteRecord(): Observable<Long> = deleteRecord

    override val allowBack: Boolean
        get() = true

    override val toolbarTitle: String
        get() = getString(R.string.record_details_title)

    override val showAccount: Boolean
        get() = false

    override val showInfo: Boolean
        get() = false

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_record_detail, container, false).also {

        val bundle = this.arguments
        if (bundle != null) {
            recordId = bundle.getLong(RECORD_ID_EXTRA)
        }

        adapter = ValidationAdapter { }
        /*adapter = ValidationAdapter { item ->
            if (item.status == ValidatorViewModel.Status.none && item.id != null) requestValidation.onNext(item.id!!)
        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_show_qr.setOnClickListener {
            (activity as? RecordDetailsActivity)?.showPopupQRFragment(recordId)
        }




        delete_button.setOnClickListener {

            RecordModifyDialog(activity as Activity, RecordModifyDialog.Action.DELETE) {
                deleteRecord.onNext(recordId)
            }.show();

            //activity!!.finish()
        }


        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    override fun createPresenter() = RecordDetailsPresenter(context!!,
            recordId,
            Injection.instance.recordsRepository,
            Injection.instance.validatorsRepository
    )

    override fun render(vs: LRViewState<RecordDetailsModel>) {
        super.render(vs)

        val record = vs.model.item
        type.text = record?.recordType?.name
        value.text = record?.value


        adapter.items = vs.model.validations

        if (vs.model.requestValidationError != null) {
            showToastMessage(resources.getString(R.string.record_details_validation_request_error))
        }

        if (vs.model.recordDeleteSuccess != null) {
            if (vs.model.recordDeleteSuccess) {
                Log.d("forus", "Record was deleted!!")
                activity!!.finish()
            }
        }

        edit_button.visibility = View.INVISIBLE
        edit_button.setOnClickListener {



            if(vs.model.item != null) {
                RecordModifyDialog(activity as Activity, RecordModifyDialog.Action.EDIT) {
                    startActivity(EditRecordActivity.getCallingIntent(context!!,vs.model.item.id, vs.model.item.recordType.name,vs.model.item.value))
                    activity!!.finish()
                }.show();
            }

        }


        if (vs.model.recordDeleteError != null) {

            val error = vs.model.recordDeleteError
            var messageString = error.localizedMessage
            if (error is RetrofitException) {

                try {
                    val delError = retrofitExceptionMapper.mapToBaseApiError(error)
                    if (error.responseCode == 403) {
                        messageString = if (delError.message == null) "" else delError.message
                    }

                } catch (e: Exception) {
                }
            }
            Log.d("forus", "Record  delete error =$messageString")
            DeleteRecordErrorDialog(messageString,context!!).show()

        }
    }
}