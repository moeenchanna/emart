package com.fyp.emart.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.emart.project.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView mTvTitleApp;
    /**
     * Please SignUp for a new customer or vendor
     */
    private TextView mTvSubtitleSignUp;
    /**
     * Name
     */
    private TextInputEditText mTitName;
    private TextInputLayout mTilName;
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
     * Phone
     */
    private TextInputEditText mTitPhone;
    private TextInputLayout mTilPhone;
    /**
     * Address
     */
    private TextInputEditText mTitAddress;
    private TextInputLayout mTilAddress;
    /**
     * Customer
     */
    private RadioButton mRadioCustomer;
    /**
     * Vendor
     */
    private RadioButton mRadioVendor;
    private RadioGroup mRadioGroup;
    /**
     * Sign Up
     */
    private Button mBtnSignUp;
    private Button mBtnLogin;

    String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        initView();
    }

    private void initView() {
        mTvTitleApp = (TextView) findViewById(R.id.tvTitleApp);
        mTvSubtitleSignUp = (TextView) findViewById(R.id.tvSubtitleSignUp);
        mTitName = (TextInputEditText) findViewById(R.id.titName);
        mTilName = (TextInputLayout) findViewById(R.id.tilName);
        mTitEmail = (TextInputEditText) findViewById(R.id.titEmail);
        mTilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        mTitPassword = (TextInputEditText) findViewById(R.id.titPassword);
        mTilPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        mTitPhone = (TextInputEditText) findViewById(R.id.titPhone);
        mTilPhone = (TextInputLayout) findViewById(R.id.tilPhone);
        mTitAddress = (TextInputEditText) findViewById(R.id.titAddress);
        mTilAddress = (TextInputLayout) findViewById(R.id.tilAddress);
        mRadioCustomer = (RadioButton) findViewById(R.id.radioCustomer);
        mRadioVendor = (RadioButton) findViewById(R.id.radioVendor);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mBtnSignUp = (Button) findViewById(R.id.btnSignUp);
        mBtnLogin = (Button) findViewById(R.id.btnLogin);

        mBtnSignUp.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btnSignUp:

                if (mRadioCustomer.isChecked()) {
                    selectedUser = mRadioCustomer.getText().toString();
                } else if (mRadioVendor.isChecked()) {
                    selectedUser = mRadioVendor.getText().toString();
                }
                Toast.makeText(getApplicationContext(), selectedUser, Toast.LENGTH_LONG).show();

                break;
            case R.id.btnLogin:

                Intent i = new Intent(SignupActivity.this,LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();

                break;
        }
    }
}
