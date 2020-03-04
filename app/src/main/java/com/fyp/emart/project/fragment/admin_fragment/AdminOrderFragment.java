package com.fyp.emart.project.fragment.admin_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.ProductActivity;
import com.fyp.emart.project.adapters.OrderAdapter;
import com.fyp.emart.project.model.MartList;

import java.util.List;

public class AdminOrderFragment extends Fragment {

    private RecyclerView mRecyclerViewMart;
    private OrderAdapter orderAdapter;
    private ProgressDialog progressDialog;

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
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

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

                startActivity(new Intent(mContext, ProductActivity.class));
            }
        });
    }


    private void orderData() {
        progressDialog.show();

        mRecyclerViewMart.setLayoutManager(new LinearLayoutManager(mContext));
//        orderAdapter = new OrderAdapter(getActivity(),martLists);
//        mRecyclerViewMart.setAdapter(orderAdapter);
//
//        Call<List<MartList>> martlistCall = mApiService.getMarts();
//        martlistCall.enqueue(new Callback<List<MartList>>() {
//            @Override
//            public void onResponse(Call<List<MartList>> call, Response<List<MartList>> response) {
//                progressDialog.dismiss();
//                martLists = response.body();
//                Log.d("TAG","Response = "+martLists);
//                orderAdapter.setMartList(martLists);
//            }
//
//            @Override
//            public void onFailure(Call<List<MartList>> call, Throwable t) {
//                progressDialog.dismiss();
//                Log.e("Error", t.getMessage());
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }
}
