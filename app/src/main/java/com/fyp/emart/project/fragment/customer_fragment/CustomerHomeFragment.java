package com.fyp.emart.project.fragment.customer_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.BaseFragment;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.LoginActivity;
import com.fyp.emart.project.activity.ProductActivity;
import com.fyp.emart.project.adapters.MartAdapter;
import com.fyp.emart.project.adapters.ProductAdapter;
import com.fyp.emart.project.model.MartList;
import com.fyp.emart.project.model.ProductList;
import com.fyp.emart.project.utils.SaveSharedPreference;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerHomeFragment extends BaseFragment  implements View.OnClickListener{
    private static int cart_count = 0;
    private RecyclerView mRecyclerViewMart;
    private List<MartList> martLists;
    private MartAdapter martAdapter;
    private ProgressDialog progressDialog;

    private Context mContext;
    private BaseApiService mApiService;
    private ImageView mLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        cart_count = cartCount();

        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();
        mRecyclerViewMart = (RecyclerView) view.findViewById(R.id.mart_recycler_view);


        mLogout = (ImageView) view.findViewById(R.id.logout);
        mLogout.setOnClickListener(this);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading please wait...");


        martData();



        RecycleClick.addTo(mRecyclerViewMart).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Toast.makeText(getActivity(), "id: "+martLists.get(position).getId(), Toast.LENGTH_SHORT).show();
                Bundle b = new Bundle();
                Intent i = new Intent(getActivity(), ProductActivity.class);
                b.putString("id",martLists.get(position).getId());
                i.putExtras(b);
                startActivity(i);
            }
        });
    }




    public void martData()
    {
        progressDialog.show();
         LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
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

    public void  logout(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to logout?");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SaveSharedPreference.setLoggedIn(getActivity(), false);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                logout();
                break;
            default:
                break;
        }
    }
}
