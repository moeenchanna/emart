package com.fyp.emart.project.fragment.admin_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.AdminOrderAdapter;
import com.fyp.emart.project.model.OrderList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrderFragment extends Fragment {

    private RecyclerView mRecyclerViewMart;
    private AdminOrderAdapter adminOrderAdapter;
    private ProgressDialog progressDialog;

    private List<OrderList> adminOrderListModel;

    private Context mContext;
    private BaseApiService mApiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_admin_order_fragment, container, false);
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

    }


    private void orderData() {
        progressDialog.show();

        mRecyclerViewMart.setLayoutManager(new LinearLayoutManager(mContext));
        adminOrderAdapter = new AdminOrderAdapter(adminOrderListModel,mContext);
        mRecyclerViewMart.setAdapter(adminOrderAdapter);

        final Call<List<OrderList>> adminOrder = mApiService.getAdminorder();
        adminOrder.enqueue(new Callback<List<OrderList>>() {
            @Override
            public void onResponse(@Nullable Call<List<OrderList>> call, @Nullable Response<List<OrderList>> response) {
                progressDialog.dismiss();
                adminOrderListModel = response.body();
                Log.d("TAG","Response = "+ adminOrderListModel);
                adminOrderAdapter.setOrderList(adminOrderListModel);

            }

            @Override
            public void onFailure(@Nullable Call<List<OrderList>> call, @Nullable Throwable t) {
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
