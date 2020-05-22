package com.fyp.emart.project.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.utils.SaveSharedPreference;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPromoActivity extends AppCompatActivity implements View.OnClickListener {


    private ProgressDialog loading;
    private Context mContext;
    private BaseApiService mApiService;
    private ImageView mLogout;
    private Toolbar mBarTool;
    private TextInputEditText mTitle;
    private TextInputEditText mDetail;
    private TextInputEditText mCode;
    private TextInputEditText mDiscount;
    private TextInputEditText mAmount;
    private TextInputEditText mImageUrlPr;
    private Button mBtnupload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_promo);
        initView();

        mApiService = UtilsApi.getAPIService();
        mContext = this;
    }

    private void initView() {
        mLogout = (ImageView) findViewById(R.id.logout);
        mBarTool = (Toolbar) findViewById(R.id.tool_bar);
        mTitle = (TextInputEditText) findViewById(R.id.title);
        mDetail = (TextInputEditText) findViewById(R.id.detail);
        mCode = (TextInputEditText) findViewById(R.id.code);
        mDiscount = (TextInputEditText) findViewById(R.id.discount);
        mAmount = (TextInputEditText) findViewById(R.id.amount);
        mImageUrlPr = (TextInputEditText) findViewById(R.id.pr_image_url);
        mBtnupload = (Button) findViewById(R.id.btnupload);
        mLogout.setOnClickListener(this);
        mBtnupload.setOnClickListener(this);

        mBarTool.setTitle("Add Promotion");
        setSupportActionBar(mBarTool);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                // TODO 20/05/23
                logout();
                break;
            case R.id.btnupload:
                // TODO 20/05/23
                checkValidations();
                break;
            default:
                break;
        }
    }

    private void checkValidations() {

        if (mTitle.getText().length() < 1) {
            mTitle.requestFocus();
            mTitle.setError("Promo name required.");
        } else if (mDetail.getText().length() < 1) {
            mDetail.requestFocus();
            mDetail.setError("Promo detail required.");
        } else if (mCode.getText().length() < 1) {
            mCode.requestFocus();
            mCode.setError("Promo code required.");
        } else if (mDiscount.getText().length() < 1) {
            mDiscount.requestFocus();
            mDiscount.setError("Promo discount required.");
        } else if (mAmount.getText().length() < 1) {
            mAmount.requestFocus();
            mAmount.setError("Promo amount required.");
        } else if (mImageUrlPr.getText().length() < 1) {
            mImageUrlPr.requestFocus();
            mImageUrlPr.setError("Promo image url required.");
        } else {

            String promoname = mTitle.getText().toString();
            String amount = mAmount.getText().toString();
            String discount = mDiscount.getText().toString();
            String promoimage = mImageUrlPr.getText().toString();
            String code = mCode.getText().toString();
            String detail = mDetail.getText().toString();
           /* SharedPreferences sp = AddPromoActivity.this.getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
            String martid = sp.getString(MART_iD, null);
*/

            String martid = LoginActivity.getMARTID();

            loading = ProgressDialog.show(mContext, null, "Please wait...", true, false);
            addPromo(promoname, amount, discount, promoimage, martid, code, detail);
        }


    }

    private void addPromo(String promoname, String amount, String discount, String promoimage, String martid, String code, String detail) {

        mApiService.AddPromo(martid,promoname,detail,code,discount,amount,promoimage)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                if (response.body() != null) {

                                    String role = response.body().string();
                                    //Toast.makeText(mContext, role, Toast.LENGTH_SHORT).show();
                                    Log.d("debug", role);
                                    mTitle.setText("");
                                    mDetail.setText("");
                                    mCode.setText("");
                                    mDiscount.setText("");
                                    mImageUrlPr.setText("");
                                    mAmount.setText("");
                                    loading.dismiss();

                                    Toast.makeText(mContext, "Successfully added now add another", Toast.LENGTH_SHORT).show();
                                } else {
                                    // If the login fails
                                    // error case
                                    switch (response.code()) {
                                        case 404:
                                            Toast.makeText(AddPromoActivity.this, "Server not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        case 500:
                                            Toast.makeText(AddPromoActivity.this, "Server request not found", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(AddPromoActivity.this, "unknown error", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(AddPromoActivity.this, "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });


    }


    public void logout() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(AddPromoActivity.this);
        builder1.setMessage("Are you sure you want to logout?");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SaveSharedPreference.setLoggedIn(AddPromoActivity.this, false);
                        Intent intent = new Intent(AddPromoActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
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
}
