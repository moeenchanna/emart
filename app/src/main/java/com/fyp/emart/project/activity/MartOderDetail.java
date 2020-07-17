package com.fyp.emart.project.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.OrderHistoryAdapter;
import com.fyp.emart.project.model.ProductDetailsList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MartOderDetail extends AppCompatActivity {

    private Toolbar mBarTool;
    private RecyclerView mListProdut;

    private RecyclerView orderhistory_list;
    OrderHistoryAdapter orderHistoryAdapter;
    List<ProductDetailsList> productDetailsLists;
    private ProgressDialog progressDialog;

    private Context mContext;
    private BaseApiService mApiService;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mart_oder_detail);
        initView();




    }

    private void initView() {

        mApiService = UtilsApi.getAPIService();
        mContext = this;
        
        mBarTool = (Toolbar) findViewById(R.id.tool_bar);
        mListProdut = (RecyclerView) findViewById(R.id.produt_list);

        mBarTool.setTitle("Order Product Details");
        setSupportActionBar(mBarTool);

        Intent intent = getIntent();
        String orderid = intent.getStringExtra("orderno");

        orderhistory_list = findViewById(R.id.produt_list);
        orderhistory_list.setLayoutManager(new LinearLayoutManager(this));
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading please wait...");

        getProductDetails(orderid);
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
}