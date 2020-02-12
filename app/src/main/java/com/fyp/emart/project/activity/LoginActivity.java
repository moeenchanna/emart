package com.fyp.emart.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fyp.emart.project.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

                i = new Intent(LoginActivity.this, DashboardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);

                break;
            case R.id.tvToSignUp:

                i = new Intent(LoginActivity.this,SignupActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                break;
        }
    }
}
