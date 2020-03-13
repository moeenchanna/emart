package com.fyp.emart.project.fragment.checkout_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.fyp.emart.project.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class AddressFragment extends Fragment implements View.OnClickListener {


    private EditText mNameSa;
    private EditText mEmailSa;
    private EditText mMobileSa;
    private EditText mAddressSa;
    private TextView mPymentTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNameSa = (EditText) view.findViewById(R.id.sa_name);
        mEmailSa = (EditText) view.findViewById(R.id.sa_email);
        mMobileSa = (EditText) view.findViewById(R.id.sa_mobile);
        mAddressSa = (EditText) view.findViewById(R.id.sa_address);
        mPymentTxt = (TextView) view.findViewById(R.id.txt_pyment);
        mPymentTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_pyment:
                // TODO 20/03/13
                break;
            default:
                break;
        }
    }
}
