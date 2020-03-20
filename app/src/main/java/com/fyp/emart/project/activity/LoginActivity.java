package com.fyp.emart.project.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.model.CustomerProfileList;
import com.fyp.emart.project.model.MartProfileList;
import com.fyp.emart.project.utils.SaveSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_ADDRESS;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_EMAIL;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_NAME;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_PHONE;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_iD;
import static com.fyp.emart.project.Api.DataConfig.KEY_EMAIL;
import static com.fyp.emart.project.Api.DataConfig.KEY_PASSWORD;
import static com.fyp.emart.project.Api.DataConfig.KEY_ROLE_ID;
import static com.fyp.emart.project.Api.DataConfig.MART_ADDRESS;
import static com.fyp.emart.project.Api.DataConfig.MART_EMAIL;
import static com.fyp.emart.project.Api.DataConfig.MART_NAME;
import static com.fyp.emart.project.Api.DataConfig.MART_PHONE;
import static com.fyp.emart.project.Api.DataConfig.MART_iD;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    /**
     * Please SignIn to continue
     */
    private TextView mTvSubtitleSignIn;
    /**
     * Email
     */
    private TextInputEditText mTitEmail;
    private TextInputLayout mTilEmail;
    /**
     * Password
     */
    private TextInputEditText mTitPassword;
    private TextInputLayout mTilPassword;
    /**
     * Forgot password?
     *
     */
    private TextView mTvForgotPassword;
    /**
     * Sign In
     */
    private Button mBtnSignIn;
    /**
     *
     */
    private TextView mTvToSignUp;
    LinearLayout loginForm;

    private CheckBox mCheckBox;
    private LinearLayout mLoginForm;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    Context mContext;
    BaseApiService mApiService;
    ProgressDialog loading;

    String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mApiService = UtilsApi.getAPIService(); // heat the contents of the package api helper
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        // Check if UserResponse is Already Logged In
        if (SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            doSomethingElse();

        } else {
            loginForm.setVisibility(View.VISIBLE);
        }


    }

    private void initView() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        mTvSubtitleSignIn = (TextView) findViewById(R.id.tvSubtitleSignIn);
        mTitEmail = (TextInputEditText) findViewById(R.id.titEmail);
        mTilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        mTitPassword = (TextInputEditText) findViewById(R.id.titPassword);
        mTilPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        mTvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        mBtnSignIn = (Button) findViewById(R.id.btnSignIn);
        mTvToSignUp = (TextView) findViewById(R.id.tvToSignUp);
        mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        loginForm = (LinearLayout) findViewById(R.id.loginForm);

        mTvForgotPassword.setOnClickListener(this);
        mBtnSignIn.setOnClickListener(this);
        mTvToSignUp.setOnClickListener(this);
        mCheckBox.setOnClickListener(this);
        mLoginForm = (LinearLayout) findViewById(R.id.loginForm);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            mTitEmail.setText(loginPreferences.getString("username", ""));
            mTitPassword.setText(loginPreferences.getString("password", ""));
            mCheckBox.setChecked(true);
        }
    }
    public void doSomethingElse() {
       /* startActivity(new Intent(LoginActivity.this, MainActivity.class));
        LoginActivity.this.finish();*/


        // Set Logged In statue to 'true'
        SaveSharedPreference.setLoggedIn(mContext, true);
        email = mTitEmail.getText().toString();
        password = mTitPassword.getText().toString();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTitEmail.getWindowToken(), 0);

        if (mCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", mTitEmail.getText().toString());
            loginPrefsEditor.putString("password", mTitPassword.getText().toString());
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

        loading = ProgressDialog.show(mContext, null, "Please wait...", true, false);
        requestLogin(email, password);
    }
    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()) {

            default:
                break;
            case R.id.tvForgotPassword:

                break;
            case R.id.btnSignIn:

                checkValidations();

                break;
            case R.id.tvToSignUp:

                i = new Intent(LoginActivity.this, SignupActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                break;
        }
    }

    private void requestLogin(final String email, final String password) {
        mApiService.loginUser(email, password)
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

                                    Intent i;

                                    SharedPreferences sp = getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString(KEY_EMAIL, email);
                                    editor.putString(KEY_PASSWORD, password);
                                    editor.putString(KEY_ROLE_ID, role);
                                    editor.apply();
                                    SaveSharedPreference.setLoggedIn(mContext, true);



                                    switch (role) {
                                        case "1"://Customer Role
                                            requestCustomerData(email);
                                            i = new Intent(LoginActivity.this, CustomerDashboardActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);
                                            finish();
                                            break;

                                        case "2"://Mart Role
                                            requestMartData(email);
                                            i = new Intent(LoginActivity.this, MartDashboardActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);
                                            finish();
                                            break;

                                        case "3"://Admin Role

                                            i = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);
                                            finish();
                                            break;

                                        case "0"://No Usser

                                            Toast.makeText(mContext, "No user in records", Toast.LENGTH_SHORT).show();
                                            break;

                                    }

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
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }

    private void checkValidations() {
        if (!isEmailValid(mTitEmail.getText().toString())) {
            mTitEmail.requestFocus();
            mTitEmail.setError("Valid email required.");
        } else if (mTitPassword.getText().length() < 1) {
            mTitPassword.requestFocus();
            mTitPassword.setError("Password required.");
        } else {

            email = mTitEmail.getText().toString();
            password = mTitPassword.getText().toString();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mTitEmail.getWindowToken(), 0);

            if (mCheckBox.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", mTitEmail.getText().toString());
                loginPrefsEditor.putString("password", mTitPassword.getText().toString());
                loginPrefsEditor.commit();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }

            loading = ProgressDialog.show(mContext, null, "Please wait...", true, false);
            requestLogin(email, password);
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

    private void requestCustomerData(String email) {
        mApiService.getCustomerProfile(email)
                .enqueue(new  Callback<List<CustomerProfileList>>() {
                    @Override
                    public void onResponse(Call<List<CustomerProfileList>> call, Response<List<CustomerProfileList>> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();

                            List<CustomerProfileList> adslist = response.body();

                            String name = adslist.get(0).getCname();
                            String id = adslist.get(0).getCid();
                            String phone = adslist.get(0).getPhone();
                            String email = adslist.get(0).getEmail();
                            String address = adslist.get(0).getAddress();

                            SharedPreferences sp = getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(CUSTOMER_NAME, name);
                            editor.putString(CUSTOMER_iD, id);
                            editor.putString(CUSTOMER_PHONE, phone);
                            editor.putString(CUSTOMER_EMAIL, email);
                            editor.putString(CUSTOMER_ADDRESS, address);
                            editor.apply();

                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CustomerProfileList>> call, Throwable t) {
                        Log.e("checkerror", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(LoginActivity.this, "network failure :( inform the user and possibly retry\n"+t.toString(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
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
                            String id = adslist.get(0).getId();
                            String phone = adslist.get(0).getPhone();
                            String email = adslist.get(0).getEmail();
                            String address = adslist.get(0).getAddress();

                            SharedPreferences sp = getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(MART_EMAIL, email);
                            editor.putString(MART_NAME, name);
                            editor.putString(MART_iD, id);
                            editor.putString(MART_PHONE, phone);
                            editor.putString(MART_ADDRESS, address);
                            editor.apply();

                        } else {
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MartProfileList>> call, Throwable t) {
                        Log.e("checkerror", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(LoginActivity.this, "network failure :( inform the user and possibly retry\n"+t.toString(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(LoginActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(LoginActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };
}
