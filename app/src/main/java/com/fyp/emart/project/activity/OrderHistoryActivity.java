package com.fyp.emart.project.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.OrderHistoryAdapter;
import com.fyp.emart.project.model.ProductDetailsList;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_NAME;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_iD;
import static com.fyp.emart.project.Api.DataConfig.MART_NAME;
import static com.fyp.emart.project.Api.DataConfig.MART_iD;

public class OrderHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mBarTool;

    private RecyclerView orderhistory_list;
    OrderHistoryAdapter orderHistoryAdapter;
    List<ProductDetailsList> productDetailsLists;
    private ProgressDialog progressDialog;

    private Context mContext;
    private BaseApiService mApiService;
    private ImageView mComplaint;
    private ImageView mReview;

    private Button mBtnAdd;
    ProgressDialog loading;
    AlertDialog dialogBuilder;
    private TextInputEditText mTxtcomplaints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        mApiService = UtilsApi.getAPIService();
        mContext = this;

        initView();
    }

    private void initView() {
        mBarTool = (Toolbar) findViewById(R.id.tool_bar);
        mBarTool.setTitle("Order Product Details");
        setSupportActionBar(mBarTool);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String orderid = intent.getStringExtra("orderno");
        String statusid = intent.getStringExtra("statusid");
        String status = intent.getStringExtra("status");

        orderhistory_list = findViewById(R.id.produt_list);
        orderhistory_list.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading please wait...");



        mComplaint = (ImageView) findViewById(R.id.complaint);
        mComplaint.setOnClickListener(this);
        mReview = (ImageView) findViewById(R.id.review);
        mReview.setOnClickListener(this);

        getProductDetails(orderid);

        HorizontalStepView setpview5 = (HorizontalStepView) findViewById(R.id.step_view);
        List<StepBean> stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("Pending",1);
        StepBean stepBean1 = new StepBean("Process",1);
        StepBean stepBean2 = new StepBean("Dispatch",1);
        StepBean stepBean3 = new StepBean("Delivered",0);
        StepBean stepBean4 = new StepBean("Picket",-1);
        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);
        stepsBeanList.add(stepBean3);
        stepsBeanList.add(stepBean4);
        setpview5
                .setStepViewTexts(stepsBeanList)//总步骤
                .setTextSize(12)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(this, android.R.color.white))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(this, android.R.color.white))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.attention));//设置StepsViewIndicator AttentionIcon

    }

    private void getProductDetails(String orderid) {

        progressDialog.show();
        orderhistory_list.setLayoutManager(new LinearLayoutManager(mContext));
        orderHistoryAdapter = new OrderHistoryAdapter(productDetailsLists, mContext);
        orderhistory_list.setAdapter(orderHistoryAdapter);

        final Call<List<ProductDetailsList>> productsdetail = mApiService.geOrderDetails(orderid);
        productsdetail.enqueue(new Callback<List<ProductDetailsList>>() {
            @Override
            public void onResponse(@Nullable Call<List<ProductDetailsList>> call, @Nullable Response<List<ProductDetailsList>> response) {
                progressDialog.dismiss();
                productDetailsLists = response.body();
                Log.d("TAG", "Response = " + productDetailsLists);
                orderHistoryAdapter.setCodeLists(productDetailsLists);

            }

            @Override
            public void onFailure(@Nullable Call<List<ProductDetailsList>> call, @Nullable Throwable t) {
                progressDialog.dismiss();
                Log.e("Error", t.getMessage());
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complaint:
                complaintAlert();
                // TODO 20/05/20
                break;
            case R.id.review:
                reviewAlert();
                // TODO 20/05/20
                break;
            default:
                break;
        }
    }

    public void complaintAlert() {
        dialogBuilder = new AlertDialog.Builder(OrderHistoryActivity.this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.complain_layout, null);
        mTxtcomplaints = (TextInputEditText) dialogView.findViewById(R.id.txtcomplaints);
        mBtnAdd = (Button) dialogView.findViewById(R.id.btnAdd);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final String currentDateandTime = sdf.format(new Date());

        SharedPreferences sp = OrderHistoryActivity.this.getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        final String martid = sp.getString(MART_iD, null);
        final String martname = sp.getString(MART_NAME, null);
        final String custid = sp.getString(CUSTOMER_iD, null);
        final String custname = sp.getString(CUSTOMER_NAME, null);

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTxtcomplaints.getText().length() < 1) {
                    mTxtcomplaints.requestFocus();
                    mTxtcomplaints.setError("Complaint required.");
                } else {

                    String detail = mTxtcomplaints.getText().toString();
                    //Toast.makeText(mContext, token, Toast.LENGTH_SHORT).show();

                    loading = ProgressDialog.show(mContext, null, "Please wait...", true, false);
                    addComplaints(detail, currentDateandTime, custid, custname, martid, martname);

                }
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    public void reviewAlert() {
        dialogBuilder = new AlertDialog.Builder(OrderHistoryActivity.this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.review_layout, null);
        mTxtcomplaints = (TextInputEditText) dialogView.findViewById(R.id.txtcomplaints);
        mBtnAdd = (Button) dialogView.findViewById(R.id.btnAdd);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final String currentDateandTime = sdf.format(new Date());

        SharedPreferences sp = OrderHistoryActivity.this.getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        final String martid = sp.getString(MART_iD, null);
        final String martname = sp.getString(MART_NAME, null);
        final String custid = sp.getString(CUSTOMER_iD, null);
        final String custname = sp.getString(CUSTOMER_NAME, null);

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTxtcomplaints.getText().length() < 1) {
                    mTxtcomplaints.requestFocus();
                    mTxtcomplaints.setError("Review required.");
                } else {

                    String comment = mTxtcomplaints.getText().toString();
                    //Toast.makeText(mContext, token, Toast.LENGTH_SHORT).show();
                    loading = ProgressDialog.show(mContext, null, "Please wait...", true, false);
                    addReviews(comment, currentDateandTime, custid, custname, martid, martname);

                }

            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    private void addComplaints(String detail, String datetime, String custid, String custname, String martid, String martname) {

        mApiService.AddComplaints(detail, datetime, custid, custname, martid,martname)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                if (response.body() != null) {

                                    String role = response.body().string();
                                    //Toast.makeText(mContext, role, Toast.LENGTH_SHORT).show();
                                    Log.d("debug", role);
                                    dialogBuilder.dismiss();
                                    Toast.makeText(mContext, "Your Complaint Submit Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If the login fails
                                    // error case
                                    switch (response.code()) {
                                        case 404:
                                            Toast.makeText(OrderHistoryActivity.this, "Server not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 500:
                                            Toast.makeText(OrderHistoryActivity.this, "Server request not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(OrderHistoryActivity.this, "unknown error", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(OrderHistoryActivity.this, "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

    private void addReviews(String comment, String datetime, String custid, String custname, String martid, String martname) {

        mApiService.AddReview(comment, datetime, custid, custname, martid, martname)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                if (response.body() != null) {
                                    dialogBuilder.dismiss();
                                    String role = response.body().string();
                                    //Toast.makeText(mContext, role, Toast.LENGTH_SHORT).show();
                                    Log.d("debug", role);

                                    Toast.makeText(mContext, "Your Review Submit Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If the login fails
                                    // error case
                                    switch (response.code()) {
                                        case 404:
                                            Toast.makeText(OrderHistoryActivity.this, "Server not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 500:
                                            Toast.makeText(OrderHistoryActivity.this, "Server request not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(OrderHistoryActivity.this, "unknown error", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(OrderHistoryActivity.this, "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

}
