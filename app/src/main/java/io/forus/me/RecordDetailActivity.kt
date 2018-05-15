package io.forus.me

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import io.forus.me.entities.Record
import io.forus.me.helpers.JsonHelper

class RecordDetailActivity : AppCompatActivity() {

    private lateinit var record: Record

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = intent.getStringExtra(RequestCode.RECORD_OBJECT_KEY)
        if (data == null) {
            finish()
        }
        val record = JsonHelper.toRecord(data)
        if (record == null) {
            finish()
        }
        this.record = record!!
    }

    override fun onResume() {
        super.onResume()
        this.setContentView(R.layout.activity_record_detail)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = record.name
        toolbar.subtitle = resources.getString(R.string.syncing) // TODO after sync is implemented record.value

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener { this.finish() }
    }

    class RequestCode {
        companion object {
            const val RECORD_OBJECT_KEY = "record"
        }
    }
}