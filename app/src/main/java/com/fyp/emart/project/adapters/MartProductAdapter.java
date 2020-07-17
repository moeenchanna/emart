package com.fyp.emart.project.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.emart.project.R;
import com.fyp.emart.project.model.ProductList;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MartProductAdapter extends RecyclerView.Adapter<MartProductAdapter.MyviewHolder>  {

    Context context;
    private List<ProductList> productLists;



    public MartProductAdapter(Context context, List<ProductList> productLists) {
        this.context = context;
        this.productLists = productLists;
    }

    public void setProductList(List<ProductList> productLists) {
        this.productLists = productLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_mart_products, parent, false);
        return new MartProductAdapter.MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyviewHolder holder, int position) {

        holder.title.setText(productLists.get(position).getProductName());
        holder.attribute.setText(productLists.get(position).getProductBrand());
        holder.price.setText(productLists.get(position).getProductPrice());

        holder.offer.setVisibility(View.GONE);
        String Simage = productLists.get(position).getProductimage();
        Simage.replace("\\/", "");

        Picasso.get()
                .load(Simage)
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Error : ", e.getMessage());
                        holder.imageView.setImageResource(R.drawable.loading);
                    }
                });

    }

    @Override
    public int getItemCount() {
        if(productLists != null){
            return productLists.size();
        }
        return 0;

    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title;
        ProgressBar progressBar;
        CardView cardView;
        TextView offer, currency, price,attribute;


        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.product_title);
            imageView = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.product_title);
            progressBar = itemView.findViewById(R.id.progressbar);
            cardView = itemView.findViewById(R.id.card_view);
            offer = itemView.findViewById(R.id.product_discount);
            currency = itemView.findViewById(R.id.product_currency);
            price = itemView.findViewById(R.id.product_price);
            attribute = itemView.findViewById(R.id.product_brand);

        }
    }
}
