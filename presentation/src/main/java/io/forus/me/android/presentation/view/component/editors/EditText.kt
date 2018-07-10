package io.forus.me.android.presentation.view.component.editors

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.util.AttributeSet
import io.forus.me.android.presentation.R
import java.util.regex.Pattern

class EditText : AppCompatEditText {

    private var  validationPattern : Pattern? = null
    var validationError: String? = null

    var validationRegex: String? = ""
        set(value) {
            field = value ?: ""
            validationPattern = Pattern.compile(if (field.isNullOrEmpty()) ".*" else validationRegex)
        }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        initAttrs(context, attrs)

        this.addTextChangedListener(object: android.text.TextWatcher {
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
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomEditTextAttrs, 0, 0)
        validationRegex = ta.getString(R.styleable.CustomEditTextAttrs_validationRegex)
        validationError = ta.getString(R.styleable.CustomEditTextAttrs_validationError)
    }

    private fun isValid() : Boolean{
        return validationPattern == null || validationPattern!!.matcher(text).matches()
    }

    public fun validate() : Boolean {
        return validate(this.text.toString())
    }

    private fun validate(text: String) : Boolean {
        val isValid =  isValid()
        error = if (!isValid) {
            validationError ?: null
        } else {
            null
        }

        return isValid
    }


}
