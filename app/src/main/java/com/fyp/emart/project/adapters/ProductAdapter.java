package com.fyp.emart.project.adapters;

import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fyp.emart.project.BaseActivity;
import com.fyp.emart.project.R;
import com.fyp.emart.project.model.Cart;
import com.fyp.emart.project.model.ProductList;
import com.fyp.emart.project.utils.LocalStorage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyviewHolder> implements Filterable {

    Context context;
    private List<ProductList> productLists;
    private List<ProductList> productListsFiltered;

    int pQuantity = 1;
    LocalStorage localStorage;
    Gson gson;
    List<Cart> cartList = new ArrayList<>();
    String _quantity, _price, _attribute, _subtotal;

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
    public void onBindViewHolder(final ProductAdapter.MyviewHolder holder, final int position) {

        final ProductList product = productLists.get(position);
        localStorage = new LocalStorage(context);
        gson = new Gson();
        cartList = ((BaseActivity) context).getCartList();

        holder.tvproductname.setText(product.getProductName());
        //holder.tvproductbrand.setText(productLists.get(position).getProductBrand());
        holder.tvproductprice.setText(product.getProductPrice());

        RequestOptions options = new RequestOptions()
                .centerInside()
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading);

        String Simage = product.getProductimage();
        Simage.replace("\\/", "");

        Glide.with(context).load(Simage).apply(options).into(holder.image);

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pQuantity = Integer.parseInt(holder.quantity.getText().toString());
                if (pQuantity >= 1) {
                    int total_item = Integer.parseInt(holder.quantity.getText().toString());
                    total_item++;
                    holder.quantity.setText(total_item + "");
                    for (int i = 0; i < cartList.size(); i++) {

                        if (cartList.get(i).getId().equalsIgnoreCase(product.getIdProduct())) {

                            // Log.d("totalItem", total_item + "");

                            _subtotal = String.valueOf(Double.parseDouble(holder.price.getText().toString()) * total_item);
                            cartList.get(i).setQuantity(holder.quantity.getText().toString());
                            cartList.get(i).setSubTotal(_subtotal);
                            holder.subTotal.setText(total_item + "X" + holder.price.getText().toString() + "= Rs." + _subtotal);
                            String cartStr = gson.toJson(cartList);
                            //Log.d("CART", cartStr);
                            localStorage.setCart(cartStr);
                            notifyItemChanged(position);
                        }
                    }
                }

            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pQuantity = Integer.parseInt(holder.quantity.getText().toString());
                if (pQuantity != 1) {
                    int total_item = Integer.parseInt(holder.quantity.getText().toString());
                    total_item--;
                    holder.quantity.setText(total_item + "");
                    for (int i = 0; i < cartList.size(); i++) {
                        if (cartList.get(i).getId().equalsIgnoreCase(product.getIdProduct())) {

                            //holder.quantity.setText(total_item + "");
                            //Log.d("totalItem", total_item + "");
                            _subtotal = String.valueOf(Double.parseDouble(holder.price.getText().toString()) * total_item);
                            cartList.get(i).setQuantity(holder.quantity.getText().toString());
                            cartList.get(i).setSubTotal(_subtotal);
                            holder.subTotal.setText(total_item + "X" + holder.price.getText().toString() + "= Rs." + _subtotal);
                            String cartStr = gson.toJson(cartList);
                            //Log.d("CART", cartStr);
                            localStorage.setCart(cartStr);
                            notifyItemChanged(position);
                        }
                    }

                }

            }
        });
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

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvproductname, tvproductprice, tvproductbrand;
        ImageView image;
        Button plus, minus;
        TextView offer, currency, price, quantity, attribute, addToCart, subTotal;
        public MyviewHolder(View itemView) {
            super(itemView);
            tvproductname = (TextView) itemView.findViewById(R.id.product_name);
            tvproductprice = (TextView) itemView.findViewById(R.id.product_price);
            //tvproductbrand = (TextView)itemView.findViewById(R.id.productbrand);
            image = (ImageView) itemView.findViewById(R.id.product_image);


            currency = itemView.findViewById(R.id.product_currency);
            price = itemView.findViewById(R.id.product_price);
            quantity = itemView.findViewById(R.id.quantity);
            addToCart = itemView.findViewById(R.id.add_to_cart);
            attribute = itemView.findViewById(R.id.product_attribute);
            plus = itemView.findViewById(R.id.quantity_plus);
            minus = itemView.findViewById(R.id.quantity_minus);
        }
    }
}