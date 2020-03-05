package com.fyp.emart.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.emart.project.R;
import com.fyp.emart.project.model.AdminOrder;
import com.fyp.emart.project.model.Cart;
import com.fyp.emart.project.model.MartList;
import com.fyp.emart.project.utils.LocalStorage;
import com.google.gson.Gson;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.MyviewHolder> {

    List<AdminOrder> adminOrderList;
    Context context;
    LocalStorage localStorage;
    Gson gson;

    public AdminOrderAdapter() {
    }

    public AdminOrderAdapter(List<AdminOrder> adminOrderList, Context context) {
        this.adminOrderList = adminOrderList;
        this.context = context;
    }

    public void setOrderList(List<AdminOrder> adminOrders) {
        this.adminOrderList = adminOrders;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cart, parent, false);


        return new MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        final AdminOrder order = adminOrderList.get(position);
        localStorage = new LocalStorage(context);
        gson = new Gson();

        holder.date.setText(order.getDatetime());
        holder.order.setText(order.getOrderno());
        holder.email.setText(order.getCustemail());
        holder.total.setText(order.getSubtotal());
        holder.status.setText(order.getStatus());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView date,order,name,email,total,status;

        public MyviewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.admin_order_date);
            order = itemView.findViewById(R.id.admin_order_number);
            name = itemView.findViewById(R.id.admin_order_name);
            email = itemView.findViewById(R.id.admin_order_email);
            total = itemView.findViewById(R.id.admin_order_total);
            status = itemView.findViewById(R.id.admin_order_status);
        }
    }

}
