package io.forus.me.android.presentation.view.screens.records.list

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.FragmentRecordsRecyclerBinding
import io.forus.me.android.presentation.helpers.Converter
import io.forus.me.android.presentation.helpers.SharedPref
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.records.create_record.CreateRecordActivity
import io.forus.me.android.presentation.view.screens.records.item.RecordDetailsActivity
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Fragment Records Delegates Screen.
 */
class RecordsFragment : ToolbarLRFragment<RecordsModel, RecordsView, RecordsPresenter>(), RecordsView {

    private var isRecords = true

    private val records = PublishSubject.create<Long>()
    override fun records(): Observable<Long> = records


    private val archives = PublishSubject.create<Long>()
    override fun archives(): Observable<Long> = archives

    val LAUNCH_SECOND_ACTIVITY = 111

    companion object {
        private val CATEGORY_ID_EXTRA = "CATEGORY_ID_EXTRA"
        private val CATEGORY_NAME_EXTRA = "CATEGORY_NAME_EXTRA"

        fun newIntent(recordCategoryId: Long, recordCategoryName: String): RecordsFragment = RecordsFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(CATEGORY_ID_EXTRA, recordCategoryId)
            bundle.putString(CATEGORY_NAME_EXTRA, recordCategoryName)
            it.arguments = bundle
        }

        fun newIntent(): RecordsFragment = RecordsFragment().also {

        }
    }

    var deleteButton: View? = null
    var editButton: View? = null
    var profileButton: View? = null

    override val allowBack: Boolean
        get() = true

    override val showAccount: Boolean
        get() = false

    override val showInfo: Boolean
        get() = false

    override val toolbarTitle: String
        get() = getString(R.string.dashboard_records)

    private var recordCategoryId: Long = 0
    private var recordCategoryName: String = ""

    private lateinit var adapter: RecordsAdapter

    override fun viewForSnackbar(): View = binding.root

    override fun loadRefreshPanel() = binding.lrPanel

    private lateinit var binding: FragmentRecordsRecyclerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        
        binding = FragmentRecordsRecyclerBinding.inflate(inflater)
        
        val bundle = this.arguments
        if (bundle != null) {
            recordCategoryId = bundle.getLong(CATEGORY_ID_EXTRA)
            recordCategoryName = bundle.getString(CATEGORY_NAME_EXTRA)?:""
        }

        deleteButton = binding.root.findViewById(R.id.delete_button)
        editButton = binding.root.findViewById(R.id.edit_button)
        profileButton = binding.root.findViewById(R.id.profile_button)
        
        return binding.root
    }

    var builder: SimpleTooltip.Builder? = null
    var addRecordTooltip: SimpleTooltip? = null
    val h = Handler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecordsAdapter()
        adapter.clickListener = { item ->
            if (isRecords) {
                val intentToLaunch = RecordDetailsActivity.getCallingIntent(requireContext(), item)
                startActivityForResult(intentToLaunch, LAUNCH_SECOND_ACTIVITY)
            }
        }
        binding.recycler.layoutManager =
            LinearLayoutManager(context)
        binding.recycler.adapter = adapter

        binding.addRecordBt.setOnClickListener {
            startActivity(CreateRecordActivity.getCallingIntent(requireContext()))
        }

        profileButton?.setOnClickListener {
            navigator.navigateToAccount(requireContext())
        }

        val text = getString(R.string.tooltip_create_record)

        if (SharedPref.read(SharedPref.OPTION_SHOW_TOOLTIP_ADD_RECORD,true)) {
            SharedPref.write(SharedPref.OPTION_SHOW_TOOLTIP_ADD_RECORD, false)
            builder = SimpleTooltip.Builder(requireContext())
                    .anchorView(binding.addRecordBt)
                    .text(text)
                    .gravity(Gravity.START)
                    .dismissOnOutsideTouch(false)
                    .dismissOnInsideTouch(true)
                    .modal(false)
                    .animated(true)
                    .animationDuration(1000)
                    .animationPadding(SimpleTooltipUtils.pxFromDp(Converter.convertDpToPixel(1f, requireContext()).toFloat()))
                    .transparentOverlay(true)
                    .arrowWidth(Converter.convertDpToPixel(10f, requireContext()).toFloat())
                    .arrowHeight(Converter.convertDpToPixel(7f, requireContext()).toFloat())
                    .contentView(R.layout.tooltip_new_record, R.id.tooltipText)//


            addRecordTooltip = builder!!.build()
            addRecordTooltip!!.show()


            h.postDelayed({

                addRecordTooltip.let {
                    it?.dismiss()
                }
            }, 30000)
        }
    }


    override fun createPresenter() = RecordsPresenter(
            recordCategoryId,
            Injection.instance.recordsRepository
    )


    override fun render(vs: LRViewState<RecordsModel>) {
        super.render(vs)



        if (isRecords) {
            setToolbarTitle(getString(R.string.dashboard_records))
            adapter.records = vs.model.items
        } else {
            adapter.records = vs.model.archives
        }
        adapter.notifyDataSetChanged()



        binding.tab1.setOnClickListener {
            binding.tab1title.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            binding.tab2title.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_subtitle))

            binding.tab1divider.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            binding.tab2divider.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.silver))
            isRecords = true
            records.onNext(0)
            binding.addRecordBt.show()
        }

        binding.tab2.setOnClickListener {
            binding.tab1title.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_subtitle))
            binding.tab2title.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))

            binding.tab1divider.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.silver))
            binding.tab2divider.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            isRecords = false
            archives.onNext(0)
            binding.addRecordBt.hide()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateModel()
    }
}

