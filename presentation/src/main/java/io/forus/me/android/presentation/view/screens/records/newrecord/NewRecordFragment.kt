package io.forus.me.android.presentation.view.screens.records.newrecord

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.domain.exception.RetrofitException
import io.forus.me.android.domain.exception.RetrofitExceptionMapper
import io.forus.me.android.domain.models.records.RecordCategory
import io.forus.me.android.domain.models.records.RecordType
import io.forus.me.android.domain.models.validators.SimpleValidator
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentNewRecordBinding
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.records.newrecord.NewRecordView.Companion.NUM_PAGES
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.NewRecordViewPagerAdapter
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.RecordCategoriesAdapter
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.RecordTypesAdapter
import io.forus.me.android.presentation.view.screens.records.newrecord.adapters.RecordValidatorAdapter
import io.forus.me.android.presentation.view.screens.records.newrecord.viewholders.SelectedCategoryVH
import io.forus.me.android.presentation.view.screens.records.newrecord.viewholders.SelectedTextVH
import io.forus.me.android.presentation.view.screens.records.newrecord.viewholders.SelectedTypeVH
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
//import kotlinx.android.synthetic.main.fragment_new_record.*
//import kotlinx.android.synthetic.main.view_new_record_select_category.*
//import kotlinx.android.synthetic.main.view_new_record_select_type.*
//import kotlinx.android.synthetic.main.view_new_record_select_validator.*
//import kotlinx.android.synthetic.main.view_new_record_select_value.*
import java.lang.Exception


/**
 * Fragment New Record Screen.
 */
class NewRecordFragment : ToolbarLRFragment<NewRecordModel, NewRecordView, NewRecordPresenter>(), NewRecordView  {

    companion object {

        fun newIntent(): NewRecordFragment = NewRecordFragment().also {
            val bundle = Bundle()
            it.arguments = bundle
        }
    }

    private lateinit var mRootView : View

    private lateinit var recordCategoriesAdapter: RecordCategoriesAdapter
    private lateinit var recordTypesAdapter: RecordTypesAdapter
    private lateinit var recordValidatorAdapter: RecordValidatorAdapter

    private lateinit var selectedCategoryVH: SelectedCategoryVH
    private lateinit var selectedCategoryVH2: SelectedCategoryVH
    private lateinit var selectedCategoryVH3: SelectedCategoryVH
    private lateinit var selectedTypeVH2: SelectedTypeVH
    private lateinit var selectedTypeVH3: SelectedTypeVH
    private lateinit var selectedTextVH3: SelectedTextVH

    private var recyclerCategory: androidx.recyclerview.widget.RecyclerView? = null
    private var recyclerType: androidx.recyclerview.widget.RecyclerView? = null
    private var recyclerValidators: androidx.recyclerview.widget.RecyclerView? = null
    private var value: EditText? = null

    private var retrofitExceptionMapper: RetrofitExceptionMapper = Injection.instance.retrofitExceptionMapper

    override val toolbarTitle: String
        get() = getString(R.string.new_record_title_choose_category)

    override val allowBack: Boolean
        get() = true

    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = object : LoadRefreshPanel {
        override fun retryClicks(): Observable<Any> = Observable.never()

        override fun refreshes(): Observable<Any> = Observable.never()

        override fun render(vs: LRViewState<*>) {

        }
    }

