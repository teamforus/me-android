package io.forus.me.android.presentation.view.screens.records.newrecord

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRFragment
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.exception.RetrofitExceptionMapper
import io.forus.me.android.domain.models.records.NewRecordRequest
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.domain.models.records.errors.NewRecordError
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.component.buttons.Button
import io.forus.me.android.presentation.view.component.editors.EditText
import io.forus.me.android.presentation.view.component.text.TextView
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.NewRecordViewPagerAdapter
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.RecordCategoriesAdapter
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.RecordTypesAdapter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_new_record.*


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
    private lateinit var viewPager: ViewPager
    private lateinit var viewSelectCategory: View
    private lateinit var viewSelectType: View
    private lateinit var viewSelectValue: View
    private lateinit var rvCategories : RecyclerView
    private lateinit var rvTypes : RecyclerView
    private lateinit var tvValue: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvError: TextView

    private lateinit var recordCategoriesAdapter: RecordCategoriesAdapter
    private lateinit var recordTypesAdapter: RecordTypesAdapter

    private var selectedCategory: RecordCategory? = null
    private var selectedType: RecordType? = null
    private var sendingCreateRecord: Boolean = false

    private var retrofitExceptionMapper: RetrofitExceptionMapper = Injection.instance.retrofitExceptionMapper

    private val viewIsValid: Boolean
        get() {
            var valid = false
            if(sendingCreateRecord){
                showError("Request in progress")
            }
            else if(selectedCategory == null){
                showError("Please select category")
            }
            else if(selectedCategory == null){
                showError("Please select type")
            }
            else if(tvValue.getText().isEmpty() || !tvValue.validate()){
                showError("Value is not valid")
            }
            else valid = true
            return valid
        }


    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }


    private val createRecordAction = PublishSubject.create<NewRecordRequest>()

    override fun createRecord() = createRecordAction

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mRootView = inflater.inflate(R.layout.fragment_new_record, container, false)

        viewPager = mRootView.findViewById(R.id.pager)
        viewPager.adapter = NewRecordViewPagerAdapter()
        viewPager.offscreenPageLimit = 3

        viewSelectCategory = mRootView.findViewById(R.id.page_one)
        viewSelectType = mRootView.findViewById(R.id.page_two)
        viewSelectValue = mRootView.findViewById(R.id.page_tree)

        recordCategoriesAdapter = RecordCategoriesAdapter {
            selectedCategory = it
        }
        recordTypesAdapter = RecordTypesAdapter {
            selectedType = it
        }

        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvCategories = viewSelectCategory.findViewById(R.id.recycler)
        rvCategories.layoutManager = LinearLayoutManager(context)

        rvCategories.adapter = recordCategoriesAdapter

        rvTypes = viewSelectType.findViewById(R.id.recycler)
        rvTypes.layoutManager = LinearLayoutManager(context)
        rvTypes.adapter = recordTypesAdapter

        tvValue = viewSelectValue.findViewById(R.id.value)
        btnRegister = viewSelectValue.findViewById(R.id.register)
        tvError = viewSelectValue.findViewById(R.id.error)

        btnRegister.setOnClickListener {
            if (viewIsValid) {
                showError(null)

                createRecordAction.onNext(NewRecordRequest(
                    selectedType as RecordType, selectedCategory as RecordCategory, tvValue.getText(), 0
                ))
            }
        }
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

        if(vs.model.sendingCreateRecordError != null){
            val error: Throwable = vs.model.sendingCreateRecordError
            val newRecordError: NewRecordError = retrofitExceptionMapper.mapToNewRecordError(error as RetrofitException)
            showError(newRecordError.toString())
        }

        sendingCreateRecord = vs.model.sendingCreateRecord
    }

    private fun closeScreen() {
        navigator.navigateToDashboard(activity)
        activity?.finish()
    }

    private fun showError(error: String?){
        tvError.text = error
    }
}