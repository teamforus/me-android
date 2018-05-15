package io.forus.me.views.record

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import io.forus.me.R
import io.forus.me.RecordDetailActivity
import io.forus.me.entities.Record
import io.forus.me.helpers.JsonHelper
import io.forus.me.views.base.TitledFragment

/**
 * Created by martijn.doornik on 15/03/2018.
 */
class RecordsFragment : TitledFragment() {

    private lateinit var recordCategoryList: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.records_fragment, container, false)
        this.recordCategoryList = view.findViewById(R.id.recordCategoryView)
        recordCategoryList.layoutManager = LinearLayoutManager(this.context)
        recordCategoryList.adapter = RecordCategoryAdapter(this)
        return view
    }

    fun onRecordSelect(record:Record) {
        val intent = Intent(this.activity, RecordDetailActivity::class.java)
        intent.putExtra(RecordDetailActivity.RequestCode.RECORD_OBJECT_KEY, record.toJson().toString())
        startActivity(intent)
    }

}