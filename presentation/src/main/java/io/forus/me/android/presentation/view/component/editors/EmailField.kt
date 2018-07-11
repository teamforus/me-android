package io.forus.me.android.presentation.view.component.editors

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.RelativeLayout
import io.forus.me.android.presentation.R
import java.util.regex.Pattern

class EmailField : FrameLayout{

    var hint: String? = ""
    var validationError: String? = null
    var validationRegex: String? = ""
        set(value) {
            field = value ?: ""
            validationPattern = Pattern.compile(if (field.isNullOrEmpty()) ".*" else validationRegex)
        }

    private lateinit var  mContainer : RelativeLayout
    private lateinit var  mTextEdit : AppCompatEditText
    private lateinit var  mTextInputLayout : TextInputLayout
    private var  validationPattern : Pattern? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        initAttrs(context, attrs)

        val inflater = LayoutInflater.from(context)
        val mRootView = inflater.inflate(R.layout.view_email_field, this)
        mContainer = mRootView.findViewById(R.id.container)
        mTextEdit = mContainer.findViewById(R.id.text_edit)
        mTextInputLayout = mContainer.findViewById(R.id.text_input_layout)
        mTextEdit.hint = hint

        mTextEdit.addTextChangedListener(object: android.text.TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validate(text = p0.toString())
            }
        })
    }

    private fun initAttrs(context: Context, attrs: AttributeSet){
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomEmailFieldAttrs, 0, 0)
        hint = ta.getString(R.styleable.CustomEmailFieldAttrs_email_hint)
        validationRegex = ta.getString(R.styleable.CustomEmailFieldAttrs_email_validationRegex)
        validationError = ta.getString(R.styleable.CustomEmailFieldAttrs_email_validationError)
        ta.recycle()
    }

    private fun validate(text: String) : Boolean {
        val isValid =  isValid()
        mTextInputLayout.error = if(!isValid) validationError else ""

        return isValid
    }

    private fun isValid() : Boolean{
        return validationPattern == null || validationPattern!!.matcher(mTextEdit.text).matches()
    }

    fun validate() : Boolean {
        return validate(this.mTextEdit.text.toString())
    }

    fun getText(): String {
        return mTextEdit.text.toString()
    }

    fun setError(error: String){
        mTextInputLayout.error = error
    }

    fun setTextChangedListener(listener : android.text.TextWatcher) {
        mTextEdit.addTextChangedListener(listener)
    }
}
