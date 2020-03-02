package com.fyp.emart.project.fragment.customer_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.ProductActivity;
import com.fyp.emart.project.adapters.MartAdapter;
import com.fyp.emart.project.adapters.ProductAdapter;
import com.fyp.emart.project.model.MartList;
import com.fyp.emart.project.model.ProductList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerHomeFragment extends Fragment {

    private RecyclerView mRecyclerViewMart;
    List<MartList> martLists;
    MartAdapter martAdapter;
    ProgressDialog progressDialog;

    Context mContext;
    BaseApiService mApiService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();
        mRecyclerViewMart = (RecyclerView) view.findViewById(R.id.mart_recycler_view);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading please wait...");


        martData();

        RecycleClick.addTo(mRecyclerViewMart).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Toast.makeText(getApplicationContext(), "id: "+productCategoryListPojos.get(position).getId(), Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getActivity(), ProductActivity.class);
                startActivity(i);
            }
        });
    }




    public void martData()
    {
        progressDialog.show();
         LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
       //GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1);
        mRecyclerViewMart.setLayoutManager(layoutManager);
        martAdapter = new MartAdapter(getActivity(),martLists);
        mRecyclerViewMart.setAdapter(martAdapter);

        Call<List<MartList>> martlistCall = mApiService.getMarts();
        martlistCall.enqueue(new Callback<List<MartList>>() {
            @Override
            public void onResponse(Call<List<MartList>> call, Response<List<MartList>> response) {
                progressDialog.dismiss();
                martLists = response.body();
                Log.d("TAG","Response = "+martLists);
                martAdapter.setMartList(martLists);
            }

            @Override
            public void onFailure(Call<List<MartList>> call, Throwable t) {
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
