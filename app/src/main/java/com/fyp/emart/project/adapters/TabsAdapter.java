package com.fyp.emart.project.adapters;

import com.fyp.emart.project.fragment.signup_fragment.CustomerSignupFragment;
import com.fyp.emart.project.fragment.signup_fragment.MartSignupFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public TabsAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                CustomerSignupFragment customerSignupFragment = new CustomerSignupFragment();
                return customerSignupFragment;
            case 1:
                MartSignupFragment martSignupFragment = new MartSignupFragment();
                return martSignupFragment;

            default:
                return null;
        }
    }
}