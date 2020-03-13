package com.fyp.emart.project.fragment.admin_fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.AdminComplaintAdapter;
import com.fyp.emart.project.model.ComplaintList;

import java.util.List;

public class AdminComplainFragment extends Fragment {

    private RecyclerView mRecyclerViewMart;
    private AdminComplaintAdapter orderAdapter;
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

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();
        mRecyclerViewMart = view.findViewById(R.id.complaints_recycler_view);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading please wait...");

        complaintData();
    }

    private void complaintData() {
    }
}
