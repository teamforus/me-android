package io.forus.me.android.presentation.view.component.editors

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.NumberKeyListener
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.forus.me.android.presentation.R


//Input Text View that allows entering a dot or comma and enhances the
// user experience for inputting decimal numbers
//Created by Vasyl Horodetskyi 11.08.2023

class AmountTextInputEditText : TextInputEditText {

    var onInputListener: OnInputListener? = null

    /**
    Public interface to listen input changes
     */
    interface OnInputListener {

        /**
        It is called when the string entered by the user is changed.
         */
        fun onInput(s: String)
    }


    constructor(context: Context) : super(context) {
        setupValidationLogic()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setupValidationLogic()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setupValidationLogic()
    }

    private fun setupValidationLogic() {
        // Setting input type
        setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)

// Setting allowed characters (dot and comma)
        keyListener = object : NumberKeyListener() {
            override fun getInputType(): Int {
                return InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            }

            override fun getAcceptedChars(): CharArray {
                return charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', ',')
            }
        }

// Set the maximum numerical value
        filters = arrayOf<InputFilter>(InputFilter.LengthFilter(10)) // Наприклад, 10 цифр до коми

// Restriction to one dot or comma
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = text.toString()
                val dotCount = text.replace("[^.^,]*".toRegex(), "").length
                val commaCount = text.replace("[^.^,]*".toRegex(), "").length

                if (dotCount > 1 || commaCount > 1) {
                    setText(
                        text.removeSuffix(".")
                            .removeSuffix(",")
                    )
                    setSelection(this@AmountTextInputEditText.text?.length ?: 0)
                } else {
                    if (text.startsWith("."))
                        setText("0$text")

                    if (text.startsWith(","))
                        setText("0$text")

                    setSelection(this@AmountTextInputEditText.text?.length ?: 0)

                    val amountLayout = parent.parent as? TextInputLayout
                    if (text.endsWith(".") || text.endsWith(",")) {

                        amountLayout?.error =
                            context.getString(R.string.me_validation_error_decimal)
                    } else {
                        amountLayout?.error = null
                    }
                }

                onInputListener?.onInput(this@AmountTextInputEditText.text?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {}
        })

// Replacing comma with a dot or vice versa when entering a value by the user
        setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_PERIOD || keyCode == KeyEvent.KEYCODE_NUMPAD_DOT)) {
                    val text = text.toString()
                    setText(text.replace(",", ".")) // Replacing comma with a dot
                    setSelection(this@AmountTextInputEditText.text?.length ?: 0)
                    return true
                } else if (event?.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_COMMA) {
                    val text = text.toString()
                    setText(text.replace(".", ",")) // Replacing dot with a comma
                    setSelection(this@AmountTextInputEditText.text?.length ?: 0)
                    return true
                }
                return false
            }
        })
    }





}