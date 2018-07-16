package io.forus.me.android.presentation.view.screens.records.newrecord

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRFragment
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.exception.RetrofitExceptionMapper
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.domain.models.records.errors.NewRecordError
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.NewRecordViewPagerAdapter
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.RecordCategoriesAdapter
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.RecordTypesAdapter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_new_record.*
import kotlinx.android.synthetic.main.view_new_record_select_category.*
import kotlinx.android.synthetic.main.view_new_record_select_type.*
import kotlinx.android.synthetic.main.view_new_record_select_value.*


/**
 * Fragment New Record Screen.
 */
class NewRecordFragment : LRFragment<NewRecordModel, NewRecordView, NewRecordPresenter>(), NewRecordView  {

    companion object {
        private val ID_EXTRA = "ID_EXTRA"

        fun newIntent(id: String): NewRecordFragment = NewRecordFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(ID_EXTRA, id)
            it.arguments = bundle
        }
    }

    private lateinit var mRootView : View



    private lateinit var recordCategoriesAdapter: RecordCategoriesAdapter
    private lateinit var recordTypesAdapter: RecordTypesAdapter

    private var retrofitExceptionMapper: RetrofitExceptionMapper = Injection.instance.retrofitExceptionMapper



    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }


    override fun createRecord() = RxView.clicks(register).map {
        true
    }!!

    private val selectRecordCategory = PublishSubject.create<RecordCategory>()
    override fun selectCategory(): Observable<RecordCategory> = selectRecordCategory

    private val selectRecordType = PublishSubject.create<RecordType>()
    override fun selectType() = selectRecordType


    override fun setValue() = RxTextView.textChanges(value.getEditText()).map {
        it.toString()
    }!!



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mRootView = inflater.inflate(R.layout.fragment_new_record, container, false)

        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main_view_pager.adapter = NewRecordViewPagerAdapter()
        main_view_pager.offscreenPageLimit = 3


        recordCategoriesAdapter = RecordCategoriesAdapter {
            selectRecordCategory.onNext(it)
        }
        recordTypesAdapter = RecordTypesAdapter {
            selectRecordType.onNext(it)
        }


        recycler_category.layoutManager = LinearLayoutManager(context)
        recycler_category.adapter = recordCategoriesAdapter

        recycler_type.layoutManager = LinearLayoutManager(context)
        recycler_type.adapter = recordTypesAdapter


    }

    override fun createPresenter() = NewRecordPresenter(
            Injection.instance.recordsRepository
    )


    override fun render(vs: LRViewState<NewRecordModel>) {
        super.render(vs)

        recordCategoriesAdapter.items = vs.model.categories
        recordTypesAdapter.items = vs.model.types

        if (vs.closeScreen) {
            closeScreen()
        }


        register.visibility = if (vs.model.validationResult.valid) View.VISIBLE else View.GONE

        //showError(vs.model.validationResult.message)

//        if(vs.model.sendingCreateRecordError != null){
//            val error: Throwable = vs.model.sendingCreateRecordError
//            val newRecordError: NewRecordError = retrofitExceptionMapper.mapToNewRecordError(error as RetrofitException)
//            showError(newRecordError.toString())
//        }

    }

    private fun closeScreen() {
        navigator.navigateToDashboard(activity)
        activity?.finish()
    }

    private fun showError(text: String?){
        error.text = text
    }
}