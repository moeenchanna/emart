package com.fyp.emart.project.fragment.checkout_fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.R;
import com.fyp.emart.project.utils.LocalStorage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static android.content.Context.MODE_PRIVATE;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_ADDRESS;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_EMAIL;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_NAME;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_PHONE;


public class AddressFragment extends Fragment implements View.OnClickListener {


    private EditText mNameSa;
    private EditText mEmailSa;
    private EditText mMobileSa;
    private EditText mAddressSa;
    private Button mBtnpyment;
    Context context;

    String _name, _email, _mobile, _address, userString;

    LocalStorage localStorage;
    Gson gson;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_address, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNameSa = (EditText) view.findViewById(R.id.sa_name);
        mEmailSa = (EditText) view.findViewById(R.id.sa_email);
        mMobileSa = (EditText) view.findViewById(R.id.sa_mobile);
        mAddressSa = (EditText) view.findViewById(R.id.sa_address);
        mBtnpyment = (Button) view.findViewById(R.id.btnpyment);
        mBtnpyment.setOnClickListener(this);

        localStorage = new LocalStorage(getContext());
        gson = new Gson();

        SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);

        String name = sp.getString(CUSTOMER_NAME, null);
        String email = sp.getString(CUSTOMER_EMAIL, null);
        String phone = sp.getString(CUSTOMER_PHONE, null);
        String address = sp.getString(CUSTOMER_ADDRESS, null);

        mNameSa.setText(name);
        mEmailSa.setText(email);
        mMobileSa.setText(phone);
        mAddressSa.setText(address);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnpyment:
                _name = mNameSa.getText().toString();
                _email = mEmailSa.getText().toString();
                _mobile = mMobileSa.getText().toString();
                _address = mAddressSa.getText().toString();

                Pattern p = Pattern.compile(DataConfig.regEx);

                Matcher m = p.matcher(_email);

                if (_name.length() == 0) {
                    mNameSa.setError("Enter Name");
                    mNameSa.requestFocus();
                } else if (_email.length() == 0) {
                    mEmailSa.setError("Enter email");
                    mEmailSa.requestFocus();
                } else if (!m.find()) {
                    mEmailSa.setError("Enter Correct email");
                    mEmailSa.requestFocus();

                } else if (_mobile.length() == 0) {
                    mMobileSa.setError("Enter mobile Number");
                    mMobileSa.requestFocus();
                } else if (_mobile.length() < 10) {
                    mMobileSa.setError("Enter Corretct mobile Number");
                    mMobileSa.requestFocus();
                } else if (_address.length() == 0) {
                    mAddressSa.setError("Enter your Address");
                    mAddressSa.requestFocus();
                }else {

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
                    ft.replace(R.id.content_frame, new PaymentFragment());
                    ft.commit();
                }
                break;
            default:
                break;
        }
    }
}
