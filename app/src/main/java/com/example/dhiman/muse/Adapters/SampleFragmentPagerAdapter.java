package com.example.dhiman.muse.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.dhiman.muse.Fragments.Dashboard;
import com.example.dhiman.muse.Fragments.MyMusic;

/**
 * Created by dhiman on 23/3/18.
 */

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Dashboard", "My Music" };
    private Dashboard dashboard;
    private MyMusic myMusic;

    public SampleFragmentPagerAdapter(FragmentManager fm,Dashboard dashboard,MyMusic myMusic) {
        super(fm);
        this.dashboard = dashboard;
        this.myMusic = myMusic;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
       if(position==0){
           return dashboard;
       }
       else if(position ==1){
           return myMusic;
       }
       else{
           return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
