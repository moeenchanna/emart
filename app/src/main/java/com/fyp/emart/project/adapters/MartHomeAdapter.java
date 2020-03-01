package com.fyp.emart.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fyp.emart.project.R;
import com.fyp.emart.project.model.MartList;

import java.util.List;

public class MartHomeAdapter extends RecyclerView.Adapter<MartHomeAdapter.MyviewHolder>{

    Context context;
    List<MartList> martLists;

    public MartHomeAdapter(Context context, List<MartList> martLists) {
        this.context = context;
        this.martLists = martLists;
    }

    public void setMartList(List<MartList> martList) {
        this.martLists = martList;
        notifyDataSetChanged();
    }

    @Override
    public MartHomeAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recylerview_mart_orderlist_layout,parent,false);
       return new MartHomeAdapter.MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

        holder.tvmartname.setText(martLists.get(position).getMartName());

        RequestOptions options = new RequestOptions()
                .centerInside()
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading);

        String Simage = martLists.get(position).getMartlogo();
        Simage.replace("\\/","");
        Glide.with(context).load(Simage).apply(options).into(holder.image);

    }




    @Override
    public int getItemCount() {
        if(martLists != null){
            return martLists.size();
        }
        return 0;

    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvmartname;
        ImageView image;

        public MyviewHolder(View itemView) {
            super(itemView);
          /*  tvmartname = (TextView)itemView.findViewById(R.id.martname);
            image = (ImageView)itemView.findViewById(R.id.image);*/
        }
    }

}
