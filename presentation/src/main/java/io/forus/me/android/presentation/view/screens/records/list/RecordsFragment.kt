package io.forus.me.android.presentation.view.screens.records.list

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.internal.Injection
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.fragment.ToolbarLRFragment
import io.forus.me.android.presentation.view.screens.records.create_record.CreateRecordActivity
import io.forus.me.android.presentation.view.screens.records.item.RecordDetailsActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_records_recycler.*
import kotlinx.android.synthetic.main.toolbar_view.*


/**
 * Fragment Records Delegates Screen.
 */
class RecordsFragment : ToolbarLRFragment<RecordsModel, RecordsView, RecordsPresenter>(), RecordsView{

    private var isRecords =true

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

    override fun viewForSnackbar(): View = root

    override fun loadRefreshPanel() = lr_panel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_records_recycler, container, false)
        val bundle = this.arguments
        if (bundle != null) {
            recordCategoryId = bundle.getLong(CATEGORY_ID_EXTRA)
            recordCategoryName = bundle.getString(CATEGORY_NAME_EXTRA)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecordsAdapter()
        adapter.clickListener = { item ->
            if(isRecords) {
                val intentToLaunch = RecordDetailsActivity.getCallingIntent(context!!, item)
                startActivityForResult(intentToLaunch, LAUNCH_SECOND_ACTIVITY)
            }
        }
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        addRecordBt.setOnClickListener {
            startActivity(CreateRecordActivity.getCallingIntent(context!!))
        }

        profile_button.setOnClickListener {
            navigator.navigateToAccount(context!!)
        }

    }


    override fun createPresenter() = RecordsPresenter(
            recordCategoryId,
            Injection.instance.recordsRepository
    )


    override fun render(vs: LRViewState<RecordsModel>) {
        super.render(vs)


        if(isRecords) {
            setToolbarTitle(getString(R.string.dashboard_records))
            adapter.records = vs.model.items
        }else{
            adapter.records = vs.model.archives
        }
        adapter.notifyDataSetChanged()



        tab1.setOnClickListener {
            tab1title.setTextColor(ContextCompat.getColor(context!!,R.color.colorAccent))
            tab2title.setTextColor(ContextCompat.getColor(context!!,R.color.gray_subtitle))

            tab1divider.setBackgroundColor(ContextCompat.getColor(context!!,R.color.colorAccent))
            tab2divider.setBackgroundColor(ContextCompat.getColor(context!!,R.color.silver))
            isRecords = true
            records.onNext(0)

            addRecordBt.show()
        }

        tab2.setOnClickListener {
            tab1title.setTextColor(ContextCompat.getColor(context!!,R.color.gray_subtitle))
            tab2title.setTextColor(ContextCompat.getColor(context!!,R.color.colorAccent))

            tab1divider.setBackgroundColor(ContextCompat.getColor(context!!,R.color.silver))
            tab2divider.setBackgroundColor(ContextCompat.getColor(context!!,R.color.colorAccent))
            isRecords = false
            archives.onNext(0)

            addRecordBt.hide()
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateModel()

    }
}

