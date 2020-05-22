package com.fyp.emart.project.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.AdminOrderAdapter;
import com.fyp.emart.project.adapters.PromotionCodeAdapter;
import com.fyp.emart.project.model.PromotionList;
import com.fyp.emart.project.model.PromotionList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fyp.emart.project.Api.DataConfig.MART_iD;
import static com.fyp.emart.project.Api.DataConfig.TEMP_MART_iD;

public class PromotionActivity extends AppCompatActivity {

    private Toolbar mBarTool;

    private RecyclerView promtion_list;
    PromotionCodeAdapter codeAdapter;
    List<PromotionList> promotionLists;
    private ProgressDialog progressDialog;



    private Context mContext;
    private BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion);

        mApiService = UtilsApi.getAPIService();
        mContext = this;

        initView();
    }

    private void initView() {
        mBarTool = (Toolbar) findViewById(R.id.tool_bar);
        mBarTool.setTitle("Apply Promotion");
        setSupportActionBar(mBarTool);

      /*  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        promtion_list = findViewById(R.id.coupon_list);

        promtion_list.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading please wait...");

        SharedPreferences sp = getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        String martid = sp.getString(TEMP_MART_iD, null);
        getCouponList(martid);

    }

    private void getCouponList(String martid) {

        progressDialog.show();
        promtion_list.setLayoutManager(new LinearLayoutManager(mContext));
        codeAdapter = new PromotionCodeAdapter(promotionLists,mContext);
        promtion_list.setAdapter(codeAdapter);

        final Call<List<PromotionList>> promtions = mApiService.gePromotion(martid);
        promtions.enqueue(new Callback<List<PromotionList>>() {
            @Override
            public void onResponse(@Nullable Call<List<PromotionList>> call, @Nullable Response<List<PromotionList>> response) {
                progressDialog.dismiss();
                promotionLists = response.body();
                Log.d("TAG","Response = "+ promotionLists);
                codeAdapter.setCodeLists(promotionLists);

            }

            @Override
            public void onFailure(@Nullable Call<List<PromotionList>> call, @Nullable Throwable t) {
                progressDialog.dismiss();
                Log.e("Error", t.getMessage());
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

}
