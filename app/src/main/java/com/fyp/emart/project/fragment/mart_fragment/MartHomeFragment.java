package com.fyp.emart.project.fragment.mart_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chootdev.recycleclick.RecycleClick;
import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.AdminOrderAdapter;
import com.fyp.emart.project.adapters.MartAdapter;
import com.fyp.emart.project.adapters.MartHomeAdapter;
import com.fyp.emart.project.adapters.ProductAdapter;
import com.fyp.emart.project.model.AdminOrder;
import com.fyp.emart.project.model.MartList;
import com.fyp.emart.project.model.ProductList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MartHomeFragment extends Fragment {

    private RecyclerView mRecyclerViewMart;
    List<MartList> martLists;
    MartHomeAdapter martHomeAdapter;

    ProgressDialog progressDialog;

    List<AdminOrder> adminOrderList;

    Context mContext;
    BaseApiService mApiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mart_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();
        mRecyclerViewMart = view.findViewById(R.id.order_recycler_view);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading please wait...");


        orderData();


        RecycleClick.addTo(mRecyclerViewMart).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Toast.makeText(getApplicationContext(), "id: "+productCategoryListPojos.get(position).getId(), Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, position, Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(mContext, ProductActivity.class));
            }
        });
    }


    private void orderData() {
        progressDialog.show();

        mRecyclerViewMart.setLayoutManager(new LinearLayoutManager(mContext));
        martHomeAdapter = new MartHomeAdapter((List<AdminOrder>) martHomeAdapter,mContext);
        mRecyclerViewMart.setAdapter(martHomeAdapter);

        final Call<List<AdminOrder>> adminOrder = mApiService.getAdminorder();
        adminOrder.enqueue(new Callback<List<AdminOrder>>() {
            @Override
            public void onResponse(Call<List<AdminOrder>> call, Response<List<AdminOrder>> response) {
                progressDialog.dismiss();
                adminOrderList = response.body();
                Log.d("TAG","Response = "+adminOrderList);
                martHomeAdapter.setOrderList(adminOrderList);
            }

            @Override
            public void onFailure(Call<List<AdminOrder>> call, Throwable t) {
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
}



