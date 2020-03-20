package com.fyp.emart.project.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fyp.emart.project.BaseActivity;
import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.ViewPagerAdapter;
import com.fyp.emart.project.fragment.customer_fragment.CustomerComplaintListFragment;
import com.fyp.emart.project.fragment.customer_fragment.CustomerHomeFragment;
import com.fyp.emart.project.fragment.customer_fragment.CustomerProfileFragment;
import com.fyp.emart.project.fragment.customer_fragment.CustomerMapFragment;
import com.fyp.emart.project.fragment.customer_fragment.CustomerReviewListFragment;
import com.fyp.emart.project.fragment.customer_fragment.OrderHistoryFragment;
import com.fyp.emart.project.helper.Converter;
import com.fyp.emart.project.utils.SaveSharedPreference;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class CustomerDashboardActivity extends BaseActivity {


    private BottomNavigationView mBottomNavigation;
    private ViewPager mViewpager;
    MenuItem prevMenuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initView();

    }

    private void initView() {

        mBottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mViewpager = (ViewPager) findViewById(R.id.viewpager);

        mBottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.action_map:
                                mViewpager.setCurrentItem(0);
                                break;
                            case R.id.action_home:
                                mViewpager.setCurrentItem(1);
                                break;
                            case R.id.action_orders:
                                mViewpager.setCurrentItem(2);
                                break;
                            case R.id.action_complaints:
                                mViewpager.setCurrentItem(3);
                                break;
                            case R.id.action_reviews:
                                mViewpager.setCurrentItem(4);
                                break;
                            case R.id.action_profile:
                                mViewpager.setCurrentItem(5);
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

        CustomerHomeFragment customerHomeFragment = new CustomerHomeFragment();
        CustomerMapFragment martMapFragment = new CustomerMapFragment();
        OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
        CustomerComplaintListFragment customerComplaintListFragment = new CustomerComplaintListFragment();
        CustomerReviewListFragment reviewListFragment = new CustomerReviewListFragment();
        CustomerProfileFragment customerProfileFragment = new CustomerProfileFragment();

        adapter.addFragment(martMapFragment);
        adapter.addFragment(customerHomeFragment);
        adapter.addFragment(orderHistoryFragment);
        adapter.addFragment(customerComplaintListFragment);
        adapter.addFragment(reviewListFragment);
       // adapter.addFragment(customerProfileFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }



}
