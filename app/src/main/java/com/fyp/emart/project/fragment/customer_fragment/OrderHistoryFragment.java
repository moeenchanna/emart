package com.fyp.emart.project.fragment.customer_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chootdev.recycleclick.RecycleClick;
import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.BaseFragment;
import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.OrderAdapter;
import com.fyp.emart.project.model.OrderList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_iD;
import static com.fyp.emart.project.Api.DataConfig.MART_iD;

public class OrderHistoryFragment extends BaseFragment {

    private static int cart_count = 0;

    private RecyclerView mRecyclerViewMart;
    private OrderAdapter orderAdapter;
    private ProgressDialog progressDialog;

    List<OrderList> orderListList;

    private Context mContext;
    private BaseApiService mApiService;
    SharedPreferences loginPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cart_count = cartCount();

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();
        mRecyclerViewMart = view.findViewById(R.id.order_recycler_view);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading please wait...");

        SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        String customerid = sp.getString(MART_iD, null);
        orderData(customerid);


    }

    private void orderData(String id) {
        progressDialog.show();

        mRecyclerViewMart.setLayoutManager(new LinearLayoutManager(mContext));
        orderAdapter = new OrderAdapter(orderListList,mContext);
        mRecyclerViewMart.setAdapter(orderAdapter);

        final Call<List<OrderList>> adminOrder = mApiService.getOrderHistory(id);
        adminOrder.enqueue(new Callback<List<OrderList>>() {
            @Override
            public void onResponse(Call<List<OrderList>> call, Response<List<OrderList>> response) {
                progressDialog.dismiss();
                orderListList = response.body();
                Log.d("TAG","Response = "+ orderListList);
                orderAdapter.setOrderList(orderListList);
            }

            @Override
            public void onFailure(Call<List<OrderList>> call, Throwable t) {
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
