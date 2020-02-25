package com.fyp.emart.project.fragment.signup_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.LoginActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MartSignupFragment extends Fragment implements View.OnClickListener {
//https://github.com/shivpujan12/LocationPicker


    private static final int ADDRESS_PICKER_REQUEST = 1020;
    private TextInputEditText mTitName;
    private TextInputEditText mTitEmail;
    private TextInputEditText mTitPassword;
    private TextInputEditText mTitPhone;
    private TextInputEditText mTitAddress;
    private TextInputEditText mTitOwnerName;
    private TextInputEditText mTitOwnerPhone;
    private TextInputEditText mTitOrderDetail;
    private TextInputEditText mTitVoucher;
    private ImageButton mImgbtnlocation;
    private Button mBtnSignUp;
    private Button mBtnLogin;

    String name, email, password, phone, address, currentLatitude, currentLongitude, ownername, ownerphone, ownerdetail, voucher;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mart_signup, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapUtility.apiKey = getResources().getString(R.string.api_key);

        mTitName = (TextInputEditText) view.findViewById(R.id.titName);
        mTitEmail = (TextInputEditText) view.findViewById(R.id.titEmail);
        mTitPassword = (TextInputEditText) view.findViewById(R.id.titPassword);
        mTitPhone = (TextInputEditText) view.findViewById(R.id.titPhone);
        mTitAddress = (TextInputEditText) view.findViewById(R.id.titAddress);
        mTitOwnerName = (TextInputEditText) view.findViewById(R.id.titOwnerName);
        mTitOwnerPhone = (TextInputEditText) view.findViewById(R.id.titOwnerPhone);
        mTitOrderDetail = (TextInputEditText) view.findViewById(R.id.titOrderDetail);
        mTitVoucher = (TextInputEditText) view.findViewById(R.id.titVoucher);
        mBtnSignUp = (Button) view.findViewById(R.id.btnSignUp);
        mBtnLogin = (Button) view.findViewById(R.id.btnLogin);
        mImgbtnlocation = (ImageButton) view.findViewById(R.id.imgbtnlocation);
        mImgbtnlocation.setOnClickListener(this);
        mTitAddress.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);

    }

    public void locationPicker() {
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
                    mTitAddress.setText(address);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtnlocation:
                locationPicker();
                break;
            case R.id.btnSignUp:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.btnLogin:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
