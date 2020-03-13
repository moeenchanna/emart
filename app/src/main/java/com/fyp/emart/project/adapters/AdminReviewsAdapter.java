package com.fyp.emart.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.emart.project.R;
import com.fyp.emart.project.model.ComplaintList;
import com.fyp.emart.project.model.ReviewList;

import java.util.List;

public class AdminReviewsAdapter extends RecyclerView.Adapter<AdminReviewsAdapter.MyviewHolder> {

    private List<ReviewList> reviewLists;
    private Context context;

    public AdminReviewsAdapter(List<ReviewList> reviewLists, Context context) {
        this.reviewLists = reviewLists;
        this.context = context;
    }

    public void setReviewLists(List<ReviewList> reviewLists) {
        this.reviewLists = reviewLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdminReviewsAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_reviews_layout, parent, false);


        return new AdminReviewsAdapter.MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminReviewsAdapter.MyviewHolder holder, int position) {

        final ReviewList review = reviewLists.get(position);
        String id = review.getId();


        holder.time.setText(review.getDatetime());
        holder.comment.setText(review.getComment());

    }

    @Override
    public int getItemCount() {
        if (reviewLists != null) {
            return reviewLists.size();
        }
        return 0;
    }

    static class MyviewHolder extends RecyclerView.ViewHolder {
        TextView time, comment;

        MyviewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.reviews_time);
            comment = itemView.findViewById(R.id.reviews_comment);
        }
    }

}
