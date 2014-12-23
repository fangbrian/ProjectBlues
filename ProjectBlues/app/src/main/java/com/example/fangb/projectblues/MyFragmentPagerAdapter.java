package com.example.fangb.projectblues;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by fangb on 12/22/2014.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;

    /** Constructor of the class */
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /** This method will be invoked when a page is requested to create */
    @Override
    public Fragment getItem(int index) {
        switch(index){

            /** tab1 is selected */
            case 0:
                ScheduleFragment fragment1 = new ScheduleFragment();
                return fragment1;

            /** tab2 is selected */
            case 1:
                RankingFragment fragment2 = new RankingFragment();
                return fragment2;

            default:
                return null;
        }

    }

    /** Returns the number of pages */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}