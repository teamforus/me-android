package io.forus.me.android.presentation.view.component.editors

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.text.Editable
import android.text.Html
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.RelativeLayout
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.component.ValidationRegex
import java.util.regex.Pattern


open class EditText : FrameLayout{

    protected open val layout: Int
        get() = R.layout.view_edit_text

    var inputType: Int = EditorInfo.TYPE_NULL
    var hint: String? = ""
    var required: Boolean = false
    var validationError: String? = null
    var validationRegex: String? = ""
        set(value) {
            field = value ?: ""
            validationPattern = Pattern.compile(if (field.isNullOrEmpty()) ".*" else validationRegex)
        }

    var isErrorLayoutEnabled: Boolean = true
        set(value) {
            field = value
            mTextInputLayout.isErrorEnabled = value
        }

    var showError: Boolean = true
        set(value) {
            field = value
            mTextInputLayout.error = if(value) fieldError else ""
        }

    var fieldError: String? = null
        set(value) {
            field = value
            if(showError) mTextInputLayout.error = value
        }

    var isEditable: Boolean = true
        set(value){
            field = value
            mTextEdit.inputType = if(value) inputType else InputType.TYPE_NULL
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
        if (inputType != EditorInfo.TYPE_NULL) {
            mTextEdit.setInputType(inputType)
        }
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
        val validationRegexValue = ta.getInt(R.styleable.CustomEditFieldAttrs_validationRegex, 0)
        validationRegex = ValidationRegex.values().get(validationRegexValue).pattern
        validationError = ta.getString(R.styleable.CustomEditFieldAttrs_validationError)
        inputType = ta.getInt(R.styleable.CustomEditFieldAttrs_android_inputType, EditorInfo.TYPE_NULL)
        ta.recycle()
    }

    private fun validate(text: String) : Boolean {
        val isValid =  isValid()
        fieldError = if(!isValid) validationError else ""

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
        fieldError = error
    }

    fun setTextChangedListener(listener : android.text.TextWatcher) {
        mTextEdit.addTextChangedListener(listener)
    }
}
