package com.ecolemultimedia.openstore.UI;

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ecolemultimedia.openstore.Model.Store;
import com.ecolemultimedia.openstore.Model.Stores;
import com.ecolemultimedia.openstore.R;
import com.ecolemultimedia.openstore.Utils.Network;
import com.ecolemultimedia.openstore.Utils.OnMapAndViewReadyListener;
import com.ecolemultimedia.openstore.Utils.PermissionUtils;
import com.ecolemultimedia.openstore.Utils.Preference;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * This shows how to create a simple activity with a map and a marker on the map.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,OnMarkerClickListener, OnMapClickListener,OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private static final String TAG = "Map";
    private Marker mSelectedMarker;
    private EditText editCategorie;
    public List<Store> storeList = new ArrayList<>();
    private List<Store> showStoreList = new ArrayList<>();
    private ArrayAdapter<Store> adapter;
    private List<Marker> markers = new ArrayList<Marker>();
    private String storeName;
    private String storeLat;
    private String storeLong;
    private Button btnCategory;
    private String storeCategory;
    private GoogleApiClient googleApiClient;
    private View search;
    private double myLongitude;
    private double myLatitude;
    protected LocationRequest mLocationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //ActionBar actionBar = getActionBar();
        //actionBar.setIcon(R.drawable.search);
        editCategorie = (EditText) findViewById(R.id.EditCategorie);
        btnCategory = (Button) findViewById(R.id.btnCategory);

        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valeur = editCategorie.getText().toString();
                addMarkersFromCategory(myLatitude, myLongitude, valeur);
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        findViewById(R.id.blockSearch).setVisibility(View.GONE);
       // listViewStore = (ListView) findViewById(R.id.listViewStore);
        //adapter
        //adapter = new ArrayAdapter<Store>(MapActivity.this,android.R.layout.simple_list_item_1, storeList);
        //listViewStore.setAdapter(adapter);
        // GOOGLE MAP
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        new OnMapAndViewReadyListener(mapFragment, this);
        createLocationRequest();



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                int visibility = findViewById(R.id.blockSearch).getVisibility();
                if (visibility == 8){
                    findViewById(R.id.blockSearch).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.blockSearch).setVisibility(View.GONE);
                }


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        googleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Hide the zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.


        // Set listener for marker click event.  See the bottom of this class for its behavior.
        mMap.setOnMarkerClickListener(this);

        // Set listener for map click event.  See the bottom of this class for its behavior.
        mMap.setOnMapClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localized.
        map.setContentDescription("Demo showing how to close the info window when the currently"
                + " selected marker is re-tapped.");
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.equals(mSelectedMarker)) {
                    String markerId = mSelectedMarker.getId().substring(1);
                    Store item = showStoreList.get(Integer.parseInt(markerId));
                    Preference.setPrefName(MapActivity.this, item.getName());
                    Preference.setPrefDay(MapActivity.this, item.getDayClosed());
                    Preference.setPrefImage(MapActivity.this, item.getImage());
                    Preference.setPrefStartHour(MapActivity.this, item.getStartHour());
                    Preference.setPrefEndHour(MapActivity.this, item.getEndHour());
                    Preference.setPrefDescription(MapActivity.this, item.getDescription());
                    //Log.e(TAG, "Map: "+ item.getDescription());
                    // The showing info window has already been closed - that's the first thing to happen
                    // when any marker is clicked.
                    // Return true to indicate we have consumed the event and that we do not want the
                    // the default behavior to occur (which is for the camera to move such that the
                    // marker is centered and for the marker's info window to open, if it has one).
                    mSelectedMarker = null;
                    Intent intent = new Intent(MapActivity.this, DetailActivity.class);
                    startActivity(intent);
                }

                mSelectedMarker = marker;

            }
        });
    }
    private boolean checkReady() {
        if (mMap == null) {
          //  Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(30000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    }
    private void updateMyLocation() {
        if (!checkReady()) {
            return;
        }
        // Enable the location layer. Request the location permission if needed.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, MapActivity.this);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DARWIN, 10f));
    }
    private void addMarkersFromCategory(double myLat, double myLong, String valeur){
        mMap.clear();
        showStoreList.clear();
        DateTime dt = new DateTime();  // current time
        int hours = dt.getHourOfDay(); // gets hour of day
        for (int i = 0; i < storeList.size(); i++) {
            Store item = storeList.get(i);
            storeLat = item.getLat();
            double lat = Double.parseDouble(storeLat);
            storeLong = item.getLng();
            double lng = Double.parseDouble(storeLong);
            storeName = item.getName();
            storeCategory = item.getCategory();
            double distance =  distance(myLat, myLong, lat, lng);
            if (distance < 20000 && item.getStartHour() < hours && hours < item.getEndHour() && valeur.equalsIgnoreCase(storeCategory)){
                showStoreList.add(item);
                LatLng latLng = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(storeName)
                        .snippet("Cliquer pour plus d'infos"));
            } else if(valeur.isEmpty() && distance < 5000 && item.getStartHour() < hours && hours < item.getEndHour()){
                showStoreList.add(item);
                LatLng latLng = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(storeName)
                        .snippet("Cliquer pour plus d'infos"));
            }

        }
    }
    private void addMarkersToMap(double myLat, double myLong) {
        DateTime dt = new DateTime();  // current time
        int hours = dt.getHourOfDay(); // gets hour of day
        for (int i = 0; i < storeList.size(); i++) {
            Store item = storeList.get(i);
            storeLat = item.getLat();
            double lat = Double.parseDouble(storeLat);
            storeLong = item.getLng();
            double lng = Double.parseDouble(storeLong);
            storeName = item.getName();
            double distance =  distance(myLat, myLong, lat, lng);
            if (distance < 5000 && item.getStartHour() < hours && hours < item.getEndHour()){
                showStoreList.add(item);
                LatLng latLng = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(storeName)
                        .snippet("Cliquer pour plus d'infos"));
            }

        }
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, MapActivity.this);
    }
    public static float distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }
    @Override
    public void onMapClick(final LatLng point) {
        // Any showing info window closes when the map is clicked.
        // Clear the currently selected marker.
        mSelectedMarker = null;

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // The user has re-tapped on the marker which was already showing an info window.
        if (marker.equals(mSelectedMarker)) {
            String markerId = mSelectedMarker.getId().substring(1);
            Store item = showStoreList.get(Integer.parseInt(markerId));
            Preference.setPrefName(MapActivity.this, item.getName());
            Preference.setPrefDay(MapActivity.this, item.getDayClosed());
            Preference.setPrefImage(MapActivity.this, item.getImage());
            Preference.setPrefStartHour(MapActivity.this, item.getStartHour());
            Preference.setPrefEndHour(MapActivity.this, item.getEndHour());
            Preference.setPrefDescription(MapActivity.this, item.getDescription());
            // The showing info window has already been closed - that's the first thing to happen
            // when any marker is clicked.
            // Return true to indicate we have consumed the event and that we do not want the
            // the default behavior to occur (which is for the camera to move such that the
            // marker is centered and for the marker's info window to open, if it has one).
            mSelectedMarker = null;
            Intent intent = new Intent(MapActivity.this, DetailActivity.class);
            startActivity(intent);
        }

        mSelectedMarker = marker;

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur.
        return false;
    }

    public void getData(){
        if(Network.isNetworkAvailable(MapActivity.this)){
            // TODO : lancer la requete
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = String.format("http://shazoom.alwaysdata.net/stores.json");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e(TAG, "onResponse: "+ response);
                            Gson gson = new Gson();
                            Stores stores = gson.fromJson(response, Stores.class);
                            Log.e(TAG, "test");
                                // TODO : afficher la liste des Entreprises
                                storeList.clear();
                                storeList.addAll(stores.data);
                            addMarkersToMap(myLatitude, myLongitude);
                               // adapter.notifyDataSetChanged(); // rafraichissement de la listView après recuperation des données
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onErrorResponse: "+ error);
                }
            });
            queue.add(stringRequest);


        }else{
           // FastDialog.showDialog(MapActivity.this, FastDialog.SIMPLE_DIALOG, getString(R.string.dialog_network_error));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "Vous etes connecté", Toast.LENGTH_SHORT).show();
        updateMyLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Vous etes déconnecté", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Vous etes déconnecté", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            //Getting longitude and latitude
            myLongitude = location.getLongitude();
            myLatitude = location.getLatitude();
            //moving the map to location
            LatLng latLng = new LatLng(myLatitude, myLongitude);
            CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(latLng, 13);
            mMap.animateCamera(myLocation);
            getData();

        }
    }

}
