package com.fyp.emart.project.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.ViewPagerAdapter;
import com.fyp.emart.project.fragment.customer_fragment.CustomerHomeFragment;
import com.fyp.emart.project.fragment.customer_fragment.CustomerProfileFragment;
import com.fyp.emart.project.fragment.customer_fragment.MartMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class DashboardActivity extends AppCompatActivity {

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
                            case R.id.action_home:
                                mViewpager.setCurrentItem(0);
                                break;
                            case R.id.action_map:
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

        CustomerHomeFragment customerHomeFragment = new CustomerHomeFragment();
        MartMapFragment martMapFragment = new MartMapFragment();
        CustomerProfileFragment customerProfileFragment = new CustomerProfileFragment();

        adapter.addFragment(customerHomeFragment);
        adapter.addFragment(martMapFragment);
        adapter.addFragment(customerProfileFragment);
        viewPager.setAdapter(adapter);
    }

}
