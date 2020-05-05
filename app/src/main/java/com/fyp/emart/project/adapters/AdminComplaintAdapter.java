package com.fyp.emart.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.emart.project.R;
import com.fyp.emart.project.model.ComplaintList;

import java.util.List;

public class AdminComplaintAdapter extends RecyclerView.Adapter<AdminComplaintAdapter.MyviewHolder> {

    private List<ComplaintList> complaintLists;
    private Context context;

    public AdminComplaintAdapter(List<ComplaintList> complaintLists, Context context) {
        this.complaintLists = complaintLists;
        this.context = context;
    }

    public void setComplaintLists(List<ComplaintList> complaintLists) {
        this.complaintLists = complaintLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_complain_layout, parent, false);


        return new AdminComplaintAdapter.MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        final ComplaintList complaint = complaintLists.get(position);
        String id = complaint.getId();

        holder.time.setText("Date Time: "+complaint.getDatetime());
        holder.martname.setText("Mart Name: "+complaint.getMartname());
        holder.customername.setText("Customer Name: "+complaint.getCustname());
        holder.detail.setText("Complaint: "+complaint.getDetail());

    }

    @Override
    public int getItemCount() {
        if (complaintLists != null) {
            return complaintLists.size();
        }
        return 0;
    }

    static class MyviewHolder extends RecyclerView.ViewHolder {
        TextView time, detail,martname,customername;

        MyviewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.complain_time);
            detail = itemView.findViewById(R.id.complain_detail);

            martname = itemView.findViewById(R.id.complaints_mart_name);
            customername = itemView.findViewById(R.id.complaints_customer_name);
        }
    }
}
