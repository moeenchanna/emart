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
import com.fyp.emart.project.model.OrderList;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.MyviewHolder> {

    private List<OrderList> adminOrderListModel;
    private Context context;

    public AdminOrderAdapter(List<OrderList> adminOrderListModel, Context context) {
        this.adminOrderListModel = adminOrderListModel;
        this.context = context;
    }

    public void setOrderList(List<OrderList> adminOrderModels) {
        this.adminOrderListModel = adminOrderModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_orders, parent, false);


        return new MyviewHolder(itemView);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        final OrderList order = adminOrderListModel.get(position);
        String id = order.getStatusid();


        holder.date.setText("Date Time: "+order.getDatetime());
        holder.order.setText("Order No: "+order.getOrderno());
        //holder.email.setText(order.getCustemail());
        holder.total.setText("Total Amount Rs: " + order.getSubtotal());
        // holder.status.setText(order.getStatus());

      /*  if (id.contains("0")) {
            holder.status.setTextColor(Color.RED);// on hold
           //// Toast.makeText(context, "0", Toast.LENGTH_SHORT).show();
            return;
        }
        if (id.contains("1")) {
            holder.status.setTextColor(Color.MAGENTA);// in process
            //Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
            return;
        }
        if (id.contains("2")) {
            holder.status.setTextColor(Color.GREEN);// delievered success
           // Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
        }*/

    }

    @Override
    public int getItemCount() {
        if (adminOrderListModel != null) {
            return adminOrderListModel.size();
        }
        return 0;
    }

    static class MyviewHolder extends RecyclerView.ViewHolder {
        TextView date, order, email, total, status;

        MyviewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.admin_order_date);
            order = itemView.findViewById(R.id.admin_order_number);
            //email = itemView.findViewById(R.id.admin_order_email);
            total = itemView.findViewById(R.id.admin_order_total);
            //status = itemView.findViewById(R.id.admin_order_status);
        }
    }

}
