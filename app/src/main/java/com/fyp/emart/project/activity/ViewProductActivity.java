package com.fyp.emart.project.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.MartAdapter;
import com.fyp.emart.project.adapters.MartProductAdapter;
import com.fyp.emart.project.adapters.ProductAdapter;
import com.fyp.emart.project.model.MartList;
import com.fyp.emart.project.model.ProductList;
import com.fyp.emart.project.utils.SaveSharedPreference;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fyp.emart.project.Api.DataConfig.MART_iD;

public class ViewProductActivity extends AppCompatActivity implements View.OnClickListener {


    private ProgressDialog progressDialog;
    private Context mContext;
    private BaseApiService mApiService;
    private ImageView mLogout;
    private Toolbar mBarTool;

    private RecyclerView mRecyclerViewProduct;
    List<ProductList> productLists;
    MartProductAdapter productAdapter;
    ImageView emptyCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        mApiService = UtilsApi.getAPIService();
        mContext = this;


        initView();
    }

    private void initView() {
        mLogout = (ImageView) findViewById(R.id.logout);
        mLogout.setOnClickListener(this);
        mBarTool = (Toolbar) findViewById(R.id.tool_bar);
        emptyCart = findViewById(R.id.empty_cart_img);
        mRecyclerViewProduct = (RecyclerView) findViewById(R.id.mart_recycler_view);

        mBarTool.setTitle("Products List");
        setSupportActionBar(mBarTool);

        progressDialog = new ProgressDialog(ViewProductActivity.this);
        progressDialog.setMessage("Loading please wait...");

        SharedPreferences sp = getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        final String martid = sp.getString(MART_iD, null);
        productData(martid);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                // TODO 20/07/16
                logout();
                break;
            default:
                break;
        }
    }
    public void logout() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ViewProductActivity.this);
        builder1.setMessage("Are you sure you want to logout?");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SaveSharedPreference.setLoggedIn(ViewProductActivity.this, false);
                        Intent intent = new Intent(ViewProductActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void productData(String martid)
    {

        productLists = new ArrayList<>();

        if (productLists.isEmpty()) {

            emptyCart.setVisibility(View.VISIBLE);

        }
        progressDialog.show();
        GridLayoutManager layoutManager = new GridLayoutManager(ViewProductActivity.this,2);
        mRecyclerViewProduct.setLayoutManager(layoutManager);
        productAdapter = new MartProductAdapter(this,productLists);
        mRecyclerViewProduct.setAdapter(productAdapter);

        Call<List<ProductList>> productlistCall = mApiService.getProducts(martid);
        productlistCall.enqueue(new Callback<List<ProductList>>() {
            @Override
            public void onResponse(Call<List<ProductList>> call, Response<List<ProductList>> response) {
                progressDialog.dismiss();
                productLists = response.body();
                emptyCart.setVisibility(View.INVISIBLE);
                Log.d("TAG","Response = "+productLists);
                productAdapter.setProductList(productLists);
            }

            @Override
            public void onFailure(Call<List<ProductList>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Error", t.getMessage());
                // noProductAlert();
                //Toast.makeText(ProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }
    }
}