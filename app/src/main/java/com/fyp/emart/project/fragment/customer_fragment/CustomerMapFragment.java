package com.fyp.emart.project.fragment.customer_fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
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
import com.fyp.emart.project.activity.CartActivity;
import com.fyp.emart.project.activity.LoginActivity;
import com.fyp.emart.project.activity.ProductActivity;
import com.fyp.emart.project.model.MartLocationList;
import com.fyp.emart.project.model.MartProfileList;
import com.fyp.emart.project.model.ProductList;
import com.fyp.emart.project.utils.SaveSharedPreference;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CustomerMapFragment extends BaseFragment implements OnMapReadyCallback, LocationListener, View.OnClickListener {


    private static int cart_count = 0;

    private GoogleMap mMap;
    public static final String URL = UtilsApi.BASE_URL_API + "FypProject/Emart/location.php";
    private JSONArray result;
    private Button mBtnproduct;
    List<MartLocationList> martLocationLists;
    ProgressDialog progressDialog;

    Context mContext;
    BaseApiService mApiService;
    String markerid;
    private ImageView mLogout;

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

        mLogout = (ImageView) view.findViewById(R.id.logout);
        mLogout.setOnClickListener(this);


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
        //getMarkerRetrofit();
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Triggered when user click any marker on the map
              /*  if(marker.getSnippet())
                    Toast.makeText(getActivity().getApplicationContext(), "Hamdy", Toast.LENGTH_SHORT).show();*/
                markerid = marker.getSnippet();
                //Toast.makeText(getActivity().getApplicationContext(), markerid, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    public void getMarkerRetrofit() {
        mApiService.getMartsLocation()
                .enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, retrofit2.Response<JSONObject> response) {
                        if (response.isSuccessful()) {
                            Log.d("JSONResult", response.toString());
                            JSONObject j = null;
                            try {
                                j = response.body();
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
                                            .title(name).snippet(martid)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.8607, 67.0011), 6.0f));


                                }

                            } catch (NullPointerException e) {
                                e.printStackTrace();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        Log.e("checkerror", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(getActivity(), "network failure :( inform the user and possibly retry\n" + t.toString(), Toast.LENGTH_SHORT).show();
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
                                .title(name).snippet(martid)
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnproduct:
                Bundle b = new Bundle();
                Intent i = new Intent(getActivity(), ProductActivity.class);
                b.putString("id", markerid);
                i.putExtras(b);
                startActivity(i);

                break;

            case R.id.logout:
                logout();
                break;
            default:
                break;
        }
    }

    public void  logout(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to logout?");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SaveSharedPreference.setLoggedIn(getActivity(), false);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
