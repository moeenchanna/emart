package com.fyp.emart.project.adapters;

import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fyp.emart.project.R;
import com.fyp.emart.project.model.ProductList;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyviewHolder> implements Filterable {

    Context context;
    private List<ProductList> productLists;
    private List<ProductList> productListsFiltered;

    public ProductAdapter(Context context, List<ProductList> productLists) {
        this.context = context;
        this.productLists = productLists;
    }


    public void setProductList(Context context, final List<ProductList> productLists1) {
        this.context = context;
        if (this.productLists == null) {
            this.productLists = productLists1;
            this.productListsFiltered = productLists1;
            notifyItemChanged(0, productListsFiltered.size());
        } else {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return ProductAdapter.this.productLists.size();
                }

                @Override
                public int getNewListSize() {
                    return productLists.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return ProductAdapter.this.productLists.get(oldItemPosition).getProductName() == productLists.get(newItemPosition).getProductName();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

                    ProductList newProduct = ProductAdapter.this.productLists.get(oldItemPosition);

                    ProductList oldProduct = productLists.get(newItemPosition);

                    return newProduct.getProductName() == oldProduct.getProductName();
                }
            });
            this.productLists = productLists;
            this.productListsFiltered = productLists;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public ProductAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_products, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.MyviewHolder holder, int position) {
        holder.tvproductname.setText(productLists.get(position).getProductName());
        //holder.tvproductbrand.setText(productLists.get(position).getProductBrand());
        holder.tvproductprice.setText(productLists.get(position).getProductPrice());

        RequestOptions options = new RequestOptions()
                .centerInside()
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading);

        String Simage = productLists.get(position).getProductimage();
        Simage.replace("\\/", "");

        Glide.with(context).load(Simage).apply(options).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if (productLists != null) {
            return productListsFiltered.size();
        } else {
            return 0;
        }

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListsFiltered = productLists;
                } else {
                    List<ProductList> filteredList = new ArrayList<>();
                    for (ProductList product : productLists) {
                        if (product.getProductName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(product);
                        }
                    }
                    productListsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListsFiltered = (ArrayList<ProductList>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvproductname, tvproductprice, tvproductbrand;
        ImageView image;

        public MyviewHolder(View itemView) {
            super(itemView);
            tvproductname = (TextView) itemView.findViewById(R.id.product_name);
            tvproductprice = (TextView) itemView.findViewById(R.id.product_price);
            //tvproductbrand = (TextView)itemView.findViewById(R.id.productbrand);
            image = (ImageView) itemView.findViewById(R.id.product_image);
        }
    }
}