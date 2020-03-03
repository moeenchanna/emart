package com.fyp.emart.project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.ProductAdapter;
import com.fyp.emart.project.model.ProductList;

import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewProduct;
    List<ProductList> productLists;
    ProductAdapter productAdapter;
    private SearchView searchView;
    ProgressDialog progressDialog;

    Context mContext;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        initView();

    }

    private void initView() {

        mApiService = UtilsApi.getAPIService();
        mContext = this;
        mRecyclerViewProduct = (RecyclerView) findViewById(R.id.products_recycler_view);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading please wait...");

        productData();

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.searchitems);
        searchView.setFocusable(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        searchView.setActivated(true);
        searchView.animate();
        searchView.setQueryHint("Search products here...");

        searchView.onActionViewExpanded();
        searchView.clearFocus();
        searchView.setFocusableInTouchMode(true);
        searchView.setIconifiedByDefault(false);
        /* Code for changing the textcolor and hint color for the search view */

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        searchAutoComplete.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        searchAutoComplete.setTextSize(15);

        /*Code for changing the search icon */
        ImageView searchIcon = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        searchIcon.setImageResource(R.drawable.searchpic);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productAdapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                productAdapter.getFilter().filter(query);
                return false;
            }
        });



    }

    public void productData()
    {
        progressDialog.show();
        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductActivity.this);
        mRecyclerViewProduct.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(this,productLists);
        mRecyclerViewProduct.setAdapter(productAdapter);

        Call<List<ProductList>> productlistCall = mApiService.getProducts();
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
}
