package io.forus.me.android.presentation.view.fragment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import kotlinx.android.synthetic.main.toolbar_view.*
import io.forus.me.android.presentation.view.base.lr.LRFragment
import io.forus.me.android.presentation.view.base.lr.LRView
import io.forus.me.android.presentation.view.base.lr.LRViewState
import io.forus.me.android.presentation.view.base.lr.LoadRefreshPanel
import io.forus.me.android.presentation.helpers.Converter
import io.reactivex.Observable

abstract class ToolbarLRFragment<M, V : LRView<M>, P : MviBasePresenter<V, LRViewState<M>>> : LRFragment<M, V, P>() {


    protected val toolbar: Toolbar
        get() = toolbar_view



    open val showAccount: Boolean
        get() = true


    open val showInfo: Boolean
        get() = false


    protected open val allowBack: Boolean
        get() = false


    protected open val toolbarTitle: String
        get() = ""

    protected open val toolbarType: ToolbarType
        get() = ToolbarType.Regular


    protected fun setToolbarTitle(title: String){
        toolbar_title.text = title

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val result =  super.onCreateView(inflater, container, savedInstanceState)
        return  result

    }

    private fun initUI() {


        setToolbarTitle(toolbarTitle)
        if (toolbarType == ToolbarType.Small){
            toolbar_title.setPadding(toolbar_title.paddingLeft, Converter.convertDpToPixel(5f, activity!!.applicationContext), toolbar_title.paddingRight, 0)
        }


        val castActivity = activity!!
        when (castActivity){
            is AppCompatActivity -> setActionBarActivity(castActivity)

        }



    }

    private fun setActionBarActivity( _activity: AppCompatActivity){
        _activity.setSupportActionBar(toolbar)
        FragmentHelper.setHomeIconToolbar(_activity, toolbar, profile_button, allowBack)

        if (!showAccount)
            profile_button.visibility = View.INVISIBLE
        else profile_button.visibility = View.VISIBLE

        info_button.visibility = if(showInfo) View.VISIBLE else View.INVISIBLE

    }

    override fun viewForSnackbar(): View {
        TODO("not implemented") //To change body of created functions use File | AppSettings | File Templates.
    }

    override fun loadRefreshPanel(): LoadRefreshPanel {
        TODO("not implemented") //To change body of created functions use File | AppSettings | File Templates.
    }


    enum class ToolbarType {
        Regular, Small
    }

}
