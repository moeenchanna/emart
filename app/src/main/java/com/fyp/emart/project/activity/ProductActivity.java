package com.fyp.emart.project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.BaseActivity;
import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.ProductAdapter;
import com.fyp.emart.project.helper.Converter;
import com.fyp.emart.project.model.ProductList;

import java.util.List;

public class ProductActivity extends BaseActivity {
    private static int cart_count = 0;

    private RecyclerView mRecyclerViewProduct;
    List<ProductList> productLists;
    ProductAdapter productAdapter;

    private SearchView searchView;
    ProgressDialog progressDialog;

    Context mContext;
    BaseApiService mApiService;
    String martid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        martid = intent.getStringExtra("id");
        initView();

    }

    private void initView() {



        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        cart_count = cartCount();

        mApiService = UtilsApi.getAPIService();
        mContext = this;
        mRecyclerViewProduct = (RecyclerView) findViewById(R.id.products_recycler_view);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading please wait...");

        productData();




    }

    public void productData()
    {
        progressDialog.show();
        GridLayoutManager layoutManager = new GridLayoutManager(ProductActivity.this,2);
        mRecyclerViewProduct.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(this,productLists);
        mRecyclerViewProduct.setAdapter(productAdapter);

        Call<List<ProductList>> productlistCall = mApiService.getProducts(martid);
        productlistCall.enqueue(new Callback<List<ProductList>>() {
            @Override
            public void onResponse(Call<List<ProductList>> call, Response<List<ProductList>> response) {
                progressDialog.dismiss();
                productLists = response.body();
                Log.d("TAG","Response = "+productLists);
                productAdapter.setProductList(ProductActivity.this,productLists);
            }

            @Override
            public void onFailure(Call<List<ProductList>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Error", t.getMessage());
                Toast.makeText(ProductActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(ProductActivity.this, cart_count, R.drawable.ic_shopping_basket));

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

    }

}
