package com.fyp.emart.project.fragment.mart_fragment;

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
import com.fyp.emart.project.activity.MartOderDetail;
import com.fyp.emart.project.adapters.AdminOrderAdapter;
import com.fyp.emart.project.model.OrderList;
import com.fyp.emart.project.utils.SaveSharedPreference;
import com.google.android.material.textfield.TextInputEditText;
import com.msoftworks.easynotify.EasyNotify;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_NAME;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_iD;
import static com.fyp.emart.project.Api.DataConfig.MART_NAME;
import static com.fyp.emart.project.Api.DataConfig.MART_iD;

public class MartOrderFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerViewMart;
    private AdminOrderAdapter adminOrderAdapter;
    private ProgressDialog progressDialog;

    List<OrderList> adminOrderListModel;

    private Context mContext;
    private BaseApiService mApiService;
    SharedPreferences loginPreferences;
    private ImageView mLogout;
    private TextInputEditText mTxtcomplaints;
    private Button mBtnAdd;
    ProgressDialog loading;
    AlertDialog dialogBuilder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mart_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();
        mRecyclerViewMart = view.findViewById(R.id.order_recycler_view);
        mLogout = (ImageView) view.findViewById(R.id.logout);
        mLogout.setOnClickListener(this);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading please wait...");


        SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        final String martid = sp.getString(MART_iD, null);
        orderData(martid);



        RecycleClick.addTo(mRecyclerViewMart).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                //Toast.makeText(getApplicationContext(), "id: "+productCategoryListPojos.get(position).getId(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(mContext, position, Toast.LENGTH_SHORT).show();
                String orderId = adminOrderListModel.get(position).getId();
                final String customerEmail = adminOrderListModel.get(position).getCustemail();
                final String customerId = adminOrderListModel.get(position).getCustid();
                String datetime = adminOrderListModel.get(position).getDatetime();
                String martId = adminOrderListModel.get(position).getMartid();
                String statusId = adminOrderListModel.get(position).getStatusid();
                String status = adminOrderListModel.get(position).getStatus();
                String orderDetail = adminOrderListModel.get(position).getOrderdetail();
                final String orderNo = adminOrderListModel.get(position).getOrderno();
                final String subtotal = adminOrderListModel.get(position).getSubtotal();
                final String fcm = adminOrderListModel.get(position).getFcm();
