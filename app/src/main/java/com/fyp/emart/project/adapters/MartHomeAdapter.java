package com.fyp.emart.project.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fyp.emart.project.R;
import com.fyp.emart.project.model.AdminOrderModel;
import com.fyp.emart.project.model.AdminOrderModel;
import com.fyp.emart.project.model.MartList;

import java.util.List;

public class MartHomeAdapter extends RecyclerView.Adapter<MartHomeAdapter.MyviewHolder>{

    private List<AdminOrderModel> adminOrderList;
    private Context context;

    public MartHomeAdapter(List<AdminOrderModel> adminOrderList, Context context) {
        this.adminOrderList = adminOrderList;
        this.context = context;
    }

    public void setOrderList(List<AdminOrderModel> adminOrders) {
        this.adminOrderList = adminOrders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MartHomeAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recylerview_mart_orderlist_layout, parent, false);


        return new MartHomeAdapter.MyviewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MartHomeAdapter.MyviewHolder holder, int position) {

        final AdminOrderModel order = adminOrderList.get(position);

        String id = order.getStatusid();

        holder.date.setText(order.getDatetime());
        holder.order.setText(order.getOrderno());
        holder.email.setText(order.getCustemail());
        holder.total.setText("Rs: "+order.getSubtotal());
        holder.status.setText(order.getStatus());

        if (id.equals("0")) {
            holder.status.setTextColor(Color.RED);// on hold
            Toast.makeText(context, "0", Toast.LENGTH_SHORT).show();
            return;
        }
        if (id.equals("1")) {
            holder.status.setTextColor(Color.YELLOW);// in process
            Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
            return;
        }
        if (id.equals("2")) {
            holder.status.setTextColor(Color.GREEN);// delievered success
            Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
        }



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
            date = itemView.findViewById(R.id.mart_order_date);
            order = itemView.findViewById(R.id.mart_order_number);
            name = itemView.findViewById(R.id.mart_order_name);
            email = itemView.findViewById(R.id.mart_order_email);
            total = itemView.findViewById(R.id.mart_order_total);
            status = itemView.findViewById(R.id.mart_order_status);
        }
    }

}
