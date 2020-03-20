package com.fyp.emart.project.fragment.mart_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.LoginActivity;
import com.fyp.emart.project.model.MartProfileList;
import com.fyp.emart.project.utils.SaveSharedPreference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.fyp.emart.project.Api.DataConfig.MART_EMAIL;


public class MartProfileFragment extends Fragment implements View.OnClickListener {

    private static final int ADDRESS_PICKER_REQUEST = 1020;
    private EditText mTvname;
    private EditText mTvemail;
    private EditText mTvmobile;
    private EditText mTvaddress;
    private ProgressDialog loading;
    private Context mContext;
    private BaseApiService mApiService;
    private ImageView mLogout;
    private Button mBtnedit;
    private Button mBtnsubmt;
    private ImageButton mImgbtnlocation;
    String address;
    String currentLatitude,currentLongitude;

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
        mLogout = (ImageView) view.findViewById(R.id.logout);


        mTvname = (EditText) view.findViewById(R.id.tvname);
        mTvemail = (EditText) view.findViewById(R.id.tvemail);
        mTvmobile = (EditText) view.findViewById(R.id.tvmobile);
        mTvaddress = (EditText) view.findViewById(R.id.tvaddress);
        mLogout.setOnClickListener(this);
        mBtnedit = (Button) view.findViewById(R.id.btnedit);
        mBtnedit.setOnClickListener(this);
        mBtnsubmt = (Button) view.findViewById(R.id.btnsubmt);
        mBtnsubmt.setOnClickListener(this);
        mImgbtnlocation = (ImageButton) view.findViewById(R.id.imgbtnlocation);
        mImgbtnlocation.setOnClickListener(this);

        mTvname.setEnabled(false);
        mTvemail.setEnabled(false);
        mTvaddress.setEnabled(false);
        mTvmobile.setEnabled(false);

        SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        String email = sp.getString(MART_EMAIL, null);
        loading = ProgressDialog.show(mContext, null, "Loading", true, false);
        requestMartData(email);
    }

    private void requestMartData(String email) {
        mApiService.getMartProfile(email)
                .enqueue(new Callback<List<MartProfileList>>() {
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
                        Toast.makeText(getActivity(), "network failure :( inform the user and possibly retry\n" + t.toString(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
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

    private void locationPicker() {
        Intent intent = new Intent(getActivity(), LocationPickerActivity.class);
        startActivityForResult(intent, ADDRESS_PICKER_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    address = data.getStringExtra(MapUtility.ADDRESS);
                    currentLatitude = String.valueOf(data.getDoubleExtra(MapUtility.LATITUDE, 0.0));
                    currentLongitude = String.valueOf(data.getDoubleExtra(MapUtility.LONGITUDE, 0.0));
                    mTvaddress.setText(address);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                logout();
                break;

            case R.id.btnedit:// TODO 20/03/20
                Toast.makeText(mContext, "Now you can edit.", Toast.LENGTH_SHORT).show();
                mImgbtnlocation.setVisibility(View.VISIBLE);
                mTvname.setEnabled(true);
                mTvname.requestFocus();
                mTvaddress.setEnabled(true);
                mTvmobile.setEnabled(true);

                break;
            case R.id.btnsubmt:// TODO 20/03/20
                checkValidations();

                break;
            case R.id.imgbtnlocation:// TODO 20/03/20
                locationPicker();
                break;
            default:
                break;
        }
    }

    private void checkValidations() {

        if (mTvname.getText().length() < 1) {
            mTvname.requestFocus();
            mTvname.setError("Name required.");
        }
        else if (mTvaddress.getText().length() < 1) {
            mTvaddress.requestFocus();
            mTvaddress.setError("Address required.");
        }

        else if (mTvmobile.getText().length() < 1) {
            mTvmobile.requestFocus();
            mTvmobile.setError("Phone required.");
        }  else {

            String name = mTvname.getText().toString();
            String email = mTvemail.getText().toString();
            String phone = mTvmobile.getText().toString();
            address = mTvaddress.getText().toString();
            //Toast.makeText(mContext, token, Toast.LENGTH_SHORT).show();
            loading = ProgressDialog.show(mContext, null, "Please wait...", true, false);
            updateProfile(name, email, phone, address);

        }
    }

    private void updateProfile(final String name, final String email,  String phone, final String address) {

        mApiService.updateMartProfile(name,email,phone,address,currentLatitude,currentLongitude)
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
                                    mImgbtnlocation.setVisibility(View.INVISIBLE);
                                    mTvname.setEnabled(false);
                                    mTvaddress.setEnabled(false);
                                    mTvmobile.setEnabled(false);
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
