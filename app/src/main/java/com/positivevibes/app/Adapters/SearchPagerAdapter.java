package com.positivevibes.app.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.positivevibes.app.Fragments.SearchAuthors;
import com.positivevibes.app.Fragments.SearchCategory;

public class SearchPagerAdapter extends FragmentPagerAdapter {

    int myNumofTabs;

    public SearchPagerAdapter(FragmentManager fm, int NumofTabs) {
        super(fm);
        this.myNumofTabs = NumofTabs;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SearchAuthors tab1 = new SearchAuthors();
                return tab1;
            case 1:
                SearchCategory tab2 = new SearchCategory();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return myNumofTabs;
    }
}
