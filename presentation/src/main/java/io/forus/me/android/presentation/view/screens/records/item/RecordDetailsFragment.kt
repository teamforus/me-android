package io.forus.me.android.presentation.view.screens.records.item

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.records.item.validations.ValidationAdapter
import io.forus.me.android.presentation.view.screens.records.item.validations.ValidationViewModel
import io.forus.me.android.presentation.view.screens.records.item.validators.ValidatorViewModel
import io.forus.me.android.presentation.view.screens.records.item.validators.ValidatorsAdapter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_record_detail.*

class RecordDetailsFragment : ToolbarLRFragment<RecordDetailsModel, RecordDetailsView, RecordDetailsPresenter>(), RecordDetailsView{

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

    override val allowBack: Boolean
        get() = true

    override val toolbarTitle: String
        get() = getString(R.string.record_details_title)

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
        = inflater.inflate(R.layout.fragment_record_detail, container, false).also {

        val bundle = this.arguments
        if (bundle != null) {
            recordId = bundle.getLong(RECORD_ID_EXTRA)
        }

        adapter = ValidationAdapter {  }
        /*adapter = ValidationAdapter { item ->
            if (item.status == ValidatorViewModel.Status.none && item.id != null) requestValidation.onNext(item.id!!)
        }*/
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_show_qr.setOnClickListener {
            (activity as? RecordDetailsActivity)?.showPopupQRFragment(recordId)
        }



        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter
    }

    override fun createPresenter() = RecordDetailsPresenter(
            recordId,
            Injection.instance.recordsRepository,
            Injection.instance.validatorsRepository
    )

    override fun render(vs: LRViewState<RecordDetailsModel>) {
        super.render(vs)

        val record = vs.model.item
        type.text = record?.recordType?.name
        value.text = record?.value

       /* Log.d("forus","adapter_1")
        val validations: MutableList<ValidationViewModel> = mutableListOf()
        Log.d("forus","adapter_2")
        validations.add(ValidationViewModel(ValidationViewModel.Type.header.name))
        Log.d("forus","adapter_3")
        for(validation in record!!.validations){
            validations.add(ValidationViewModel(validation))
            Log.d("forus","adapter_*")
        }
        Log.d("forus","adapter_4")*/
        adapter.items = vs.model.validations

        if(vs.model.requestValidationError != null){
            showToastMessage(resources.getString(R.string.record_details_validation_request_error))
        }
    }
}