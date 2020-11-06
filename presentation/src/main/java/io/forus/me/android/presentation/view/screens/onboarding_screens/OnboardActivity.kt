package io.forus.me.android.presentation.view.screens.onboarding_screens


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.screens.onboarding_screens.fragments.*
import kotlinx.android.synthetic.main.activity_onboard.*


class OnboardActivity : BaseActivity() {


    companion object {
        val PAGE_COUNT = 5

        fun getCallingIntent(context: Context): Intent {
            return Intent(context, OnboardActivity::class.java)
        }
    }

    var pagerAdapter: MyFragmentPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard)

        backBt.visibility = View.INVISIBLE

        pagerAdapter = MyFragmentPagerAdapter(supportFragmentManager)
        pager.adapter = pagerAdapter
        dots_indicator.setViewPager(pager)
        pager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    animateView(backBt, false);
                } else {
                    animateView(backBt, true);
                }
                nextBt.text = if (position == PAGE_COUNT - 1) {
                    getString(R.string.onboard_login)
                } else {
                    getString(R.string.onboard_next)
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        nextBt.setOnClickListener {
            if (pager.currentItem < PAGE_COUNT - 1) {
                val newPosition = pager.currentItem + 1
                pager.setCurrentItem(newPosition, true)
            }else if (pager.currentItem == PAGE_COUNT - 1){
                finish()
            }
        }

        backBt.setOnClickListener {
            if (pager.currentItem > 0) {
                val newPosition = pager.currentItem - 1
                pager.setCurrentItem(newPosition, true)
            }
        }
    }


    class MyFragmentPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return PAGE_COUNT
        }

        override fun getItem(position: Int): Fragment {

            return when (position) {
                0 -> OnboardWelcomeFragment.newInstance(position)
                1 -> OnboardDigitalWalletFragment.newInstance(position)
                2 -> OnboardCashRegisterFragment.newInstance(position)
                3 -> OnboardRecordsStorageFragment.newInstance(position)
                4 -> OnboardLoginQRFragment.newInstance(position)
                else -> OnboardDigitalWalletFragment.newInstance(position)
            }
        }
    }

    private fun animateView(view: View, isVisible: Boolean) {
        backBt.animate()
                .alpha(if (isVisible) {
                    1.0f
                } else {
                    0.0f
                })
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        backBt.visibility = if (isVisible) {
                            View.VISIBLE
                        } else {
                            View.INVISIBLE
                        }
                    }
                })
    }
}
