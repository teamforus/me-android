package io.forus.me.android.presentation.view.screens.welcome.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.appcompat.app.AppCompatActivity
import io.forus.me.android.presentation.view.screens.welcome.pages.*

class SectionPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        // Returns the number of fragments you want to include in welcome slider
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        // code to return the respective fragment based on the position
        return when (position) {
            0 -> WelcomeFragment1() // Make sure "Fragment1", "Fragment2" .. and so on exists or replace them with your fragment names
            1 -> WelcomeFragment2()
            2 -> WelcomeFragment3()
            3 -> WelcomeFragment4()
            4 -> WelcomeFragment5()
            else -> Fragment() // return a default fragment for non existing positions
        }
    }
}