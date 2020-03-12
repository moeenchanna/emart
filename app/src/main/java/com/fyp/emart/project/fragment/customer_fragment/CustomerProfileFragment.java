package com.fyp.emart.project.fragment.customer_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.BaseFragment;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.LoginActivity;
import com.fyp.emart.project.model.CustomerProfileList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_ADDRESS;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_EMAIL;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_NAME;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_PHONE;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_iD;
import static com.fyp.emart.project.Api.DataConfig.MART_iD;

public class CustomerProfileFragment extends BaseFragment {
    private static int cart_count = 0;
    private TextView mTvname;
    private TextView mTvemail;
    private TextView mTvmobile;
    private TextView mTvaddress;


    private ProgressDialog loading;
    private Context mContext;
    private BaseApiService mApiService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        cart_count = cartCount();

        mTvname = (TextView) view.findViewById(R.id.tvname);
        mTvemail = (TextView) view.findViewById(R.id.tvemail);
        mTvmobile = (TextView) view.findViewById(R.id.tvmobile);
        mTvaddress = (TextView) view.findViewById(R.id.tvaddress);


        SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        String email = sp.getString(CUSTOMER_EMAIL, null);
        loading = ProgressDialog.show(mContext, null, "Loading", true, false);
        requestCustomerData(email);
    }

    private void requestCustomerData(String email) {
        mApiService.getCustomerProfile(email)
                .enqueue(new  Callback<List<CustomerProfileList>>() {
                    @Override
                    public void onResponse(Call<List<CustomerProfileList>> call, Response<List<CustomerProfileList>> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();

                            List<CustomerProfileList> adslist = response.body();

                            String name = adslist.get(0).getCname();
                            String phone = adslist.get(0).getPhone();
                            String email = adslist.get(0).getEmail();
                            String address = adslist.get(0).getAddress();
                            mTvname.setText(name);
                            mTvmobile.setText(phone);
                            mTvemail.setText(email);
                            mTvaddress.setText(address);


                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CustomerProfileList>> call, Throwable t) {
                        Log.e("checkerror", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(getActivity(), "network failure :( inform the user and possibly retry\n"+t.toString(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }
}
