package com.fyp.emart.project.fragment.customer_fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fyp.emart.project.Api.BaseApiService;
import com.fyp.emart.project.Api.UtilsApi;
import com.fyp.emart.project.BaseFragment;
import com.fyp.emart.project.R;
import com.fyp.emart.project.activity.LoginActivity;
import com.fyp.emart.project.activity.ProductActivity;
import com.fyp.emart.project.model.MartLocationList;
import com.fyp.emart.project.model.ProductList;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;

public class MartMapFragment extends BaseFragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener, View.OnClickListener {


    private static int cart_count = 0;

    private GoogleMap mMap;
    public static final String URL = UtilsApi.BASE_URL_API + "FypProject/Emart/location.php";
    private JSONArray result;
    private Button mBtnproduct;
    List<MartLocationList> martLocationLists;
    ProgressDialog progressDialog;

    Context mContext;
    BaseApiService mApiService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mart_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        cart_count = cartCount();

        mBtnproduct = (Button) view.findViewById(R.id.btnproduct);
        mBtnproduct.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();
    }

    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading please wait...");
        getMartMarkers();
      //  getMartMarkersNew();
    }

    private void getMartMarkersNew() {
        progressDialog.show();
        Call<List<MartLocationList>> productlistCall = mApiService.getMartsLocation();
        productlistCall.enqueue(new Callback<List<MartLocationList>>() {
            @Override
            public void onResponse(Call<List<MartLocationList>> call, retrofit2.Response<List<MartLocationList>> response) {
                progressDialog.dismiss();
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(jsonresponse);
                            if(obj.optString("status").equals("true")){

                                ArrayList<MartLocationList> retroModelArrayList = new ArrayList<>();
                                JSONArray dataArray  = obj.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    MartLocationList retroModel = new MartLocationList();
                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    retroModel.setId(dataobj.getString("0"));
                                    retroModel.setName(dataobj.getString("1"));
                                    retroModel.setAddress(dataobj.getString("5"));
                                    retroModel.setLatitude(dataobj.getString("6"));
                                    retroModel.setLongitude(dataobj.getString("7"));

                                    retroModelArrayList.add(retroModel);

                                }

                                for (int j = 0; j < retroModelArrayList.size(); j++){

                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(Double.parseDouble(retroModelArrayList.get(j).getLatitude()), Double.parseDouble(retroModelArrayList.get(j).getLongitude())))
                                            .title(retroModelArrayList.get(j).getName()).snippet(retroModelArrayList.get(j).getAddress())
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))


                                    );


                                }


                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.8607, 67.0011), 6.0f));



                            }else {
                                Toast.makeText(getActivity(), obj.optString("message")+"", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            @Override
            public void onFailure(Call<List<MartLocationList>> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("Error", t.getMessage());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMartMarkers() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("JSONResult", response.toString());
                JSONObject j = null;
                try {
                    j = new JSONObject(response);
                    result = j.getJSONArray("FL");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jsonObject1 = result.getJSONObject(i);


                        final String name = jsonObject1.getString("1");
                        String address = jsonObject1.getString("5");
                        String lat_i = jsonObject1.getString("6");
                        String long_i = jsonObject1.getString("7");
                        final String martid = jsonObject1.getString("0");

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(lat_i), Double.parseDouble(long_i)))
                                .title(name).snippet(address)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));



                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.8607, 67.0011), 6.0f));


                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnproduct:
                Bundle b = new Bundle();
                Intent i = new Intent(getActivity(), ProductActivity.class);
                b.putString("id","2");
                i.putExtras(b);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
