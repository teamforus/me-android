package io.forus.me.android.presentation.view.adapters;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.forus.me.android.presentation.view.fragment.BaseFragment;


public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> titles;

    private Fragment currentFragment;



    public MainViewPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragments,List<String> titles) {
        super(fm);


        this.fragments = fragments;
        this.titles = titles;

    }

    public MainViewPagerAdapter(FragmentManager fm, Context context, List<BaseFragment> fragments) {
        super(fm);

        this.fragments = new ArrayList<>();
        this.titles = new ArrayList<>();

        for (BaseFragment fragment : fragments) {
            this.fragments.add(fragment);
            this.titles.add(fragment.getTitle());
        }


    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    /**
     * Get the current fragment
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

    public List<String> getTitles() {
        return titles;
    }
}