package com.fyp.emart.project.fragment.mart_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.R;
import com.fyp.emart.project.adapters.AdminReviewsAdapter;
import com.fyp.emart.project.model.ReviewList;

import java.util.List;

public class MartReviewListFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdminReviewsAdapter reviewsAdapter;
    private ProgressDialog progressDialog;

    private List<ReviewList> reviewsModel;

    private Context mContext;
    private BaseApiService mApiService;

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

        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();
        recyclerView = view.findViewById(R.id.reviews_recycler_view);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading please wait...");

        reviewsData();

    }

    private void reviewsData() {

        progressDialog.show();

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        reviewsAdapter = new AdminReviewsAdapter(reviewsModel, mContext);
        recyclerView.setAdapter(reviewsAdapter);
        recyclerView.setHasFixedSize(true);

        final Call<List<ReviewList>> call = mApiService.getAdminReviews();
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
}

