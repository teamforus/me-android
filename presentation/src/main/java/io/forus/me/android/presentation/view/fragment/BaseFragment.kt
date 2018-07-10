package io.forus.me.android.presentation.view.fragment

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.interfaces.FragmentListener

import io.forus.me.android.presentation.navigation.Navigator

//import io.forus.me.android.presentation.internal.di.HasComponent;

/**
 * Base [android.app.Fragment] class for every fragment in this application.
 */
abstract class BaseFragment : Fragment(), FragmentListener {

    protected var navigator: Navigator = Navigator()


    override fun getTitle() : String {
        return  ""
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
        initUI()
    }


    /**
     * Lifecycle init ui method.
     */
    protected open fun initUI(){

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            if (getLayoutID() > 0) inflater.inflate(getLayoutID(), container, false) else super.onCreateView(inflater, container, savedInstanceState)


    /**
     * Lifecycle init ui method.
     */
    protected open fun getLayoutID(): Int{
        return 0;
    }
    //  /**
    //   * Gets a component for dependency injection by its type.
    //   */
    //  @SuppressWarnings("unchecked")
    //  protected <C> C getComponent(Class<C> componentType) {
    //    return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    //  }
}
