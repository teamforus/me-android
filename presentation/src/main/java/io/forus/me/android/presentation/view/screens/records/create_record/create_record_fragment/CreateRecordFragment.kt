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


/**
 * A simple [Fragment] subclass.
 */
class CreateRecordFragment : Fragment() {

    companion object {

        val RECORD_TYPE_NAME_EXTRA = "RECORD_TYPE_NAME_EXTRA"
        val RECORD_TYPE_VALUE_EXTRA = "RECORD_TYPE_VALUE_EXTRA"
        val RECORD_INPUT_FIELD_TYPE_EXTRA = "RECORD_INPUT_FIELD_TYPE_EXTRA"

        fun newIntent( recordTypeName: String, recordValue: String, recordTypeType: String): RecordDetailsFragment = RecordDetailsFragment().also {
            val bundle = Bundle()

            bundle.putSerializable(RECORD_TYPE_NAME_EXTRA, recordTypeName)
            bundle.putSerializable(RECORD_TYPE_VALUE_EXTRA, recordValue)
            bundle.putSerializable(RECORD_INPUT_FIELD_TYPE_EXTRA, recordTypeType)
            it.arguments = bundle
        }
    }


    private var recordTypeName: String = ""
    private var recordValue: String = ""
    private var recordInputFieldType: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val bundle = this.arguments
        if (bundle != null) {

            recordTypeName = bundle.getString(RECORD_TYPE_NAME_EXTRA) ?: ""
            recordValue = bundle.getString(RECORD_TYPE_VALUE_EXTRA) ?: ""
            recordInputFieldType = bundle.getString(RECORD_INPUT_FIELD_TYPE_EXTRA) ?: ""

        }


        Log.d("forus", "name = $recordTypeName")





        return inflater.inflate(R.layout.fragment_create_record, container, false)
    }

    var inputTextListener: OnInputRecordNameText? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inputTextListener = context as OnInputRecordNameText
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recordGroupNameTV.text = recordTypeName
        recordNameEditText.setText(recordValue)
        if(inputTextListener!=null){
            if(recordValue.isNotEmpty()){
                inputTextListener!!.onTextInput(recordValue)
            }
        }

        when (recordInputFieldType) {
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
