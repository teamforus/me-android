package io.forus.me.android.presentation.view.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.dashboard_activity.*

import kotlinx.android.synthetic.main.toolbar_view.*
import io.forus.me.android.presentation.R.string.navigation_drawer_close
import io.forus.me.android.presentation.R.string.navigation_drawer_open
import io.forus.me.android.presentation.R.id.toolbar
import io.forus.me.android.presentation.helpers.Converter


abstract class ToolbarActivity : BaseActivity() {



    protected val toolbar: Toolbar
        get() = toolbar_view

    protected open val toolbarTitle: String
        get() = ""


    protected open val viewID: Int
        get() = 0


    protected open val toolbarType: ToolbarType
        get() = ToolbarType.Regular


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(viewID)
        initUI()
    }

    protected fun setToolbarTitle(title: String){
        toolbar_title.text = title

    }


    private fun initUI() {


        setToolbarTitle(toolbarTitle)
        if (toolbarType == ToolbarType.Small){
            toolbar_title.setPadding(toolbar_title.paddingLeft, Converter.convertDpToPixel(5f, this), toolbar_title.paddingRight, 0)
        }
        setSupportActionBar(toolbar)

        //        final Drawable upArrow = getResources().getDrawable(R.drawable.);
        //        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    protected fun initfragment(fragment: Fragment) {

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
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

    protected fun getCustomColor(color: Int): Int {
        return ContextCompat.getColor(this, color)
    }


     enum class ToolbarType {
        Regular, Small
    }



}
