package com.fyp.emart.project.fragment.customer_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fyp.emart.project.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class CustomerProfileFragment extends Fragment {

    private TextView mTvname;
    private TextView mTvemail;
    private TextView mTvmobile;
    private TextView mTvaddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        mTvname = (TextView) view.findViewById(R.id.tvname);
        mTvemail = (TextView) view.findViewById(R.id.tvemail);
        mTvmobile = (TextView) view.findViewById(R.id.tvmobile);
        mTvaddress = (TextView) view.findViewById(R.id.tvaddress);
    }
}
