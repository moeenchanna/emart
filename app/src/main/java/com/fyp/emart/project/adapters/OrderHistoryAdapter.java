package com.fyp.emart.project.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fyp.emart.project.R;
import com.fyp.emart.project.model.ProductDetailsList;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private List<ProductDetailsList> ProductDetailsLists;
    private Context context;

    public OrderHistoryAdapter(List<ProductDetailsList> ProductDetailsLists, Context context) {
        this.ProductDetailsLists = ProductDetailsLists;
        this.context = context;
    }

    public void setCodeLists(List<ProductDetailsList> ProductDetailsLists) {
        this.ProductDetailsLists = ProductDetailsLists;
        notifyDataSetChanged();
    }

    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.order_product_list, parent, false);

        return new OrderHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderHistoryAdapter.ViewHolder holder, final int position) {

        holder.grocery_productName.setText(ProductDetailsLists.get(position).getName());
        holder.txt_grocery_quantity.setText(ProductDetailsLists.get(position).getQty());
        holder.txt_grocery_amount.setText(ProductDetailsLists.get(position).getCost());

        String Simage = ProductDetailsLists.get(position).getUrl();
        Simage.replace("\\/", "");

        Picasso.get()
                .load(Simage)
                .into(holder.img_view_grocery_product, new Callback() {
                    @Override
                    public void onSuccess() {
                      //  holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Error : ", e.getMessage());
                        holder.img_view_grocery_product.setImageResource(R.drawable.loading);
                    }
                });

    }


    @Override
    public int getItemCount() {

        if (ProductDetailsLists != null) {
            return ProductDetailsLists.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView grocery_productName, txt_grocery_quantity, txt_grocery_amount;
        ImageView img_view_grocery_product;


        public ViewHolder(View itemView) {
            super(itemView);

            grocery_productName = itemView.findViewById(R.id.grocery_productName);
            txt_grocery_quantity = itemView.findViewById(R.id.txt_grocery_quantity);
            txt_grocery_amount = itemView.findViewById(R.id.txt_grocery_amount);

            img_view_grocery_product = itemView.findViewById(R.id.img_view_grocery_product);

        }
    }
}
