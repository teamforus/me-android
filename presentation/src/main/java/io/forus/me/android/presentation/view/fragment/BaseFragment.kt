package io.forus.me.android.presentation.view.fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.Converter
import io.forus.me.android.presentation.interfaces.FragmentListener

import io.forus.me.android.presentation.navigation.Navigator



/**
 * Base [android.app.Fragment] class for every fragment in this application.
 */
abstract class BaseFragment : Fragment(), FragmentListener {

    private var toolbar_view: Toolbar? = null
    private var toolbar_title: TextView? = null
    private var profile_button: View? = null


    protected var navigator: Navigator = Navigator()


    override fun getTitle(): String {
        return ""
    }


    protected open val subviewFragment: BaseFragment?
        get() = null

    protected open val allowBack: Boolean
        get() = true

    protected val toolbar: Toolbar
        get() = toolbar_view!!

    protected open val toolbarTitle: String
        get() = ""

    protected open val toolbarType: ToolbarLRFragment.ToolbarType
        get() = ToolbarLRFragment.ToolbarType.Regular


    protected fun setToolbarTitle(title: String) {
        toolbar_title?.text = title

    }


    private fun initSubView() {
        if (subviewFragment != null) {
            fragmentManager?.beginTransaction()
                ?.replace(R.id.subview, subviewFragment!!)
                ?.commit()
        }

    }


    private fun setActionBarActivity(_activity: AppCompatActivity) {


        _activity.setSupportActionBar(toolbar)
        FragmentHelper.setHomeIconToolbar(_activity, toolbar, profile_button, allowBack)
    }

    /**
     * Shows a [android.widget.Toast] message.
     *
     * @param message An string representing a message to be shown.
     */
    protected fun showToastMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_view = view.findViewById(R.id.toolbar_view)
        toolbar_title = view.findViewById(R.id.toolbar_title)
        profile_button = view.findViewById(R.id.profile_button)

        initUI()
    }


    /**
     * Lifecycle init ui method.
     */
    protected open fun initUI() {
        setToolbarTitle(toolbarTitle)
        if (toolbarType == ToolbarLRFragment.ToolbarType.Small) {
            toolbar_title?.setPadding(
                toolbar_title?.paddingLeft?:0,
                Converter.convertDpToPixel(5f, requireActivity().applicationContext),
                toolbar_title?.paddingRight?:0,
                0
            )
        }


        val castActivity = requireActivity()
        when (castActivity) {
            is AppCompatActivity -> setActionBarActivity(castActivity)

        }

        initSubView()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        if (getLayoutID() > 0) inflater.inflate(getLayoutID(), container, false) else
            super.onCreateView(inflater, container, savedInstanceState)


    /**
     * Lifecycle init ui method.
     */
    protected open fun getLayoutID(): Int {
        return 0
    }

}
