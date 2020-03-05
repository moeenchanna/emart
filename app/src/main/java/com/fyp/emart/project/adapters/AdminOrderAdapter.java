package com.fyp.emart.project.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.emart.project.R;
import com.fyp.emart.project.model.AdminOrder;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.MyviewHolder> {

    private List<AdminOrder> adminOrderList;
    private Context context;

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
                .inflate(R.layout.recycler_admin_order, parent, false);


        return new MyviewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        final AdminOrder order = adminOrderList.get(position);

        String id = order.getStatusid();

        if (id.equals("0")) {
            holder.status.setTextColor(Color.RED);
            Toast.makeText(context, "0", Toast.LENGTH_SHORT).show();
            return;
        }
        if (id.equals("1")) {
            holder.status.setTextColor(Color.YELLOW);
            Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
            return;
        }
        if (id.equals("2")) {
            holder.status.setTextColor(Color.GREEN);
            Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
        }

        holder.date.setText(order.getDatetime());
        holder.order.setText(order.getOrderno());
        holder.email.setText(order.getCustemail());
        holder.total.setText(order.getSubtotal());
        holder.status.setText(order.getStatus());

    }

    @Override
    public int getItemCount() {
        if (adminOrderList != null) {
            return adminOrderList.size();
        }
        return 0;
    }

    static class MyviewHolder extends RecyclerView.ViewHolder {
        TextView date, order, name, email, total, status;

        MyviewHolder(View itemView) {
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
