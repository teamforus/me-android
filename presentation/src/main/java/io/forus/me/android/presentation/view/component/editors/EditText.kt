package io.forus.me.android.presentation.view.component.editors

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.RelativeLayout
import io.forus.me.android.presentation.R
import java.util.regex.Pattern

open class EditText : FrameLayout{

    protected open val layout: Int
        get() = R.layout.view_edit_text

    var hint: String? = ""
    var required: Boolean = false
    var validationError: String? = null
    var validationRegex: String? = ""
        set(value) {
            field = value ?: ""
            validationPattern = Pattern.compile(if (field.isNullOrEmpty()) ".*" else validationRegex)
        }

    private lateinit var  mContainer : RelativeLayout
    private lateinit var  mTextEdit : TextInputEditText
    private lateinit var  mTextInputLayout : TextInputLayout
    private var  validationPattern : Pattern? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        initAttrs(context, attrs)

        val inflater = LayoutInflater.from(context)
        val mRootView = inflater.inflate(layout, this)
        mContainer = mRootView.findViewById(R.id.container)
        mTextEdit = mContainer.findViewById(R.id.text_edit)
        mTextInputLayout = mContainer.findViewById(R.id.text_input_layout)
        mTextInputLayout.hint = if(required) TextUtils.concat(hint, Html.fromHtml(getContext().getString(R.string.me_validation_required_asterisk))) else hint
        mTextInputLayout.isHintAnimationEnabled = true
        mTextInputLayout.isErrorEnabled = true
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
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomEditFieldAttrs, 0, 0)
        hint = ta.getString(R.styleable.CustomEditFieldAttrs_hint)
        required = ta.getBoolean(R.styleable.CustomEditFieldAttrs_required, false)
        validationRegex = ta.getString(R.styleable.CustomEditFieldAttrs_validationRegex)
        validationError = ta.getString(R.styleable.CustomEditFieldAttrs_validationError)
        ta.recycle()
    }

    private fun validate(text: String) : Boolean {
        val isValid =  isValid()
        println("EEEEEET IS VALID"+isValid)
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

    fun getTextOrNullIfBlank(): String? {
        val text = mTextEdit.text.toString()
        return if(text.isBlank()) null else text
    }

    fun setError(error: String){
        mTextInputLayout.error = error
    }

    fun setTextChangedListener(listener : android.text.TextWatcher) {
        mTextEdit.addTextChangedListener(listener)
    }

    fun getEditText() = mTextEdit
}
