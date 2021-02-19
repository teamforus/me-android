package io.forus.me.android.presentation.view.screens.onboarding_screens.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.helpers.ScreenHelper
import kotlinx.android.synthetic.main.fragment_onboard_welcome.*


private const val ARG_POSITION = "ARG_POSITION"


class OnboardDigitalWalletFragment : Fragment() {
    private var position: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboard_digital_wallet, container, false)
    }

    companion object {


        @JvmStatic
        fun newInstance(position: Int) =
                OnboardDigitalWalletFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_POSITION, position)
                    }
                }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ScreenHelper.isSmallScreen(activity!!)) {

            imageLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0,
                    OnBoardScreens.Small.imageWeight.weight)

            contentLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0,
                    OnBoardScreens.Small.contentWeight.weight)
        } else {

            imageLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0,
                    OnBoardScreens.Normal.imageWeight.weight)

            contentLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0,
                    OnBoardScreens.Normal.contentWeight.weight)

        }

    }

}