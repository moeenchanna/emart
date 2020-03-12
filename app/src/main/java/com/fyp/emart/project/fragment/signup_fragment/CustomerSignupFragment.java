package com.fyp.emart.project.fragment.signup_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.accounts.AccountManager.KEY_PASSWORD;


public class CustomerSignupFragment extends Fragment implements View.OnClickListener {

    private static final int ADDRESS_PICKER_REQUEST = 1020;
    private TextInputEditText mTitName;
    private TextInputEditText mTitEmail;
    private TextInputEditText mTitPassword;
    private TextInputEditText mTitConfirmPassword;
    private TextInputEditText mTitPhone;
    private TextInputEditText mTitAddress;
    private ImageButton mImgbtnlocation;
    private Button mBtnSignUp;
    private Button mBtnLogin;


    String name,email,password,cpassword,phone,address,currentLatitude,currentLongitude,token;

    Context mContext;
    BaseApiService mApiService;
    ProgressDialog loading;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_signup, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getActivity();
        mApiService = UtilsApi.getAPIService(); // heat the contents of the package api helper

        mTitName = (TextInputEditText) view.findViewById(R.id.titName);
        mTitEmail = (TextInputEditText) view.findViewById(R.id.titEmail);
        mTitPassword = (TextInputEditText) view.findViewById(R.id.titPassword);
        mTitConfirmPassword = (TextInputEditText) view.findViewById(R.id.RetypePassword);
        mTitPhone = (TextInputEditText) view.findViewById(R.id.titPhone);
        mTitAddress = (TextInputEditText) view.findViewById(R.id.titAddress);
        mBtnSignUp = (Button) view.findViewById(R.id.btnSignUp);
        mBtnLogin = (Button) view.findViewById(R.id.btnLogin);
        mImgbtnlocation = (ImageButton) view.findViewById(R.id.imgbtnlocation);
        mImgbtnlocation.setOnClickListener(this);
        mTitAddress.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtnlocation:
                locationPicker();
                break;
            case R.id.btnSignUp:
                checkValidations();

                break;
            case R.id.btnLogin:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    private void checkValidations() {

        if (mTitName.getText().length() < 1) {
            mTitName.requestFocus();
            mTitName.setError("Name required.");
        }

        else if (!isEmailValid(mTitEmail.getText().toString())) {
            mTitEmail.requestFocus();
            mTitEmail.setError("Valid email required.");
        }

        else if (mTitPassword.getText().length() < 1) {
            mTitPassword.requestFocus();
            mTitPassword.setError("Password required.");
        }

        else if (!mTitConfirmPassword.getText().toString().equals(mTitPassword.getText().toString())) {
            mTitConfirmPassword.requestFocus();
            mTitConfirmPassword.setError("Password not match");
        }

        else if (mTitPhone.getText().length() < 1) {
            mTitPhone.requestFocus();
            mTitPhone.setError("Phone required.");
        }

        else if (mTitAddress.getText().length() < 1) {
            mTitAddress.requestFocus();
            mTitAddress.setError("Address required.");
        }
        else {

            name = mTitName.getText().toString();
            email = mTitEmail.getText().toString();
            password = mTitPassword.getText().toString();
            phone = mTitPhone.getText().toString();
            address = mTitAddress.getText().toString();

            // Get new Instance ID token
            token = FirebaseInstanceId.getInstance().getToken();
            //Toast.makeText(mContext, token, Toast.LENGTH_SHORT).show();
            loading = ProgressDialog.show(mContext, null, "Please wait...", true, false);
            RegisterRequest(name,email,password,phone,address,token);
            customerLoginDetails(email,password);
        }


    }
    private void customerLoginDetails(String email, String password) {
        mApiService.registerUser(email,password,"1")//1 for Customer
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                if (response.body() != null) {

                                    String role = response.body().string();
                                    Toast.makeText(mContext, role +"Customer Login Created", Toast.LENGTH_SHORT).show();
                                    Log.d("debug", role +"Customer Login Created");
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

    private void RegisterRequest(final String name, String email, String password, String phone, final String address,String token) {

        mApiService.registerCustomer(name,email,password,phone,address,token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                if (response.body() != null) {

                                    String role = response.body().string();
                                    Toast.makeText(mContext, role, Toast.LENGTH_SHORT).show();
                                    Log.d("debug", role);

                                    mTitName.setText("");
                                    mTitEmail.setText("");
                                    mTitPassword.setText("");
                                    mTitConfirmPassword.setText("");
                                    mTitPhone.setText("");
                                    mTitAddress.setText("");
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
                    mTitAddress.setText(address);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean isEmailValid(String email) {
        //https://stackoverflow.com/questions/9355899/android-email-edittext-validation
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

}
