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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.chootdev.recycleclick.RecycleClick;
import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.LoginActivity;
import com.fyp.emart.project.adapters.AdminReviewsAdapter;
import com.fyp.emart.project.model.ReviewList;
import com.fyp.emart.project.utils.SaveSharedPreference;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_NAME;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_iD;
import static com.fyp.emart.project.Api.DataConfig.MART_NAME;
import static com.fyp.emart.project.Api.DataConfig.MART_iD;

public class CustomerReviewListFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private AdminReviewsAdapter reviewsAdapter;
    private ProgressDialog progressDialog;

    private List<ReviewList> reviewsModel;

    private Context mContext;
    private BaseApiService mApiService;

    private ImageView mLogout;
    private TextInputEditText mTxtcomplaints;
    private Button mBtnAdd;
    ProgressDialog loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_review_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mLogout = (ImageView) view.findViewById(R.id.logout);
        mLogout.setOnClickListener(this);


        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();
        recyclerView = view.findViewById(R.id.reviews_recycler_view);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading please wait...");

        SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        String custid = sp.getString(CUSTOMER_iD, null);
        reviewsData(custid);

        RecycleClick.addTo(recyclerView).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Toast.makeText(getApplicationContext(), "id: "+productCategoryListPojos.get(position).getId(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(mContext, position, Toast.LENGTH_SHORT).show();
                String orderId = reviewsModel.get(position).getId();
                String customerName = reviewsModel.get(position).getCustname();
                String customerId = reviewsModel.get(position).getCustid();
                String datetime = reviewsModel.get(position).getDatetime();
                String martId = reviewsModel.get(position).getMartid();
                String comment = reviewsModel.get(position).getComment();
                String martName = reviewsModel.get(position).getMartname();
//                startActivity(new Intent(mContext, ProductActivity.class));

                askComplaintOrReview();

            }
        });

    }

    private void reviewsData(String custid) {

        progressDialog.show();

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        reviewsAdapter = new AdminReviewsAdapter(reviewsModel, mContext);
        recyclerView.setAdapter(reviewsAdapter);
        recyclerView.setHasFixedSize(true);

        final Call<List<ReviewList>> call = mApiService.getCustomerReviews(custid);
        call.enqueue(new Callback<List<ReviewList>>() {
            @Override
            public void onResponse(@Nullable Call<List<ReviewList>> call, @Nullable Response<List<ReviewList>> response) {
                progressDialog.dismiss();
                assert response != null;
                reviewsModel = response.body();
                Log.d("TAG", "Response = " + reviewsModel);
                reviewsAdapter.setReviewLists(reviewsModel);

            }

            @Override
            public void onFailure(@Nullable Call<List<ReviewList>> call, @Nullable Throwable t) {
                progressDialog.dismiss();
                assert t != null;
                Log.e("Error", t.getMessage());
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void logout() {
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

    public void askComplaintOrReview() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Want to add complaints or reviews?");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Add Complaints",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        complaintAlert();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton("Add Reviews",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        reviewAlert();
                        dialog.cancel();
                    }
                });

        builder1.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public void complaintAlert() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.complain_layout, null);
        mTxtcomplaints = (TextInputEditText) dialogView.findViewById(R.id.txtcomplaints);
        mBtnAdd = (Button) dialogView.findViewById(R.id.btnAdd);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final String currentDateandTime = sdf.format(new Date());

        SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        final String martid = sp.getString(MART_iD, null);
        final String martname = sp.getString(MART_NAME, null);
        final String custid = sp.getString(CUSTOMER_iD, null);
        final String custname = sp.getString(CUSTOMER_NAME, null);

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTxtcomplaints.getText().length() < 1) {
                    mTxtcomplaints.requestFocus();
                    mTxtcomplaints.setError("Complaint required.");
                } else {

                    String detail = mTxtcomplaints.getText().toString();
                    //Toast.makeText(mContext, token, Toast.LENGTH_SHORT).show();

                    loading = ProgressDialog.show(mContext, null, "Please wait...", true, false);
                    addComplaints(detail, currentDateandTime, custid, custname, martid, martname);

                }
            }
        });

    }

    public void reviewAlert() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.review_layout, null);
        mTxtcomplaints = (TextInputEditText) dialogView.findViewById(R.id.txtcomplaints);
        mBtnAdd = (Button) dialogView.findViewById(R.id.btnAdd);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final String currentDateandTime = sdf.format(new Date());

        SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        final String martid = sp.getString(MART_iD, null);
        final String martname = sp.getString(MART_NAME, null);
        final String custid = sp.getString(CUSTOMER_iD, null);
        final String custname = sp.getString(CUSTOMER_NAME, null);

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTxtcomplaints.getText().length() < 1) {
                    mTxtcomplaints.requestFocus();
                    mTxtcomplaints.setError("Review required.");
                } else {

                    String comment = mTxtcomplaints.getText().toString();
                    //Toast.makeText(mContext, token, Toast.LENGTH_SHORT).show();
                    loading = ProgressDialog.show(mContext, null, "Please wait...", true, false);
                    addReviews(comment, currentDateandTime, custid, custname, martid, martname);

                }

            }
        });

    }

    private void addComplaints(String detail, String datetime, String custid, String custname, String martid, String martname) {

        mApiService.AddComplaints(detail, datetime, custid, custname, martid, martname)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                if (response.body() != null) {

                                    String role = response.body().string();
                                    //Toast.makeText(mContext, role, Toast.LENGTH_SHORT).show();
                                    Log.d("debug", role);

                                    Toast.makeText(mContext, "Your Complaint Submit Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If the login fails
                                    // error case
                                    switch (response.code()) {
                                        case 404:
                                            Toast.makeText(getActivity(), "Server not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 500:
                                            Toast.makeText(getActivity(), "Server request not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("checkerror", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(getActivity(), "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

    private void addReviews(String comment, String datetime, String custid, String custname, String martid, String martname) {

        mApiService.AddReview(comment, datetime, custid, custname, martid, martname)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                if (response.body() != null) {

                                    String role = response.body().string();
                                    //Toast.makeText(mContext, role, Toast.LENGTH_SHORT).show();
                                    Log.d("debug", role);

                                    Toast.makeText(mContext, "Your Complaint Submit Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If the login fails
                                    // error case
                                    switch (response.code()) {
                                        case 404:
                                            Toast.makeText(getActivity(), "Server not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 500:
                                            Toast.makeText(getActivity(), "Server request not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("checkerror", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(getActivity(), "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }
}

