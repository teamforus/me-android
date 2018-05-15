package io.forus.me.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

/**
 * Created by martijn.doornik on 16/03/2018.
 */
class AddressTextView(context: Context, attributeSet: AttributeSet) : TextView(context, attributeSet), View.OnClickListener {

    private var address: String = this.text as String
    var isSimple: Boolean = false

    val simpleText: String
    get() = address.substring(0, 6).plus("...")

    override fun onClick(v: View?) {
        if (v == this) {
            this.toggleSimple()
        }
    }

    private val ready: Boolean
            get() = this.address.length > 7

    fun setAddress(address:String) {
        this.address = address
        showSimple()
    }

    private fun showFull() {
        if (ready) {
            this.isSimple = false
            this.text = address
        }
    }

    private fun showSimple() {
        if (ready) {
            this.isSimple = true
            this.text = this.simpleText
        }
    }

    private fun toggleSimple() {
        if (this.isSimple) {
            showFull()
        } else {
            showSimple()
        }
    }

    init {
        if (ready) {
            showSimple()
        } else {
            this.text = "Waiting address data"
        }
        this.setOnClickListener(this)
    }
}