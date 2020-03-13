package com.fyp.emart.project.fragment.checkout_fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_EMAIL;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_iD;
import static com.fyp.emart.project.Api.DataConfig.TEMP_MART_iD;


public class ConfirmFragment extends Fragment {
    LocalStorage localStorage;
    List<Cart> cartList = new ArrayList<>();
    Gson gson;
    RecyclerView recyclerView;
    CheckoutCartAdapter adapter;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    TextView back, placeOrder;
    TextView total, shipping, totalAmount;
    Double _total, _shipping, _totalAmount;
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
        shipping = view.findViewById(R.id.shipping_amount);
        back = view.findViewById(R.id.back);
        placeOrder = view.findViewById(R.id.place_order);
        progressDialog = new ProgressDialog(getContext());
        gson = new Gson();
        orderList = ((BaseActivity) getActivity()).getOrderList();
        Random rnd = new Random();
        orderNo = "Order #" + (100000 + rnd.nextInt(900000));
        setUpCartRecyclerview();
        if (orderList.isEmpty()) {
            id = "1";
        } else {
            id = String.valueOf(orderList.size() + 1);
        }


        _total = ((BaseActivity) getActivity()).getTotalPrice();
        _shipping = 0.0;
        _totalAmount = _total + _shipping;
        total.setText(_total + "");
        shipping.setText(_shipping + "");
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
                String currentDateandTime = sdf.format(new Date());
                Order order = new Order(id, orderNo, currentDateandTime, "Rs. " + _totalAmount,"Pending" );
                orderList.add(order);
                String orderString = gson.toJson(orderList);
                String status = "processing";
                String statusid = "1";
                SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
                String martid = sp.getString(TEMP_MART_iD, null);
                String custid = sp.getString(CUSTOMER_iD, null);
                String custemail = sp.getString(CUSTOMER_EMAIL, null);
                String subtotal = String.valueOf(_totalAmount);
                loading = ProgressDialog.show(getActivity(), null, "Please wait...", true, false);
                punchOrder(orderNo, orderString, currentDateandTime, status, statusid,subtotal , custemail, custid, martid);

            }
        });


        return view;
    }



    private void showCustomDialog() {

        // Create custom dialog object
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        // Include dialog.xml file
        dialog.setContentView(R.layout.success_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                startActivity(new Intent(getContext(), CustomerDashboardActivity.class));
                getActivity().finish();
            }
        });
        // Set dialog title

        dialog.show();
    }

    private void setUpCartRecyclerview() {

        cartList = new ArrayList<>();
        cartList = ((BaseActivity) getContext()).getCartList();


        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        adapter = new CheckoutCartAdapter(cartList, getContext());
        recyclerView.setAdapter(adapter);
    }

    private void punchOrder(String orderno, final String orderdetail, String curdatetime, String status, String statusid, String subtotal, String custemail, String custid, String martid) {
        mApiService.OrderPunch(orderno, orderdetail, curdatetime, status, statusid, subtotal, custemail, custid, martid)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                if (response.body() != null) {

                                    String role = response.body().string();
                                    localStorage.setOrder(orderdetail);
                                    localStorage.deleteCart();
                                    showCustomDialog();
                                    Toast.makeText(mContext, role + " Order punch successfull", Toast.LENGTH_SHORT).show();
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


}
