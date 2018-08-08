package io.forus.me.android.presentation.view.screens.records.newrecord

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRFragment
import com.ocrv.ekasui.mrm.ui.loadRefresh.LRViewState
import com.ocrv.ekasui.mrm.ui.loadRefresh.LoadRefreshPanel
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.exception.RetrofitExceptionMapper
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.screens.records.newrecord.NewRecordView.Companion.NUM_PAGES
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.NewRecordViewPagerAdapter
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.RecordCategoriesAdapter
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.RecordTypesAdapter
import io.forus.me.android.presentation.view.screens.records.newrecord.viewholders.SelectedCategoryVH
import io.forus.me.android.presentation.view.screens.records.newrecord.viewholders.SelectedTypeVH
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_new_record.*
import kotlinx.android.synthetic.main.view_new_record_select_category.*
import kotlinx.android.synthetic.main.view_new_record_select_type.*
import kotlinx.android.synthetic.main.view_new_record_select_value.*
import java.lang.Exception


/**
 * Fragment New Record Screen.
 */
class NewRecordFragment : LRFragment<NewRecordModel, NewRecordView, NewRecordPresenter>(), NewRecordView  {

    companion object {

        fun newIntent(): NewRecordFragment = NewRecordFragment().also {
            val bundle = Bundle()
            it.arguments = bundle
        }
    }

    private lateinit var mRootView : View

    private lateinit var recordCategoriesAdapter: RecordCategoriesAdapter
    private lateinit var recordTypesAdapter: RecordTypesAdapter

    private lateinit var selectedCategoryVH: SelectedCategoryVH
    private lateinit var selectedCategoryVH2: SelectedCategoryVH
    private lateinit var selectedTypeVH: SelectedTypeVH

    private var retrofitExceptionMapper: RetrofitExceptionMapper = Injection.instance.retrofitExceptionMapper


    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    override fun onBackPressed(): Boolean {
        if (main_view_pager.currentItem > 0){
            previousStep.onNext(true)
            return false
        }
        else return true
    }

    private val previousStep = PublishSubject.create<Boolean>()
    override fun previousStep(): Observable<Boolean> = previousStep

    private val nextStep = PublishSubject.create<Boolean>()
    override fun nextStep(): Observable<Boolean> = nextStep

    private val submit = PublishSubject.create<Boolean>()
    override fun submit(): Observable<Boolean> = submit

    private val selectRecordCategory = PublishSubject.create<RecordCategory>()
    override fun selectCategory(): Observable<RecordCategory> = selectRecordCategory

    private val selectRecordType = PublishSubject.create<RecordType>()
    override fun selectType() = selectRecordType

    override fun setValue() = RxTextView.textChanges(value).map {
        it.toString()
    }!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mRootView = inflater.inflate(R.layout.fragment_new_record, container, false)

        recordCategoriesAdapter = RecordCategoriesAdapter {
            selectRecordCategory.onNext(it)
        }
        recordTypesAdapter = RecordTypesAdapter {
            selectRecordType.onNext(it)
        }

        selectedCategoryVH = SelectedCategoryVH(mRootView.findViewById(R.id.hat_item_category))
        selectedCategoryVH2 = SelectedCategoryVH(mRootView.findViewById(R.id.hat_item_category_2))
        selectedTypeVH = SelectedTypeVH(mRootView.findViewById(R.id.hat_item_type))

        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newRecordViewPagerAdapter = NewRecordViewPagerAdapter()
        main_view_pager.adapter = newRecordViewPagerAdapter
        main_view_pager.offscreenPageLimit = NUM_PAGES
        main_view_pager.currentItem = 0
        indicator.setViewPager(main_view_pager)
        newRecordViewPagerAdapter.registerDataSetObserver(indicator.dataSetObserver)
        main_view_pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                (activity as NewRecordActivity).changeToolbarTitle(position)
            }
        })

        recycler_category.layoutManager = GridLayoutManager(context, 2)
        recycler_category.adapter = recordCategoriesAdapter

        recycler_type.layoutManager = LinearLayoutManager(context)
        recycler_type.adapter = recordTypesAdapter

        btn_next.setOnClickListener {
            if (main_view_pager.currentItem < NUM_PAGES - 1) nextStep.onNext(true) else submit.onNext(true)
        }
    }

    override fun createPresenter() = NewRecordPresenter(
            Injection.instance.recordsRepository
    )


    override fun render(vs: LRViewState<NewRecordModel>) {
        super.render(vs)

        progressBar.visibility = if (vs.loading) View.VISIBLE else View.INVISIBLE

        recordCategoriesAdapter.items = vs.model.categories
        recordTypesAdapter.items = vs.model.types

        if (vs.closeScreen) {
            closeScreen()
        }

        main_view_pager.currentItem = vs.model.currentStep
        renderButton(vs.model.currentStep, vs.model.buttonIsActive)
        when(vs.model.currentStep){
            1 -> if(vs.model.item.category != null) selectedCategoryVH2.render(vs.model.item.category!!)
            2 -> {
                if(vs.model.item.category != null) selectedCategoryVH.render(vs.model.item.category!!)
                if(vs.model.item.recordType != null) selectedTypeVH.render(vs.model.item.recordType!!)
            }
        }

        if(vs.model.sendingCreateRecordError != null){
            val error: Throwable = vs.model.sendingCreateRecordError
            if(error is RetrofitException && error.kind == RetrofitException.Kind.HTTP){
                try {
                    val newRecordError = retrofitExceptionMapper.mapToNewRecordError(error)
                    showError(newRecordError.message)
                }
                catch (e: Exception){}
            }
        }

    }

    private fun closeScreen() {
        navigator.navigateToDashboard(activity)
        activity?.finish()
    }

    private fun renderButton(currentStep: Int, buttonIsActive: Boolean){
        btn_next.text = resources.getString(if (currentStep < NUM_PAGES - 1) R.string.next_step else R.string.submit)
        btn_next.active = buttonIsActive
    }

    private fun showError(text: String){
        showToastMessage(text)
    }
}