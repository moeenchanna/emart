package com.fyp.emart.project.adapters;

import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fyp.emart.project.R;
import com.fyp.emart.project.model.ProductList;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyviewHolder> {

    Context context;
    List<ProductList> productLists;

    public ProductAdapter(Context context, List<ProductList> productLists) {
        this.context = context;
        this.productLists = productLists;
    }

    public void setProductList(List<ProductList> productList) {
        this.productLists = productList;
        notifyDataSetChanged();
    }

    @Override
    public ProductAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_product_layout,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.MyviewHolder holder, int position) {
        holder.tvproductname.setText(productLists.get(position).getProductName());
        holder.tvproductbrand.setText(productLists.get(position).getProductBrand());
        holder.tvproductprice.setText(productLists.get(position).getProductPrice());

        RequestOptions options = new RequestOptions()
                .centerInside()
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading);

        String Simage = productLists.get(position).getProductimage();
        Simage.replace("\\/","");

        Glide.with(context).load(Simage).apply(options).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if(productLists != null){
            return productLists.size();
        }
        return 0;

    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvproductname,tvproductprice,tvproductbrand;
        ImageView image;

        public MyviewHolder(View itemView) {
            super(itemView);
            tvproductname = (TextView)itemView.findViewById(R.id.productname);
            tvproductprice = (TextView)itemView.findViewById(R.id.productprice);
            tvproductbrand = (TextView)itemView.findViewById(R.id.productbrand);
            image = (ImageView)itemView.findViewById(R.id.image);
        }
    }
}