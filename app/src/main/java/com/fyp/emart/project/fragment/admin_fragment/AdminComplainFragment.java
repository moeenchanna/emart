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
import com.fyp.emart.project.adapters.AdminComplaintAdapter;
import com.fyp.emart.project.adapters.AdminOrderAdapter;
import com.fyp.emart.project.model.ComplaintList;
import com.fyp.emart.project.model.OrderList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminComplainFragment extends Fragment {

    private RecyclerView complainrecycler;
    private AdminComplaintAdapter complaintAdapter;
    private ProgressDialog progressDialog;

    private List<ComplaintList> complaintModel;

    private Context mContext;
    private BaseApiService mApiService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_admin_complain_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();
        complainrecycler = view.findViewById(R.id.complaints_recycler_view);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading please wait...");

        complaintData();
    }

    private void complaintData() {

        progressDialog.show();

        complainrecycler.setLayoutManager(new LinearLayoutManager(mContext));
        complaintAdapter = new AdminComplaintAdapter(complaintModel, mContext);
        complainrecycler.setAdapter(complaintAdapter);
        complainrecycler.setHasFixedSize(true);

        final Call<List<ComplaintList>> call = mApiService.getAdminComplaint();
        call.enqueue(new Callback<List<ComplaintList>>() {
            @Override
            public void onResponse(@Nullable Call<List<ComplaintList>> call, @Nullable Response<List<ComplaintList>> response) {
                progressDialog.dismiss();
                complaintModel = response.body();
                Log.d("TAG", "Response = " + complaintModel);
                complaintAdapter.setComplaintLists(complaintModel);

            }

            @Override
            public void onFailure(@Nullable Call<List<ComplaintList>> call, @Nullable Throwable t) {
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
