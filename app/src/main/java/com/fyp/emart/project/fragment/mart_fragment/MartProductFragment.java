package com.fyp.emart.project.fragment.mart_fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.ProductActivity;
import com.fyp.emart.project.adapters.ProductAdapter;
import com.fyp.emart.project.model.ProductList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MartProductFragment extends Fragment {

    private RecyclerView mRecyclerViewProduct;
    List<ProductList> productLists;
    ProductAdapter productAdapter;
    private SearchView searchView;
    ProgressDialog progressDialog;

    Context mContext;
    BaseApiService mApiService;
    private TextView mBrowse;
    private RelativeLayout mLayout;
    private RelativeLayout mRelSearch;
    private LinearLayout mTitleMart;
    private RecyclerView mRecyclerViewProducts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mart_product, container, false);
initView(v);
        // Inflate the layout for this fragment
        return v;


    }

    private void initView(@NonNull final View itemView) {
        mBrowse = (TextView) itemView.findViewById(R.id.browse);
        searchView = (SearchView) itemView.findViewById(R.id.searchitems);
        mLayout = (RelativeLayout) itemView.findViewById(R.id.layout);
        mRelSearch = (RelativeLayout) itemView.findViewById(R.id.relSearch);
        mTitleMart = (LinearLayout) itemView.findViewById(R.id.mart_title);
        mRecyclerViewProducts = (RecyclerView) itemView.findViewById(R.id.products_recycler_view);

        mApiService = UtilsApi.getAPIService();
        mContext = getContext();
        mRecyclerViewProduct = (RecyclerView) itemView.findViewById(R.id.products_recycler_view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading please wait...");

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setFocusable(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        // searchView.setQuery("", false);

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setActivated(true);
        // searchView.setQueryHint(Html.fromHtml("<font color = #310246>" + "Search and select products" + "</font>"));
        searchView.animate();
        searchView.setQueryHint("Search products here...");

        searchView.onActionViewExpanded();
        searchView.clearFocus();
        searchView.setFocusableInTouchMode(true);
        searchView.setIconifiedByDefault(false);
        /* Code for changing the textcolor and hint color for the search view */

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        searchAutoComplete.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
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
        productData();

    }

    public void productData()
    {
        progressDialog.show();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewProduct.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(getActivity(),productLists);
        mRecyclerViewProduct.setAdapter(productAdapter);

        Call<List<ProductList>> productlistCall = mApiService.getProducts();
        productlistCall.enqueue(new Callback<List<ProductList>>() {
            @Override
            public void onResponse(Call<List<ProductList>> call, Response<List<ProductList>> response) {
                progressDialog.dismiss();
                productLists = response.body();
                Log.d("TAG","Response = "+productLists);
                productAdapter.setProductList(getActivity(),productLists);
            }

            @Override
            public void onFailure(Call<List<ProductList>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Error", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
