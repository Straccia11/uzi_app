package com.example.uziv2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class UziPageAdapter extends FragmentPagerAdapter {

    // 1 - Array of colors that will be passed to PageFragment
    // private int[] colors;

    // 2 - Default Constructor
    public UziPageAdapter(FragmentManager mgr) {
        super(mgr, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        // this.colors = colors;
    }

    @Override
    public int getCount() {
        return(3); // 3 - Number of page to show
    }

    @Override
    public Fragment getItem(int position) {
        // 4 - Page to return
        return(PageFragment.newInstance(position));
    }
}
