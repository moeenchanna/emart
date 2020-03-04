package com.fyp.emart.project.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fyp.emart.project.R;

public class AdminOrderAdapter {

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvmartname;
        ImageView image;

        public MyviewHolder(View itemView) {
            super(itemView);
            tvmartname = (TextView)itemView.findViewById(R.id.martname);
            image = (ImageView)itemView.findViewById(R.id.image);
        }
    }

}
