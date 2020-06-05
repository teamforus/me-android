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
import android.text.TextWatcher





/**
 * A simple [Fragment] subclass.
 */
class CreateRecordFragment : Fragment() {

    companion object {
         val RECORD_GROUP_ID_EXTRA = "RECORD_GROUP_ID_EXTRA"
         val RECORD_GROUP_NAME_EXTRA = "RECORD_GROUP_NAME_EXTRA"

        fun newIntent(recordGroupId: Long, recordGroupName: String): RecordDetailsFragment = RecordDetailsFragment().also {
            val bundle = Bundle()
            bundle.putSerializable(RECORD_GROUP_ID_EXTRA, recordGroupId)
            bundle.putSerializable(RECORD_GROUP_NAME_EXTRA, recordGroupName)
            it.arguments = bundle
        }
    }

    private var recorCategorydId: Long = 0
    private var recorCategoryName: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val bundle = this.arguments
        if (bundle != null) {
            recorCategorydId = bundle.getLong(RECORD_GROUP_ID_EXTRA)
            recorCategoryName =bundle.getString(RECORD_GROUP_NAME_EXTRA)
        }


        Log.d("forus","name = $recorCategoryName")



        return inflater.inflate(R.layout.fragment_create_record, container, false)
    }

    var inputTextListener: OnInputRecordNameText? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inputTextListener = context as OnInputRecordNameText
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recordGroupNameTV.text = recorCategoryName


        recordNameEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

               if(inputTextListener!=null) {
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
