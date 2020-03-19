package com.fyp.emart.project.fragment.mart_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.LoginActivity;
import com.fyp.emart.project.model.MartProfileList;

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
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_EMAIL;
import static com.fyp.emart.project.Api.DataConfig.MART_ADDRESS;
import static com.fyp.emart.project.Api.DataConfig.MART_EMAIL;
import static com.fyp.emart.project.Api.DataConfig.MART_NAME;
import static com.fyp.emart.project.Api.DataConfig.MART_PHONE;
import static com.fyp.emart.project.Api.DataConfig.MART_iD;


public class MartProfileFragment extends Fragment {


    private EditText mTvname;
    private EditText mTvemail;
    private EditText mTvmobile;
    private EditText mTvaddress;
private Button medit, msubit;
    private ProgressDialog loading;
    private Context mContext;
    private BaseApiService mApiService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mart_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mTvname = (EditText) view.findViewById(R.id.tvname);
        mTvemail = (EditText) view.findViewById(R.id.tvemail);
        mTvmobile = (EditText) view.findViewById(R.id.tvmobile);
        mTvaddress = (EditText) view.findViewById(R.id.tvaddress);
        medit = (Button) view.findViewById(R.id.btnedit);
        msubit = (Button) view.findViewById(R.id.btnsubmt);


        medit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvname.setFocusable(true);
                mTvname.setClickable(true);

            }
        });





        SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        String email = sp.getString(MART_EMAIL, null);
        loading = ProgressDialog.show(mContext, null, "Loading", true, false);
        requestMartData(email);
    }

    private void requestMartData(String email) {
        mApiService.getMartProfile(email)
                .enqueue(new  Callback<List<MartProfileList>>() {
                    @Override
                    public void onResponse(Call<List<MartProfileList>> call, Response<List<MartProfileList>> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();

                            List<MartProfileList> adslist = response.body();

                            String name = adslist.get(0).getName();
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
                    public void onFailure(Call<List<MartProfileList>> call, Throwable t) {
                        Log.e("checkerror", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(getActivity(), "network failure :( inform the user and possibly retry\n"+t.toString(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }
}
