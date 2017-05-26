package com.evance.googlemaps;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener{

    private GoogleMap mMap;
    private int permissionCheck;
    private LocationManager locationManager;
    private LatLng currentLocation;
    //for drawing lines
    private ArrayList<LatLng> coorPoints;
    private Polyline line;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);

        coorPoints = new ArrayList<LatLng>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);


        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);

        permissionCheck = ContextCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 2000, 3, this);
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 2000, 3, this);
        }

        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        if (location != null){
            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            gotoLocation(location);
        }

        drawLine();
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void gotoLocation(Location location) {
        double lat, lng;
        lat = location.getLatitude();
        lng = location.getLongitude();
        LatLng latLng = new LatLng(lat, lng);
        currentLocation = latLng;
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void drawLine(){
        mMap.clear();

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);

        for (LatLng point : coorPoints)
            options.add(point);
//        options.add(new LatLng(51.5, -0.1));
//        options.add(new LatLng(40.7, -74.0));

        line = mMap.addPolyline(options); //add the poly line

    }

    @Override
    public void onLocationChanged(Location location) {
        //// TODO: 2/24/17
        if (addPoint(location)) {
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
            coorPoints.add(point);
        }

        drawLine();
        gotoLocation(location);
    }

    private boolean addPoint(Location location){

        double latDiff = Math.abs(currentLocation.latitude - location.getLatitude());
        double lonDiff = Math.abs(currentLocation.longitude - location.getLongitude());

        return latDiff >= 1 || lonDiff >= 1;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
