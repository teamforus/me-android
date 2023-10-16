package io.forus.me.android.presentation.view.screens.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import io.forus.me.android.presentation.R
import io.forus.me.android.presentation.databinding.ActivityWelcomeBinding
import io.forus.me.android.presentation.view.activity.BaseActivity
import io.forus.me.android.presentation.view.screens.welcome.adapter.SectionPagerAdapter
//import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity() {

    private var mustGoToLogin = false
    
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            mustGoToLogin = intent.getBooleanExtra(GO_TO_LOGIN, false)
        }

        val adapter = SectionPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = adapter

        Log.d("MDSASDADS", "${viewPager.itemDecorationCount}   ${adapter.itemCount}")
        val dotsIndicator = findViewById<me.relex.circleindicator.CircleIndicator3>(R.id.indicator)
        dotsIndicator.setViewPager(viewPager)


        binding.backBt.setOnClickListener {
            if (viewPager.currentItem == 0) {
                exitWelcome()
            }else{
                val nextItem = viewPager.currentItem - 1
                if (nextItem >= 0) {
                    viewPager.currentItem = nextItem
                }
            }
        }


        binding.btNext.setOnClickListener {
            if (viewPager.currentItem < 4) {
                val nextItem = viewPager.currentItem + 1
                if (nextItem < viewPager.adapter?.itemCount ?: 0) {
                    viewPager.currentItem = nextItem
                }
            } else {
                exitWelcome()
            }
        }

        binding.btSkipTutorial.setOnClickListener {
            exitWelcome()
        }

        binding.btSkip.setOnClickListener {
            exitWelcome()
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.btNext.text = if (position == 4) {
                    getString(R.string.close)
                } else {
                    getString(R.string.next_step)
                }


                if (position == 0) {
                    binding.btSkipTutorial.apply {
                        visibility = View.VISIBLE
                        alpha = 0.0f
                        animate()
                            .setDuration(200)
                            .alpha(1.0f)
                            .start()
                    }
                } else {
                    binding.btSkipTutorial.animate()
                        .setDuration(200)
                        .alpha(0.0f)
                        .withEndAction {
                            binding.btSkipTutorial.visibility = View.GONE
                        }
                        .start()
                }


                if (mustGoToLogin) {
                    binding.backBt.apply {
                        when (position) {
                            0 -> animate()
                                .setDuration(200)
                                .alpha(0.0f)
                                .withEndAction { visibility = View.GONE }
                                .start()
                            1 -> {
                                visibility = View.VISIBLE
                                alpha = 0.0f
                                animate()
                                    .setDuration(200)
                                    .alpha(1.0f)
                                    .start()
                            }
                        }
                    }
                } else {
                    binding.backBt.visibility = View.VISIBLE
                }


                if (position == 0) {
                    binding.btSkip.animate()
                        .setDuration(200)
                        .alpha(0.0f)
                        .withEndAction {
                            binding.btSkip.visibility = View.GONE
                        }
                        .start()


                } else if (position == 1) {
                    binding.btSkip.apply {
                        visibility = View.VISIBLE
                        alpha = 0.0f
                        animate()
                            .setDuration(200)
                            .alpha(1.0f)
                            .start()
                    }
                }


            }

        })

    }

    private fun exitWelcome() {
        if (mustGoToLogin) {
            this.navigator.navigateToLoginSignUp(this)
        }
        finish()
    }


    companion object {
        private const val GO_TO_LOGIN = "GO_TO_LOGIN"
        fun getCallingIntent(context: Context, goToLogin: Boolean) =
            Intent(context, WelcomeActivity::class.java).apply {
                putExtra(GO_TO_LOGIN, goToLogin)
            }
    }
}