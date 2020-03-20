package com.fyp.emart.project.fragment.customer_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.LoginActivity;
import com.fyp.emart.project.adapters.AdminComplaintAdapter;
import com.fyp.emart.project.model.ComplaintList;
import com.fyp.emart.project.utils.SaveSharedPreference;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_EMAIL;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_iD;


public class CustomerComplaintListFragment extends Fragment  implements View.OnClickListener{

    private RecyclerView complainrecycler;
    private AdminComplaintAdapter complaintAdapter;
    private ProgressDialog progressDialog;

    private List<ComplaintList> complaintModel;

    private Context mContext;
    private BaseApiService mApiService;
    private ImageView mLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_complaint_list, container, false);
    }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            mLogout = (ImageView) view.findViewById(R.id.logout);
            mLogout.setOnClickListener(this);


            mApiService = UtilsApi.getAPIService();
            mContext = getActivity();
            complainrecycler = view.findViewById(R.id.complaints_recycler_view);

            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage("Loading please wait...");

            SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
            String custid = sp.getString(CUSTOMER_iD, null);
            complaintData(custid);
        }

        private void complaintData(String custid) {

            progressDialog.show();

            complainrecycler.setLayoutManager(new LinearLayoutManager(mContext));
            complaintAdapter = new AdminComplaintAdapter(complaintModel, mContext);
            complainrecycler.setAdapter(complaintAdapter);
            complainrecycler.setHasFixedSize(true);

            final Call<List<ComplaintList>> call = mApiService.getCustomerComplaint(custid);
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
