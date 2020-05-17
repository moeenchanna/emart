package com.fyp.emart.project.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.CartActivity;
import com.fyp.emart.project.activity.PromotionActivity;
import com.fyp.emart.project.model.ComplaintList;
import com.fyp.emart.project.model.PromotionList;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class PromotionCodeAdapter extends RecyclerView.Adapter<PromotionCodeAdapter.ViewHolder> {

    private List<PromotionList> promotionLists;
    private Context context;

    public PromotionCodeAdapter(List<PromotionList> promotionLists, Context context) {
        this.promotionLists = promotionLists;
        this.context = context;
    }

    public void setCodeLists(List<PromotionList> promotionLists) {
        this.promotionLists = promotionLists;
        notifyDataSetChanged();
    }

    @Override
    public PromotionCodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.promotion_code_list, parent, false);

        return new PromotionCodeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PromotionCodeAdapter.ViewHolder holder, final int position) {

        holder.txtcode.setText(promotionLists.get(position).getCode());
        holder.title.setText(promotionLists.get(position).getTitle());
        holder.desc.setText(promotionLists.get(position).getDiscount() +" "+promotionLists.get(position).getDetail() );

        holder.apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, CartActivity.class).putExtra("promo",promotionLists.get(position).getAmount()));
                ((Activity)context).finish();

            }
        });

    }

    @Override
    public int getItemCount() {

        if(promotionLists != null){
            return promotionLists.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtcode,title, desc, tnc;
        Button apply_button;

        public ViewHolder(View itemView) {
            super(itemView);

            txtcode = itemView.findViewById(R.id.coupon_code);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);

            apply_button = itemView.findViewById(R.id.apply_button);

        }
    }
}
