package com.fyp.emart.project.fragment.customer_fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.fyp.emart.project.utils.SaveSharedPreference;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class CustomerMapFragment extends BaseFragment
        implements
        OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener ,
        View.OnClickListener {

    private GoogleApiClient googleApiClient;

    private static final int LOCATION_REQUEST_CODE = 101;

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

    private SeekBar seekBarAreaCover;
    private TextView tvProgress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mart_map, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mLogout = (ImageView) view.findViewById(R.id.logout);
        mLogout.setOnClickListener(this);


        cart_count = cartCount();

        mBtnproduct = (Button) view.findViewById(R.id.btnproduct);
        mBtnproduct.setOnClickListener(this);


        mApiService = UtilsApi.getAPIService();
        mContext = getActivity();

        seekBarAreaCover= view.findViewById(R.id.seekBar);
        tvProgress = view.findViewById(R.id.count);




    }

    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        checkLocationandAddToMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                         //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(24.8607, 67.0011), 6.0f));
                        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lon), 14));


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


        int socketTimeout = 10000;//10 sec
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }



    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();

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
                Bundle b = new Bundle();// pass data in any activity
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

    public void logout() {
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


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void checkLocationandAddToMap() {
        //Checking if the user has granted the permission
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Requesting the Location permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        //Fetching the last known location using the Fus
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        //MarkerOptions are used to create a new Marker.You can specify location, title etc with MarkerOptions
        //MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("You are Here");

        //Adding the created the marker on the map
        //mMap.addMarker(markerOptions);

        final CircleOptions circle = new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(40.0)
                .strokeColor(Color.RED)
                .fillColor(0x300000FF);//Transparent Blue color
        mMap.addCircle(circle);

        seekBarAreaCover.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress = progress;
                //Toast.makeText(getActivity(),"SeekBar Progress Change",Toast.LENGTH_SHORT).show();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getActivity(),"SeekBar Touch Started",Toast.LENGTH_SHORT).show();
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                //  tvProgress.setText("KM: " + seekBarProgress + " / " + seekBar.getMax());
                switch (seekBarProgress)
                {
                    //Radius calculate refrence link
                    // https://www.calcmaps.com/map-radius/
                    case 0:
                        mMap.clear();
                        getMartMarkers();
                        tvProgress.setText("1 KM");
                        circle.radius(1003.62);
                        mMap.addCircle(circle);
                        break;
                    case 1:
                        mMap.clear();
                        getMartMarkers();
                        tvProgress.setText("2 KM");
                        circle.radius(2001.47);
                        mMap.addCircle(circle);
                        break;
                    case 2:
                        mMap.clear();
                        getMartMarkers();
                        tvProgress.setText("3 KM");
                        circle.radius(2997.59);
                        mMap.addCircle(circle);
                        break;
                    case 3:
                        mMap.clear();
                        getMartMarkers();
                        tvProgress.setText("5 KM");
                        circle.radius(4996.16);
                        mMap.addCircle(circle);
                        break;
                    case 4:
                        mMap.clear();
                        getMartMarkers();
                        tvProgress.setText("7 KM");
                        circle.radius(7005.88);
                        mMap.addCircle(circle);
                        break;
                    case 5:
                        mMap.clear();
                        getMartMarkers();
                        tvProgress.setText("10 KM");
                        circle.radius(10005.47);
                        mMap.addCircle(circle);
                        break;
                    case 6:
                        mMap.clear();
                        getMartMarkers();
                        tvProgress.setText("12 KM");
                        circle.radius(12012.13 );
                        mMap.addCircle(circle);
                        break;
                }
                //Toast.makeText(getActivity(), "SeekBar Touch Stop ", Toast.LENGTH_SHORT).show();
            }

        });



        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
    }
}
