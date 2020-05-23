package com.fyp.emart.project.fragment.checkout_fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.BaseActivity;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.CartActivity;
import com.fyp.emart.project.activity.CustomerDashboardActivity;
import com.fyp.emart.project.adapters.CheckoutCartAdapter;
import com.fyp.emart.project.model.Cart;
import com.fyp.emart.project.model.Order;
import com.fyp.emart.project.utils.LocalStorage;
import com.google.gson.Gson;
import com.msoftworks.easynotify.EasyNotify;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_EMAIL;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_FCM_KEY;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_iD;
import static com.fyp.emart.project.Api.DataConfig.DISCOUNT_AMOUNT;
import static com.fyp.emart.project.Api.DataConfig.TEMP_MART_iD;


public class ConfirmFragment extends Fragment {
    LocalStorage localStorage;
    List<Cart> cartList = new ArrayList<>();
    Gson gson;
    RecyclerView recyclerView;
    CheckoutCartAdapter adapter;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    TextView back, placeOrder;
    TextView total, discountamount, totalAmount;
    Double _total, _discountamount, _totalAmount;
    ProgressDialog progressDialog;
    List<Order> orderList = new ArrayList<>();

    String orderNo;
    String id;

    Context mContext;
    BaseApiService mApiService;
    ProgressDialog loading;
    public ConfirmFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm, container, false);
        mContext = getActivity();
        mApiService = UtilsApi.getAPIService(); // heat the contents of the package api helper
        localStorage = new LocalStorage(getContext());
        recyclerView = view.findViewById(R.id.cart_rv);
        totalAmount = view.findViewById(R.id.total_amount);
        total = view.findViewById(R.id.total);
        discountamount = view.findViewById(R.id.shipping_amount);
        back = view.findViewById(R.id.back);
        placeOrder = view.findViewById(R.id.place_order);
        progressDialog = new ProgressDialog(getContext());
        gson = new Gson();
        orderList = ((BaseActivity) getActivity()).getOrderList();
        Random rnd = new Random();
        orderNo = "" + (100000 + rnd.nextInt(900000));

        cartList = new ArrayList<>();
        cartList = ((BaseActivity) getContext()).getCartList();

        setUpCartRecyclerview();

        if (orderList.isEmpty()) {
            id = "1";
        } else {
            id = String.valueOf(orderList.size() + 1);
        }


        _total = ((BaseActivity) getActivity()).getTotalPrice();
        SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        double _discountamount = Double.parseDouble(sp.getString(DISCOUNT_AMOUNT, null));

        //_discountamount = 0.0;

        _totalAmount = _total - _discountamount;
        total.setText(_total + "");
        discountamount.setText(_discountamount + "");
        totalAmount.setText(_totalAmount + "");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CartActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                final String currentDateandTime = sdf.format(new Date());
                Order order = new Order(id, orderNo, currentDateandTime, "Rs. " + _totalAmount,"Pending" );
                orderList.add(order);


                final String orderString = gson.toJson(orderList);

                SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
                final String martid = sp.getString(TEMP_MART_iD, null);
                final String custid = sp.getString(CUSTOMER_iD, null);
                final String custemail = sp.getString(CUSTOMER_EMAIL, null);
                final String fcm = sp.getString(CUSTOMER_FCM_KEY, null);
                final String subtotal = String.valueOf(_totalAmount);


                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Select Your Option.");
                builder1.setCancelable(false);
                builder1.setPositiveButton(
                        "Home Delivery.",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String status = "Pending";
                                String statusid = "1";
                                loading = ProgressDialog.show(getActivity(), null, "Please wait...", true, false);
                                punchOrder(orderNo, localStorage.getCart(), currentDateandTime, status, statusid,subtotal , custemail, custid, martid,orderString,fcm);

                            }
                        });

                builder1.setNegativeButton(
                        "Take Away.",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String status = "Picked";
                                String statusid = "5";

                                loading = ProgressDialog.show(getActivity(), null, "Please wait...", true, false);
                                punchOrder(orderNo, localStorage.getCart(), currentDateandTime, status, statusid,subtotal , custemail, custid, martid,orderString,fcm);

                                TakeAwayNotfication(fcm,orderNo);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();





            }
        });


        return view;
    }



    private void showCustomDialog(String orderdata) {

        localStorage.setOrder(orderdata);//Delete cart


        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Order Submitted Successfully.");
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Back to home.",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        localStorage.deleteCart();
                        startActivity(new Intent(getContext(), CustomerDashboardActivity.class));
                        getActivity().finish();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();


    }

    private void setUpCartRecyclerview() {


        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        adapter = new CheckoutCartAdapter(cartList, getContext());
        recyclerView.setAdapter(adapter);


    }

    private void punchOrder(final String orderno, String orderdetail, String curdatetime, String status, String statusid, String subtotal, String custemail, String custid, String martid, final String orderdata,String fcm) {
        mApiService.OrderPunch(orderno, orderdetail, curdatetime, status, statusid, subtotal, custemail, custid, martid,fcm)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                           // loading.dismiss();
                            try {
                                if (response.body() != null) {

                                    String role = response.body().string();
                                    for (int i = 0; i < cartList.size(); i++)
                                    {
                                        punchOrderDetails(orderno,cartList.get(i).getId(),cartList.get(i).getQuantity(),cartList.get(i).getTitle(),cartList.get(i).getImage(),cartList.get(i).getPrice());
                                       // Toast.makeText(mContext, cartList.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                                       // Toast.makeText(mContext, "Order punching please wait", Toast.LENGTH_SHORT).show();
                                    }
                                  //  loading.dismiss();

                                    showCustomDialog(orderdata);
                                   // Toast.makeText(mContext, role + " Order punch successfull", Toast.LENGTH_SHORT).show();
                                    Log.d("debug", role + "Order punch successfull");
                                } else {
                                    // If the login fails
                                    // error case
                                    switch (response.code()) {
                                        case 404:
                                            Toast.makeText(mContext, "Server not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 500:
                                            Toast.makeText(mContext, "Server request not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(mContext, "unknown error", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(mContext, "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });

    }

    private void punchOrderDetails(String orderno,String producdid, String qty, String name,String url, String cost) {
        mApiService.OrderDetailPunch(orderno, producdid, qty, name, url, cost)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                           //  loading.dismiss();
                            try {
                                if (response.body() != null) {

                                    String role = response.body().string();
                                    Toast.makeText(mContext, "Order Punching Please Wait...", Toast.LENGTH_SHORT).show();
                                    Log.d("debug", role + "Order punch successfull");
                                } else {
                                    // If the login fails
                                    // error case
                                    switch (response.code()) {
                                        case 404:
                                            Toast.makeText(mContext, "Server not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 500:
                                            Toast.makeText(mContext, "Server request not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(mContext, "unknown error", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(mContext, "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Confirm");
    }


    private void TakeAwayNotfication(String fcm,String orderNo) {

        EasyNotify easyNotify = new EasyNotify(UtilsApi.SERVER_APP_API_KEY);
        easyNotify.setSendBy(EasyNotify.TOKEN);
        easyNotify.setToken(fcm);
        easyNotify.setTitle("Order Alert.");
        easyNotify.setBody("Dear Customer you order "+ orderNo +"will be ready in 20 minutes.");
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
