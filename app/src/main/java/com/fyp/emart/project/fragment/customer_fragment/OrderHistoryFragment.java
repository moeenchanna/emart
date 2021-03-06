package com.fyp.emart.project.fragment.customer_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chootdev.recycleclick.RecycleClick;
import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.BaseFragment;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.LoginActivity;
import com.fyp.emart.project.activity.OrderHistoryActivity;
import com.fyp.emart.project.adapters.AdminOrderAdapter;
import com.fyp.emart.project.model.OrderList;
import com.fyp.emart.project.utils.SaveSharedPreference;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_EMAIL;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_NAME;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_iD;
import static com.fyp.emart.project.Api.DataConfig.MART_NAME;
import static com.fyp.emart.project.Api.DataConfig.MART_iD;
import static com.fyp.emart.project.Api.DataConfig.TEMP_MART_iD;

public class OrderHistoryFragment extends BaseFragment implements View.OnClickListener {

    private static int cart_count = 0;

    private RecyclerView mRecyclerViewMart;
    private AdminOrderAdapter adminOrderAdapter;
    private ProgressDialog progressDialog;

    List<OrderList> adminOrderListModel;

    private Context mContext;
    private BaseApiService mApiService;

    private ImageView mLogout;
    private TextInputEditText mTxtcomplaints;
    private Button mBtnAdd;
    ProgressDialog loading;
    AlertDialog dialogBuilder;
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

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mLogout = (ImageView) view.findViewById(R.id.logout);
        mLogout.setOnClickListener(this);


        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();
        mRecyclerViewMart = view.findViewById(R.id.order_recycler_view);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading please wait...");

        SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        String customerid = sp.getString(CUSTOMER_iD, null);
        orderData(customerid);

        RecycleClick.addTo(mRecyclerViewMart).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                String orderNo = adminOrderListModel.get(position).getOrderno();
                String statusId = adminOrderListModel.get(position).getStatusid();
                String status = adminOrderListModel.get(position).getStatus();


                Bundle b =new Bundle();
                Intent i = new Intent(getActivity(), OrderHistoryActivity.class);
                b.putString("orderno",orderNo);
                b.putString("statusid",statusId);
                b.putString("status",status);
                i.putExtras(b);
                startActivity(i);

                //Toast.makeText(getApplicationContext(), "id: "+productCategoryListPojos.get(position).getId(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(mContext, position, Toast.LENGTH_SHORT).show();
              /*  String orderId = adminOrderListModel.get(position).getId();
                String customerEmail = adminOrderListModel.get(position).getCustemail();
                String customerId = adminOrderListModel.get(position).getCustid();
                String datetime = adminOrderListModel.get(position).getDatetime();
                String martId = adminOrderListModel.get(position).getMartid();
                String statusId = adminOrderListModel.get(position).getStatusid();
                String status = adminOrderListModel.get(position).getStatus();
                String orderDetail = adminOrderListModel.get(position).getOrderdetail();
                String orderNo = adminOrderListModel.get(position).getOrderno();
                String subtotal = adminOrderListModel.get(position).getSubtotal();*/

               // askComplaintOrReview();

//                startActivity(new Intent(mContext, ProductActivity.class));

            }
        });

    }




    private void orderData(String id) {
        progressDialog.show();

        mRecyclerViewMart.setLayoutManager(new LinearLayoutManager(mContext));
        adminOrderAdapter = new AdminOrderAdapter(adminOrderListModel, mContext);
        mRecyclerViewMart.setAdapter(adminOrderAdapter);

        final Call<List<OrderList>> adminOrder = mApiService.getOrderHistory(id);
        adminOrder.enqueue(new Callback<List<OrderList>>() {
            @Override
            public void onResponse(Call<List<OrderList>> call, Response<List<OrderList>> response) {
                progressDialog.dismiss();
                adminOrderListModel = response.body();
                Log.d("TAG", "Response = " + adminOrderListModel);
                adminOrderAdapter.setOrderList(adminOrderListModel);
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

    public void logout() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to logout?");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                      //  PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().apply();
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
        dialogBuilder = new AlertDialog.Builder(getActivity()).create();
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

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    public void reviewAlert() {
         dialogBuilder = new AlertDialog.Builder(getActivity()).create();
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

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    private void addComplaints(String detail, String datetime, String custid, String custname, String martid, String martname) {

        mApiService.AddComplaints(detail, datetime, custid, custname, martid,martname)
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
                                    dialogBuilder.dismiss();
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
                                    dialogBuilder.dismiss();
                                    String role = response.body().string();
                                    //Toast.makeText(mContext, role, Toast.LENGTH_SHORT).show();
                                    Log.d("debug", role);

                                    Toast.makeText(mContext, "Your Review Submit Successfully", Toast.LENGTH_SHORT).show();
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
