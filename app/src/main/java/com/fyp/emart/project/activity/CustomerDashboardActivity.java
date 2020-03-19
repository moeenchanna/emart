package com.fyp.emart.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.fyp.emart.project.BaseActivity;
import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.ViewPagerAdapter;
import com.fyp.emart.project.fragment.customer_fragment.CustomerHomeFragment;
import com.fyp.emart.project.fragment.customer_fragment.CustomerProfileFragment;
import com.fyp.emart.project.fragment.customer_fragment.CustomerMapFragment;
import com.fyp.emart.project.fragment.customer_fragment.OrderHistoryFragment;
import com.fyp.emart.project.helper.Converter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class CustomerDashboardActivity extends BaseActivity {
    private static int cart_count = 0;

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
        cart_count = cartCount();

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
                            case R.id.action_orderz:
                                mViewpager.setCurrentItem(2);
                                break;
                            case R.id.action_profile:
                                mViewpager.setCurrentItem(3);
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
        CustomerProfileFragment customerProfileFragment = new CustomerProfileFragment();

        adapter.addFragment(customerHomeFragment);
        adapter.addFragment(martMapFragment);
        adapter.addFragment(orderHistoryFragment);
        adapter.addFragment(customerProfileFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    public void onAddProduct() {
        super.onAddProduct();
        cart_count++;
        invalidateOptionsMenu();

    }

    @Override
    public void onRemoveProduct() {
        super.onRemoveProduct();
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(CustomerDashboardActivity.this, cart_count, R.drawable.ic_shopping_basket));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart_action:
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }*/
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.logout_main, menu);
      return true;
  }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout_action:

                Intent i = new Intent(CustomerDashboardActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
