package com.fyp.emart.project.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fyp.emart.project.Api.DataConfig.KEY_EMAIL;
import static com.fyp.emart.project.Api.DataConfig.KEY_PASSWORD;
import static com.fyp.emart.project.Api.DataConfig.KEY_ROLE_ID;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     *
     */
    private TextView mTvTitleApp;
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

    Context mContext;
    BaseApiService mApiService;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        mApiService = UtilsApi.getAPIService(); // heat the contents of the package api helper
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
    }

    private void initView() {

        mTvTitleApp = (TextView) findViewById(R.id.tvTitleApp);
        mTvSubtitleSignIn = (TextView) findViewById(R.id.tvSubtitleSignIn);
        mTitEmail = (TextInputEditText) findViewById(R.id.titEmail);
        mTilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        mTitPassword = (TextInputEditText) findViewById(R.id.titPassword);
        mTilPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        mTvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        mBtnSignIn = (Button) findViewById(R.id.btnSignIn);
        mTvToSignUp = (TextView) findViewById(R.id.tvToSignUp);

        mTvForgotPassword.setOnClickListener(this);
        mBtnSignIn.setOnClickListener(this);
        mTvToSignUp.setOnClickListener(this);
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

                i = new Intent(LoginActivity.this,SignupActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
                break;
        }
    }

    private void requestLogin(final String email, final String password){
        mApiService.loginUser(email, password)
       // mApiService.loginUser("moeen@gmail.com", "moeen")
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

                                    Intent i;

                                    SharedPreferences sp = getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString(KEY_EMAIL, email);
                                    editor.putString(KEY_PASSWORD, password);
                                    editor.putString(KEY_ROLE_ID, role);
                                    editor.apply();

                                    switch (role)
                                    {
                                        case "1"://Customer Role

                                            i = new Intent(LoginActivity.this, CustomerDashboardActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(i);
                                            finish();
                                            break;

                                        case "2"://Mart Role

                                            i = new Intent(LoginActivity.this, MartDashboardActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(i);
                                            finish();
                                            break;

                                        case "3"://Admin Role

                                            i = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(i);
                                            finish();
                                            break;

                                        case "0"://No Usser

                                            Toast.makeText(mContext, "No user in records", Toast.LENGTH_SHORT).show();
                                            break;

                                    }

                                }
                                else
                                {
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


                        }
                          else {
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
        }

        else if (mTitPassword.getText().length() < 1) {
            mTitPassword.requestFocus();
            mTitPassword.setError("Password required.");
        }

        else {

            String email = mTitEmail.getText().toString();
            String password = mTitPassword.getText().toString();
            loading = ProgressDialog.show(mContext, null, "Please wait...", true, false);
            requestLogin(email,password);
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
