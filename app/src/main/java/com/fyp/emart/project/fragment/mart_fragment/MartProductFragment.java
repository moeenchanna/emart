package com.fyp.emart.project.fragment.mart_fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.fyp.emart.project.R;
import com.google.android.material.textfield.TextInputEditText;

public class MartProductFragment extends Fragment implements View.OnClickListener {


    private TextInputEditText mProductname;
    private TextInputEditText mPriceProduct;
    private TextInputEditText mQuanProduct;
    private TextInputEditText mCodeProduct;

    private Button mButton;

    private static final int STORAGE_PERMISSION_CODE = 123;

    //Uri to store the image uri
    private Uri filePath;

    private static final int IMAGE_SELECT_CODE = 1001;
    private TextInputEditText mImageUrlProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_mart_product, container, false);
        // Inflate the layout for this fragment

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        mProductname = (TextInputEditText) view.findViewById(R.id.productname);
        mPriceProduct = (TextInputEditText) view.findViewById(R.id.product_price);
        mQuanProduct = (TextInputEditText) view.findViewById(R.id.product_quan);
        mCodeProduct = (TextInputEditText) view.findViewById(R.id.product_code);
        mButton = (Button) view.findViewById(R.id.button);
        mButton.setOnClickListener(this);
        mImageUrlProduct = (TextInputEditText) view.findViewById(R.id.pr_image_url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Toast.makeText(getActivity(), "Send", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

//    private void requestStoragePermission() {
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//            return;
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE))
//            requestStoragePermission();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//
//    }

}
