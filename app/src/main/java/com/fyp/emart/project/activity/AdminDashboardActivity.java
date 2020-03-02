package com.fyp.emart.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.ViewPagerAdapter;
import com.fyp.emart.project.fragment.admin_fragment.AdminComplainFragment;
import com.fyp.emart.project.fragment.admin_fragment.AdminOrderFragment;
import com.fyp.emart.project.fragment.admin_fragment.AdminProfileFragment;
import com.fyp.emart.project.fragment.customer_fragment.CustomerHomeFragment;
import com.fyp.emart.project.fragment.customer_fragment.CustomerProfileFragment;
import com.fyp.emart.project.fragment.customer_fragment.MartMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminDashboardActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationAdmin;
    private ViewPager mViewpagerAdmin;
    private MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        initView();


    }

    private void initView() {
        mBottomNavigationAdmin = (BottomNavigationView) findViewById(R.id.admin_bottom_navigation);
        mViewpagerAdmin = (ViewPager) findViewById(R.id.admin_viewpager);

        mBottomNavigationAdmin.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_order:
                                mViewpagerAdmin.setCurrentItem(0);
                                break;
                            case R.id.action_complain:
                                mViewpagerAdmin.setCurrentItem(1);
                                break;
                            case R.id.action_profile:
                                mViewpagerAdmin.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        mViewpagerAdmin.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    mBottomNavigationAdmin.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                mBottomNavigationAdmin.getMenu().getItem(position).setChecked(true);
                prevMenuItem = mBottomNavigationAdmin.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(mViewpagerAdmin);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        AdminOrderFragment adminOrderFragment = new AdminOrderFragment();
        AdminComplainFragment adminComplainFragment = new AdminComplainFragment();
        AdminProfileFragment adminProfileFragment = new AdminProfileFragment();

        adapter.addFragment(adminOrderFragment);
        adapter.addFragment(adminComplainFragment);
        adapter.addFragment(adminProfileFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent(AdminDashboardActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}