package com.fyp.emart.project.fragment.mart_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.DataConfig;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.LoginActivity;
import com.fyp.emart.project.adapters.AdminReviewsAdapter;
import com.fyp.emart.project.model.ReviewList;
import com.fyp.emart.project.utils.SaveSharedPreference;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.fyp.emart.project.Api.DataConfig.CUSTOMER_iD;
import static com.fyp.emart.project.Api.DataConfig.MART_iD;

public class MartReviewListFragment extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private AdminReviewsAdapter reviewsAdapter;
    private ProgressDialog progressDialog;

    private List<ReviewList> reviewsModel;

    private Context mContext;
    private BaseApiService mApiService;
    private ImageView mLogout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mart_review_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mLogout = (ImageView) view.findViewById(R.id.logout);
        mLogout.setOnClickListener(this);

        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();
        recyclerView = view.findViewById(R.id.reviews_recycler_view);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading please wait...");

        SharedPreferences sp = getActivity().getSharedPreferences(DataConfig.SHARED_PREF_NAME, MODE_PRIVATE);
        String martid = sp.getString(MART_iD, null);
        reviewsData(martid);

    }

    private void reviewsData(String martid) {

        progressDialog.show();

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        reviewsAdapter = new AdminReviewsAdapter(reviewsModel, mContext);
        recyclerView.setAdapter(reviewsAdapter);
        recyclerView.setHasFixedSize(true);

        final Call<List<ReviewList>> call = mApiService.getMartReviews(martid);
        call.enqueue(new Callback<List<ReviewList>>() {
            @Override
            public void onResponse(@Nullable Call<List<ReviewList>> call, @Nullable Response<List<ReviewList>> response) {
                progressDialog.dismiss();
                assert response != null;
                reviewsModel = response.body();
                Log.d("TAG", "Response = " + reviewsModel);
                reviewsAdapter.setReviewLists(reviewsModel);

            }

            @Override
            public void onFailure(@Nullable Call<List<ReviewList>> call, @Nullable Throwable t) {
                progressDialog.dismiss();
                assert t != null;
                Log.e("Error", t.getMessage());
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void  logout(){
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
            default:
                break;
        }
    }
}

