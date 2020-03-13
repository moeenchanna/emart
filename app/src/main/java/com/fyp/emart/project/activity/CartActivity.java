package com.fyp.emart.project.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.BaseActivity;
import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.CartAdapter;
import com.fyp.emart.project.model.Cart;
import com.fyp.emart.project.utils.LocalStorage;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_EMAIL;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_iD;
import static com.fyp.emart.project.Api.DataConfig.MART_iD;
import static com.fyp.emart.project.Api.DataConfig.TEMP_PRODUCT_iD;

public class CartActivity extends BaseActivity implements View.OnClickListener {
    LocalStorage localStorage;
    List<Cart> cartList = new ArrayList<>();
    Gson gson;
    RecyclerView recyclerView;
    CartAdapter adapter;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    ImageView emptyCart;
    LinearLayout checkoutLL;
    TextView totalPrice;
    private String mState = "SHOW_MENU";

    private LinearLayout mCheckout;
    Context mContext;
    BaseApiService mApiService;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mContext = this;
        mApiService = UtilsApi.getAPIService(); // heat the contents of the package api helper

        initView();




    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mCheckout = (LinearLayout) findViewById(R.id.checkout);
        mCheckout.setOnClickListener(this);

        localStorage = new LocalStorage(getApplicationContext());
        gson = new Gson();
        emptyCart = findViewById(R.id.empty_cart_img);
        checkoutLL = findViewById(R.id.checkout_LL);
        totalPrice = findViewById(R.id.total_price);
        totalPrice.setText("Rs. " + getTotalPrice() + "");
        setUpCartRecyclerview();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        MenuItem item = menu.findItem(R.id.cart_delete);
        if (mState.equalsIgnoreCase("HIDE_MENU")) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()) {
            case R.id.cart_delete:

                AlertDialog diaBox = showDeleteDialog();
                diaBox.show();

                return true;

            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(CartActivity.this, CustomerDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AlertDialog showDeleteDialog() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)

                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.ic_delete_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        localStorage.deleteCart();
                        adapter.notifyDataSetChanged();
                        emptyCart.setVisibility(View.VISIBLE);
                        mState = "HIDE_MENU";
                        invalidateOptionsMenu();
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;
    }


    private void setUpCartRecyclerview() {
        cartList = new ArrayList<>();
        cartList = getCartList();
        if (cartList.isEmpty()) {
            mState = "HIDE_MENU";
            invalidateOptionsMenu();
            emptyCart.setVisibility(View.VISIBLE);
            checkoutLL.setVisibility(View.GONE);
        }
        recyclerView = findViewById(R.id.cart_rv);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        adapter = new CartAdapter(cartList, CartActivity.this);
        recyclerView.setAdapter(adapter);
    }


    public void onCheckoutClicked(View view) {

        // startActivity(new Intent(getApplicationContext(), CheckoutActivity.class));
    }


    @Override
    public void updateTotalPrice() {

        totalPrice.setText("Rs. " + getTotalPrice() + "");
        if (getTotalPrice() == 0.0) {
            mState = "HIDE_MENU";
            invalidateOptionsMenu();
            emptyCart.setVisibility(View.VISIBLE);
            checkoutLL.setVisibility(View.GONE);
        }
    }



    public void checkoutOrder() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Your order punch successfully.");
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "C.O.D",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Online Transaction",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkout:
                startActivity(new Intent(getApplicationContext(), CheckoutActivity.class));
               /* //generate order no
                SimpleDateFormat sm = new SimpleDateFormat("yyyyMMddHHmmss.SSS");
                Date myDate = new Date();
                String strDate = sm.format(myDate);
                String orderno = strDate.replace(".", "");

                //generate current date time
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                String curdatetime = formatter.format(date);

                String orderdetail =  "";
                String status = "processing";
                String statusid = "1";
                String subtotal = String.valueOf(getTotalPrice());

                SharedPreferences sp = getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
                String martid = sp.getString(TEMP_PRODUCT_iD, null);
                String custid = sp.getString(CUSTOMER_iD, null);
                String custemail = sp.getString(CUSTOMER_EMAIL, null);


                Toast.makeText(mContext, "orderdetail "+orderdetail, Toast.LENGTH_SHORT).show();
*/
                ///loading = ProgressDialog.show(this, null, "Please wait...", true, false);
                //punchOrder(orderno, orderdetail, curdatetime, status, statusid, subtotal, custemail, custid, martid);
                break;
            default:
                break;
        }
    }

    private void punchOrder(String orderno,String orderdetail,String curdatetime,String status,String statusid,String subtotal,String custemail,String custid,String martid) {
        mApiService.OrderPunch(orderno, orderdetail, curdatetime, status, statusid, subtotal, custemail, custid, martid)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                if (response.body() != null) {

                                    String role = response.body().string();
                                    localStorage.deleteCart();
                                    Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                                    checkoutOrder();
                                    Toast.makeText(mContext, role + " Order punch successfull", Toast.LENGTH_SHORT).show();
                                    Log.d("debug", role + "Order punch successfull");
                                } else {
                                    // If the login fails
                                    // error case
                                    switch (response.code()) {
                                        case 404:
                                            Toast.makeText(CartActivity.this, "Server not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 500:
                                            Toast.makeText(CartActivity.this, "Server request not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(CartActivity.this, "unknown error", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("checkerror", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(CartActivity.this, "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), CustomerDashboardActivity.class));
    }
}
