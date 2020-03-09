package com.fyp.emart.project;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fyp.emart.project.model.Cart;
import com.fyp.emart.project.utils.AddorRemoveCallbacks;
import com.fyp.emart.project.utils.LocalStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment implements AddorRemoveCallbacks {
    public static final String TAG = "BaseActivity===>";

    public Gson gson;
    public LocalStorage localStorage;
    String userJson;
    ProgressDialog progressDialog;
    List<Cart> cartList = new ArrayList<Cart>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        localStorage = new LocalStorage(getActivity());
        gson = new Gson();
        userJson = localStorage.getUserLogin();
        progressDialog = new ProgressDialog(getActivity());
        //user = gson.fromJson(userJson, UserResult.class);
        //  NetworkCheck.isNetworkAvailable(getApplicationContext());
        cartCount();
    }

    @Override
    public void onAddProduct() {

    }

    @Override
    public void onRemoveProduct() {

    }

    @Override
    public void updateTotalPrice() {

    }

    public int cartCount() {

        gson = new Gson();
        if (localStorage.getCart() != null) {
            String jsonCart = localStorage.getCart();
            Log.d("CART : ", jsonCart);
            Type type = new TypeToken<List<Cart>>() {
            }.getType();
            cartList = gson.fromJson(jsonCart, type);


            //Toast.makeText(getContext(),remedyList.size()+"",Toast.LENGTH_LONG).show();
            return cartList.size();
        }
        return 0;
    }


}
