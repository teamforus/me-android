package io.forus.me.android.presentation.view.screens.onboarding_screens.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.forus.me.android.presentation.R
import kotlinx.android.synthetic.main.fragment_onboard_welcome.*


private const val ARG_POSITION = "ARG_POSITION"


class OnboardWelcomeFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_onboard_welcome, container, false)
    }

    companion object {


        @JvmStatic
        fun newInstance(position: Int) =
                OnboardWelcomeFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_POSITION, position)
                    }
                }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sklipBt.setOnClickListener {
            activity?.finish()
        }
    }
}