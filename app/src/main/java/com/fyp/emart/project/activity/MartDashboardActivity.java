package com.fyp.emart.project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.ViewPagerAdapter;
import com.fyp.emart.project.fragment.customer_fragment.CustomerHomeFragment;
import com.fyp.emart.project.fragment.customer_fragment.CustomerProfileFragment;
import com.fyp.emart.project.fragment.customer_fragment.MartMapFragment;
import com.fyp.emart.project.fragment.mart_fragment.MartComplaintFragment;
import com.fyp.emart.project.fragment.mart_fragment.MartHomeFragment;
import com.fyp.emart.project.fragment.mart_fragment.MartProfileFragment;
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
                            case R.id.action_complaints:
                                mViewpager.setCurrentItem(1);
                                break;
                            case R.id.action_profile:
                                mViewpager.setCurrentItem(2);
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
        MartComplaintFragment martComplaintFragment = new MartComplaintFragment();
        MartProfileFragment martProfileFragment = new MartProfileFragment();

        adapter.addFragment(martHomeFragment);
        adapter.addFragment(martComplaintFragment);
        adapter.addFragment(martProfileFragment);
        viewPager.setAdapter(adapter);
    }

}
