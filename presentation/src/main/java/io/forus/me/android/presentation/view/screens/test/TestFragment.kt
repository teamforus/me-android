
package io.forus.me.android.presentation.view.screens.test

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.fragment.BaseFragment
import java.util.*

/**
 * Fragment Welcome Screen.
 */
class TestFragment : BaseFragment() {

    private val randomTitle = Random().nextInt(1000).toString();

    companion object {
        fun newIntent(): TestFragment {
            return TestFragment()
        }
    }
    override fun getLayoutID(): Int {
        return R.layout.test_fragment
    }

    override fun getTitle(): String {
        return  randomTitle
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)


    }







    override fun initUI() {
    }
}

