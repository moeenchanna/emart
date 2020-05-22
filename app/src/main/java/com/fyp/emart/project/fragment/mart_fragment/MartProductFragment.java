package com.fyp.emart.project.fragment.mart_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.AddPromoActivity;
import com.fyp.emart.project.activity.LoginActivity;
import com.fyp.emart.project.utils.SaveSharedPreference;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MartProductFragment extends Fragment implements View.OnClickListener {


    private TextInputEditText mProductname;
    private TextInputEditText mPriceProduct;
    private TextInputEditText mQuanProduct;
    private TextInputEditText mCodeProduct;

    private Button mButton;

    private TextInputEditText mImageUrlProduct;
    private ImageView mLogout;
    private ProgressDialog loading;
    private Context mContext;
    private BaseApiService mApiService;
    private TextInputEditText mDescription;
    private TextInputEditText mBrand;
    private Button mBtnpromo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mart_product, container, false);
        // Inflate the layout for this fragment

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mLogout = (ImageView) view.findViewById(R.id.logout);


        mProductname = (TextInputEditText) view.findViewById(R.id.productname);
        mPriceProduct = (TextInputEditText) view.findViewById(R.id.product_price);
        mQuanProduct = (TextInputEditText) view.findViewById(R.id.product_quan);
        mImageUrlProduct = (TextInputEditText) view.findViewById(R.id.pr_image_url);
        mDescription = (TextInputEditText) view.findViewById(R.id.description);
        mBrand = (TextInputEditText) view.findViewById(R.id.brand);

        mButton = (Button) view.findViewById(R.id.btnupload);
        mButton.setOnClickListener(this);
        mLogout.setOnClickListener(this);

        mBtnpromo = (Button) view.findViewById(R.id.btnpromo);
        mBtnpromo.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                logout();
                break;
            case R.id.btnupload:
                checkValidations();
                //Toast.makeText(mContext, ""+LoginActivity.getMARTID(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnpromo:// TODO 20/05/23
                Intent i = new Intent(getActivity(), AddPromoActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    private void checkValidations() {

        if (mProductname.getText().length() < 1) {
            mProductname.requestFocus();
            mProductname.setError("Product name required.");
        } else if (mDescription.getText().length() < 1) {
            mDescription.requestFocus();
            mDescription.setError("Product description required.");
        } else if (mPriceProduct.getText().length() < 1) {
            mPriceProduct.requestFocus();
            mPriceProduct.setError("Product price required.");
        } else if (mQuanProduct.getText().length() < 1) {
            mQuanProduct.requestFocus();
            mQuanProduct.setError("Product quantity required.");
        } else if (mBrand.getText().length() < 1) {
            mBrand.requestFocus();
            mBrand.setError("Product quantity required.");
        } else if (mImageUrlProduct.getText().length() < 1) {
            mImageUrlProduct.requestFocus();
            mImageUrlProduct.setError("Product image url required.");
        } else {

            String productname = mProductname.getText().toString();
            String productprice = mPriceProduct.getText().toString();
            String productquantity = mQuanProduct.getText().toString();
            String productimage = mImageUrlProduct.getText().toString();
            String brand = mBrand.getText().toString();
            String description = mDescription.getText().toString();
           /* SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
            String martid = sp.getString(MART_iD, null);
*/

            String martid = LoginActivity.getMARTID();

            loading = ProgressDialog.show(mContext, null, "Please wait...", true, false);
            addProducts(productname, productprice, productquantity, productimage, martid, brand, description);

        }
    }

    private void addProducts(String productname, String productprice, String productquantity, String productimage, String martid, String brand, String description) {

        mApiService.AddProducts(productname, description, productimage, brand, productprice, productquantity, martid)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                if (response.body() != null) {

                                    String role = response.body().string();
                                    //Toast.makeText(mContext, role, Toast.LENGTH_SHORT).show();
                                    Log.d("debug", role);
                                    mProductname.setText("");
                                    mPriceProduct.setText("");
                                    mQuanProduct.setText("");
                                    mImageUrlProduct.setText("");
                                    mBrand.setText("");
                                    mDescription.setText("");
                                    loading.dismiss();

                                    Toast.makeText(mContext, "Successfully added now add another", Toast.LENGTH_SHORT).show();
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
