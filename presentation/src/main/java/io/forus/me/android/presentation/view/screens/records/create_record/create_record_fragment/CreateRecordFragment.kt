package io.forus.me.android.presentation.view.screens.records.create_record.create_record_fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.screens.records.item.RecordDetailsFragment
import kotlinx.android.synthetic.main.fragment_create_record.*
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo


/**
 * A simple [Fragment] subclass.
 */
class CreateRecordFragment : Fragment() {

    companion object {
        val RECORD_TYPE_KEY_EXTRA = "RECORD_TYPE_KEY_EXTRA"
        val RECORD_TYPE_NAME_EXTRA = "RECORD_TYPE_NAME_EXTRA"
        val RECORD_TYPE_TYPE_EXTRA = "RECORD_TYPE_TYPE_EXTRA"

        fun newIntent(recordTypeKey: String, recordTypepName: String, recordTypeType: String): RecordDetailsFragment = RecordDetailsFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(RECORD_TYPE_KEY_EXTRA, recordTypeKey)
            bundle.putSerializable(RECORD_TYPE_NAME_EXTRA, recordTypepName)
            bundle.putSerializable(RECORD_TYPE_TYPE_EXTRA, recordTypeType)
            it.arguments = bundle
        }
    }

    private var recordTypeKey: String = ""
    private var recordTypepName: String = ""
    private var recordTypeType: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val bundle = this.arguments
        if (bundle != null) {
            recordTypeKey = bundle.getString(RECORD_TYPE_KEY_EXTRA)
            recordTypepName = bundle.getString(RECORD_TYPE_NAME_EXTRA)
            recordTypeType = bundle.getString(RECORD_TYPE_TYPE_EXTRA)
        }


        Log.d("forus", "name = $recordTypepName")





        return inflater.inflate(R.layout.fragment_create_record, container, false)
    }

    var inputTextListener: OnInputRecordNameText? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inputTextListener = context as OnInputRecordNameText
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recordGroupNameTV.text = recordTypepName

        when (recordTypeType) {
            "string" -> {
                recordNameEditText.maxLines = 1
                recordNameEditText.inputType = InputType.TYPE_CLASS_TEXT
                //recordNameEditText.imeOptions = EditorInfo.IME_ACTION_DONE
            }
            "text" -> {
                recordNameEditText.maxLines = 1000
                recordNameEditText.inputType = InputType.TYPE_CLASS_TEXT
            }
            "number" -> {
                recordNameEditText.maxLines = 1
                recordNameEditText.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }

        recordNameEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                if (inputTextListener != null) {
                    inputTextListener!!.onTextInput(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    interface OnInputRecordNameText {
        fun onTextInput(text: String)
    }


}
