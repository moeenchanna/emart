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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fyp.emart.project.Api.DataConfig.KEY_ADDRESS;
import static com.fyp.emart.project.Api.DataConfig.KEY_EMAIL;
import static com.fyp.emart.project.Api.DataConfig.KEY_PASSWORD;
import static com.fyp.emart.project.Api.DataConfig.KEY_PHONE;
import static com.fyp.emart.project.Api.DataConfig.KEY_ROLE_ID;
import static com.fyp.emart.project.Api.DataConfig.KEY_USERNAME;

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


                loading = ProgressDialog.show(mContext, null, "Please wait...", true, false);
                requestLogin();



                break;
            case R.id.tvToSignUp:

                i = new Intent(LoginActivity.this,SignupActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                break;
        }
    }

    private void requestLogin(){
        mApiService.loginRequest(mTitEmail.getText().toString(), mTitPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("Success").equals("true")){
                                    // If the login is successful then the name data is in the response API
                                    // will be parsed to the next activity.

                                    String Username = jsonRESULTS.getJSONObject("Content").getString("username");
                                    String phone = jsonRESULTS.getJSONObject("Content").getString("phone");
                                    String address = jsonRESULTS.getJSONObject("Content").getString("address");
                                    String roleid = jsonRESULTS.getJSONObject("Content").getString("role");

                                    SharedPreferences sp = getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString(KEY_EMAIL, mTitEmail.getText().toString());
                                    editor.putString(KEY_PASSWORD, mTitPassword.getText().toString());
                                    editor.putString(KEY_USERNAME, Username);
                                    editor.putString(KEY_ADDRESS, address);
                                    editor.putString(KEY_PHONE, phone);
                                    editor.putString(KEY_ROLE_ID, roleid);

                                    editor.apply();

                                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(i);

                                } else {
                                    // If the login fails
                                    String error_message = jsonRESULTS.getString("Message");

                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
}
