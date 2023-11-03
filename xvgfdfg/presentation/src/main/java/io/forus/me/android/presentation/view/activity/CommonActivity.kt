package io.forus.me.android.presentation.view.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.MenuItem
import androidx.lifecycle.ViewModel


abstract class CommonActivity : BaseActivity() {

    protected open val viewID: Int
        get() = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(viewID)
        initUI()
    }

    private fun initUI() {


    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onMenuHomePressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onMenuHomePressed() {
        onBackPressed()
    }

    fun getCustomColor(color: Int): Int {
        return ContextCompat.getColor(this, color)
    }
}