//                startActivity(new Intent(mContext, ProductActivity.class));
              //

                final AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Select Your Option.");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Complaint Or Review",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                askComplaintOrReview(customerId,customerEmail,fcm);
                            }
                        });

                builder1.setNegativeButton(
                        "Update Order Status",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                              updateStatus(orderNo,fcm);
                            }
                        });

                builder1.setNeutralButton(
                        "Order Detail",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Bundle b = new Bundle();
                                Intent i = new Intent(getActivity(), MartOderDetail.class);
                                b.putString("orderno",orderNo);
                                i.putExtras(b);
                                startActivity(i);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
    }

    public void updateStatus(final String orderno , final String fcm)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Select Your Option.");
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Delivered",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String status = "Delivered";
                        String statusid = "4";
                        loading = ProgressDialog.show(getActivity(), null, "Please wait...", true, false);
                        updateStatusService(orderno, status, statusid,fcm);

                    }
                });

        builder1.setNegativeButton(
                "Accept",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String status = "Accept";
                        String statusid = "1";
                        loading = ProgressDialog.show(getActivity(), null, "Please wait...", true, false);
                        updateStatusService(orderno, status, statusid,fcm);

                    }
                });

        builder1.setNeutralButton(
                "Decline",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String status = "Decline";
                        String statusid = "2";
                        loading = ProgressDialog.show(getActivity(), null, "Please wait...", true, false);
                        updateStatusService(orderno, status, statusid,fcm);

                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void orderData(String id) {
        progressDialog.show();

        mRecyclerViewMart.setLayoutManager(new LinearLayoutManager(mContext));
        adminOrderAdapter = new AdminOrderAdapter(adminOrderListModel, mContext);
        mRecyclerViewMart.setAdapter(adminOrderAdapter);

        final Call<List<OrderList>> adminOrder = mApiService.getMartOrders(id);
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
              //  Toast.makeText(mContext, "Orders not available", Toast.LENGTH_SHORT).show();
               // Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void askComplaintOrReview(final String custid, final String custeremail, final String fcm) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Want to add complaints or reviews?");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Add Complaints",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        complaintAlert(custid,custeremail,fcm);
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton("Add Reviews",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        reviewAlert(custid,custeremail,fcm);
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


    public void complaintAlert(final String custid, final String custname, final String fcm) {
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
                    addComplaints(detail, currentDateandTime, custid, custname, martid, martname,fcm);

                }
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    public void reviewAlert(final String custid, final String custname , final String fcm) {
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
                    addReviews(comment, currentDateandTime, custid, custname, martid, martname,fcm);

                }

            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }


    private void addComplaints(String detail, String datetime, String custid, final String custname, String martid, final String martname , final String fcm) {

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
                                    dialogBuilder.dismiss();
                                    ComplaintNotfication(fcm,custname,martname);
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

    private void addReviews(String comment, String datetime, String custid, final String custname, String martid, final String martname, final String fcm) {

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
                                    ReviewNotfication(fcm,custname,martname);
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

    private void updateStatusService(final String order, final String status, String statusid, final String fcm) {

        mApiService.UpdateStatus(order, status, statusid)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                if (response.body() != null) {
                                    loading.dismiss();
                                    String role = response.body().string();
                                    //Toast.makeText(mContext, role, Toast.LENGTH_SHORT).show();
                                    Log.d("debug", role);

                                    UpdateNotfication(fcm,order,status);

                                    Toast.makeText(mContext, "Status Updated Successfully", Toast.LENGTH_SHORT).show();
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

    private void UpdateNotfication(String fcm,String order,String status) {

        EasyNotify easyNotify = new EasyNotify(UtilsApi.SERVER_APP_API_KEY);
        easyNotify.setSendBy(EasyNotify.TOKEN);
        easyNotify.setToken(fcm);
        easyNotify.setTitle("Order Alert.");
        easyNotify.setBody("Dear customer your order no: "+order+" has been "+ status);
        easyNotify.setSound("default");
        easyNotify.nPush();
        easyNotify.setEasyNotifyListener(new EasyNotify.EasyNotifyListener() {
            @Override
            public void onNotifySuccess(String s) {
                //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, "Notification send", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNotifyError(String s) {
                //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void ComplaintNotfication(String fcm,String customer,String mart) {

        EasyNotify easyNotify = new EasyNotify(UtilsApi.SERVER_APP_API_KEY);
        easyNotify.setSendBy(EasyNotify.TOKEN);
        easyNotify.setToken(fcm);
        easyNotify.setTitle("Complaint Alert.");
        easyNotify.setBody("Dear "+customer+" you received a complaint from: "+mart);
        easyNotify.setSound("default");
        easyNotify.nPush();
        easyNotify.setEasyNotifyListener(new EasyNotify.EasyNotifyListener() {
            @Override
            public void onNotifySuccess(String s) {
                //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, "Notification send", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNotifyError(String s) {
                //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void ReviewNotfication(String fcm,String customer,String mart) {

        EasyNotify easyNotify = new EasyNotify(UtilsApi.SERVER_APP_API_KEY);
        easyNotify.setSendBy(EasyNotify.TOKEN);
        easyNotify.setToken(fcm);
        easyNotify.setTitle("Review Alert.");
        easyNotify.setBody("Dear "+customer+" you received a complaint from: "+mart);
        easyNotify.setSound("default");
        easyNotify.nPush();
        easyNotify.setEasyNotifyListener(new EasyNotify.EasyNotifyListener() {
            @Override
            public void onNotifySuccess(String s) {
                //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, "Notification send", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNotifyError(String s) {
                //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }

        });
    }

}