    override fun onBackPressed(): Boolean {
        if (binding.mainViewPager.currentItem > 1){
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

    private val selectValidator = PublishSubject.create<SimpleValidator>()
    override fun selectValidator(): Observable<SimpleValidator> = selectValidator

    private val selectRecordType = PublishSubject.create<RecordType>()
    override fun selectType() = selectRecordType

    override fun setValue() = RxTextView.textChanges(value!!).map {
        it.toString()
    }!!

    private lateinit var binding : FragmentNewRecordBinding 

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentNewRecordBinding.inflate(inflater)

        mRootView = binding.root

        recordCategoriesAdapter = RecordCategoriesAdapter {
            selectRecordCategory.onNext(it)
        }
        recordValidatorAdapter = RecordValidatorAdapter  {
            selectValidator.onNext(it)
        }
        recordTypesAdapter = RecordTypesAdapter {
            selectRecordType.onNext(it)
        }

        selectedCategoryVH = SelectedCategoryVH(mRootView.findViewById(R.id.hat_item_category_1))
        selectedCategoryVH2 = SelectedCategoryVH(mRootView.findViewById(R.id.hat_item_category_2))
        selectedCategoryVH3 = SelectedCategoryVH(mRootView.findViewById(R.id.hat_item_category_3))
        selectedTypeVH2 = SelectedTypeVH(mRootView.findViewById(R.id.hat_item_type_2))
        selectedTypeVH3 = SelectedTypeVH(mRootView.findViewById(R.id.hat_item_type_3))
        selectedTextVH3 = SelectedTextVH(mRootView.findViewById(R.id.hat_item_value_3))


        recyclerCategory = mRootView.findViewById(R.id.recycler_category)
        recyclerType = mRootView.findViewById(R.id.recycler_type)
        recyclerValidators = mRootView.findViewById(R.id.recycler_validators)
        value = mRootView.findViewById(R.id.value)

        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newRecordViewPagerAdapter = NewRecordViewPagerAdapter(NUM_PAGES)
        binding.mainViewPager.adapter = newRecordViewPagerAdapter
        binding.mainViewPager.offscreenPageLimit = NUM_PAGES
        binding.mainViewPager.currentItem = 0
        binding.indicator.setViewPager(binding.mainViewPager)
        newRecordViewPagerAdapter.registerDataSetObserver(binding.indicator.dataSetObserver)
        binding.mainViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                changeToolbarTitle(position)
            }
        })


        recyclerCategory?.layoutManager =
            GridLayoutManager(context, 2)
        recyclerCategory?.adapter = recordCategoriesAdapter

        recyclerCategory?.layoutManager =
            LinearLayoutManager(context)
        recyclerCategory?.adapter = recordTypesAdapter


        recyclerValidators?.layoutManager =
            LinearLayoutManager(context)
        recyclerValidators?.adapter = recordValidatorAdapter
    }

    override fun createPresenter() = NewRecordPresenter(
            Injection.instance.recordsRepository,
            Injection.instance.validatorsRepository
    )


    override fun render(vs: LRViewState<NewRecordModel>) {
        super.render(vs)

        binding.progressBar.visibility = if (vs.loading || vs.model.sendingCreateRecord) View.VISIBLE else View.INVISIBLE

        recordCategoriesAdapter.items = vs.model.categories
        recordTypesAdapter.items = vs.model.types
        recordValidatorAdapter.items = vs.model.validators

        if (vs.closeScreen) {
            closeScreen()
        }

        binding.indicator.getChildAt(NUM_PAGES - 1)?.visibility = if(vs.model.validators.isEmpty()) View.INVISIBLE else View.VISIBLE
        binding.mainViewPager.currentItem = vs.model.currentStep
        renderButton(vs.model.isFinalStep, vs.model.buttonIsActive)

        when(vs.model.currentStep){
            1 -> if(vs.model.item.category != null) selectedCategoryVH.render(vs.model.item.category!!)
            2 -> {
                if(vs.model.item.category != null) selectedCategoryVH2.render(vs.model.item.category!!)
                if(vs.model.item.recordType != null) selectedTypeVH2.render(vs.model.item.recordType!!)
            }
            3 -> {
                if(vs.model.item.category != null) selectedCategoryVH3.render(vs.model.item.category!!)
                if(vs.model.item.recordType != null) selectedTypeVH3.render(vs.model.item.recordType!!)
                selectedTextVH3.render(vs.model.item.value)
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
        //navigator.navigateToDashboard(activity, false)
        activity?.finish()
    }

    private fun renderButton(isFinalStep: Boolean, buttonIsActive: Boolean){
        binding.btnNext.text = resources.getString(if (!isFinalStep) R.string.new_record_next_step else R.string.new_record_submit)
        binding.btnNext.active = buttonIsActive

        binding.btnNext.setOnClickListener {
            if (!isFinalStep) nextStep.onNext(true) else submit.onNext(true)
        }
    }

    private fun showError(text: String){
        showToastMessage(text)
    }

    private fun changeToolbarTitle(position: Int){
        val title =
                when (position) {
                    0 -> getString(R.string.new_record_title_choose_category)
                    1 -> getString(R.string.new_record_title_choose_type)
                    2 -> getString(R.string.new_record_title_choose_text)
                    3 -> getString(R.string.new_record_title_choose_validators)
                    else -> getString(R.string.new_record_title)
                }
        setToolbarTitle(title)
    }
}