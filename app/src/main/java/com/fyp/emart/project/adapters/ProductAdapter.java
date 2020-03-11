package com.fyp.emart.project.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fyp.emart.project.BaseActivity;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.ProductActivity;

import com.fyp.emart.project.model.Cart;
import com.fyp.emart.project.model.ProductList;
import com.fyp.emart.project.utils.AddorRemoveCallbacks;
import com.fyp.emart.project.utils.LocalStorage;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

    public void setProductList(Context context,final List<ProductList> productLists){
        this.context = context;
        if(this.productLists == null){
            this.productLists = productLists;
            this.productListsFiltered = productLists;
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

                    return newProduct.getProductName() == oldProduct.getProductName() ;
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

        holder.title.setText(product.getProductName());
        holder.attribute.setText(product.getProductBrand());
        holder.price.setText(product.getProductPrice());

        holder.offer.setVisibility(View.GONE);
       /* if (product.getDiscount() == null || product.getDiscount().length() == 0) {
            holder.offer.setVisibility(View.GONE);
        }*/

        String Simage = product.getProductimage();
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
                    }
                });

        if (!cartList.isEmpty()) {
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).getId().equalsIgnoreCase(product.getIdProduct())) {
                    holder.addToCart.setVisibility(View.GONE);
                    holder.subTotal.setVisibility(View.VISIBLE);
                    holder.quantity.setText(cartList.get(i).getQuantity());
                    _quantity = cartList.get(i).getQuantity();
                    _price = product.getProductPrice();
                    _subtotal = String.valueOf(Double.parseDouble(_price) * Integer.parseInt(_quantity));
                    holder.subTotal.setText(_quantity + "X" + _price + "= Rs." + _subtotal);
                    Log.d("Tag : ", cartList.get(i).getId() + "-->" + product.getIdProduct());
                }
            }
        } else {

            holder.quantity.setText("1");
        }

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


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, ProductActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);*/
                Toast.makeText(context, "Product Clicked", Toast.LENGTH_LONG).show();
            }
        });

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.addToCart.setVisibility(View.GONE);
                holder.subTotal.setVisibility(View.VISIBLE);

                _price = product.getProductPrice();
                _quantity = holder.quantity.getText().toString();
                _attribute = product.getProductBrand();

                if (Integer.parseInt(_quantity) != 0) {
                    _subtotal = String.valueOf(Double.parseDouble(_price) * Integer.parseInt(_quantity));
                    holder.subTotal.setText(_quantity + "X" + _price + "= Rs." + _subtotal);
                    if (context instanceof ProductActivity) {
                        Cart cart = new Cart(product.getIdProduct(), product.getProductName(), product.getProductimage(), "Rs", _price, _attribute, _quantity, _subtotal,product.getMartid());
                        cartList = ((BaseActivity) context).getCartList();
                        cartList.add(cart);
                        String cartStr = gson.toJson(cartList);
                        //Log.d("CART", cartStr);
                        localStorage.setCart(cartStr);
                        ((AddorRemoveCallbacks) context).onAddProduct();
                        notifyItemChanged(position);
                    }
                } else {
                    Toast.makeText(context, "Please Add Quantity", Toast.LENGTH_SHORT).show();
                }


            }
        });

      /*  holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductViewActivity.class);
                intent.putExtra("id", product.getIdProduct());
                intent.putExtra("title", product.getProductName());
                intent.putExtra("image", product.getProductimage());
                intent.putExtra("price", product.getProductPrice());
                intent.putExtra("currency", "Rs");
                intent.putExtra("attribute", product.getProductBrand());
                intent.putExtra("discount", "0");
                intent.putExtra("description", product.getProductDescription());
                intent.putExtra("mart", product.getMartid());

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });*/


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
        ImageView imageView;
        TextView title;
        ProgressBar progressBar;
        CardView cardView;
        TextView offer, currency, price, quantity, attribute, addToCart, subTotal;
        Button plus, minus;
        public MyviewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.product_title);
            imageView = itemView.findViewById(R.id.product_image);
            title = itemView.findViewById(R.id.product_title);
            progressBar = itemView.findViewById(R.id.progressbar);
            cardView = itemView.findViewById(R.id.card_view);
            offer = itemView.findViewById(R.id.product_discount);
            currency = itemView.findViewById(R.id.product_currency);
            price = itemView.findViewById(R.id.product_price);
            quantity = itemView.findViewById(R.id.quantity);
            addToCart = itemView.findViewById(R.id.add_to_cart);
            attribute = itemView.findViewById(R.id.product_brand);
            plus = itemView.findViewById(R.id.quantity_plus);
            minus = itemView.findViewById(R.id.quantity_minus);
            subTotal = itemView.findViewById(R.id.sub_total);
        }
    }
}