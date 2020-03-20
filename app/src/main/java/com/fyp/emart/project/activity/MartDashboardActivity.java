package com.fyp.emart.project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.ViewPagerAdapter;
import com.fyp.emart.project.fragment.mart_fragment.MartComplaintListFragment;
import com.fyp.emart.project.fragment.mart_fragment.MartHomeFragment;
import com.fyp.emart.project.fragment.mart_fragment.MartProductFragment;
import com.fyp.emart.project.fragment.mart_fragment.MartProfileFragment;
import com.fyp.emart.project.fragment.mart_fragment.MartReviewListFragment;
import com.fyp.emart.project.utils.SaveSharedPreference;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MartDashboardActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigation;
    private ViewPager mViewpager;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mart_dashboard);
        initView();
    }

    private void initView() {
        mBottomNavigation = (BottomNavigationView) findViewById(R.id.mart_bottom_navigation);
        mViewpager = (ViewPager) findViewById(R.id.mart_viewpager);

        mBottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_receive:
                                mViewpager.setCurrentItem(0);
                                break;
                            case R.id.action_product:
                                mViewpager.setCurrentItem(1);
                                break;
                            case R.id.action_complaints:
                                mViewpager.setCurrentItem(2);
                                break;
                            case R.id.action_reviews:
                                mViewpager.setCurrentItem(3);
                                break;
                            case R.id.action_profile:
                                mViewpager.setCurrentItem(4);
                                break;
                        }
                        return false;
                    }
                });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    mBottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                mBottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = mBottomNavigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(mViewpager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        MartHomeFragment martHomeFragment = new MartHomeFragment();
        MartProductFragment martProductFragment = new MartProductFragment();

        MartComplaintListFragment martComplaintListFragment = new MartComplaintListFragment();
        MartReviewListFragment martReviewListFragment = new MartReviewListFragment();
        MartProfileFragment martProfileFragment = new MartProfileFragment();

        adapter.addFragment(martHomeFragment);
        adapter.addFragment(martProductFragment);
        adapter.addFragment(martComplaintListFragment);
        adapter.addFragment(martReviewListFragment);
        adapter.addFragment(martProfileFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }



}
