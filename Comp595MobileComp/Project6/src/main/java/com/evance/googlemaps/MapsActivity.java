package com.evance.googlemaps;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import static android.R.attr.clickable;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private int permissionCheck;
    private LocationManager locationManager;
    private LatLng currentLocation;
    //for drawing lines
    private ArrayList<LatLng> coorPoints;
    private LatLng previousPoint;
    private Polyline line;
    private GoogleApiClient mGoogleApiClient;
    private SoundMeter soundMeter;
    Circle circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);

        coorPoints = new ArrayList<LatLng>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        soundMeter = new SoundMeter();
        soundMeter.start();

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
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 2000, 10, this);
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 2000, 10, this);
        }

        Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        if (location != null) {
            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            previousPoint = currentLocation;
            gotoLocation(currentLocation);
        }

//        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
//            @Override
//            public void onCircleClick(Circle circle) {
//                Toast.makeText(MapsActivity.this, "Latitude: " + circle.getCenter().latitude + "/nLongitude: " + circle.getCenter().longitude, Toast.LENGTH_LONG);
//            }
//        });

        //drawLine();
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void gotoLocation(LatLng location) {
        currentLocation = location;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    private void drawLine(LatLng currentPoint) {
//        mMap.clear();


        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);

//        for (LatLng point : coorPoints)
//            options.add(point);

        if (previousPoint != null) {
            options.add(previousPoint);
            options.add(currentPoint);
        }
        line = mMap.addPolyline(options); //add the poly line

        //create ground overlay
        //https://developers.google.com/maps/documentation/android-api/groundoverlay
        // Instantiates a new CircleOptions object and defines the center and radius


    }

    private void drawCirlce(LatLng latLng) {

        int[] colorRadius = getColorAndRadius();

        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(colorRadius[1])
                .strokeColor(colorRadius[0])// In meters
                .fillColor(colorRadius[2]);



// Get back the mutable Circle
        circle = mMap.addCircle(circleOptions);



    }

    private int[] getColorAndRadius() {
        int color, amp, radius, fill;
        amp = soundMeter.getAmplitude();
        Log.d("SOUND", "" + amp);
        if (amp <= 12000) {
            color = Color.GREEN;
            radius = 10;
            fill = 0x4D186C34;//green transparent 30%
            Log.d("SOUND", "Low");
        } else if (amp >= 15001 && amp <= 23000) {
            color = Color.YELLOW;
            radius = 12;
            fill = 0x4DDFE71A;
            Log.d("SOUND", "Medium");
        } else {
            color = Color.RED;
            fill = 0x4DDF1616;//red transparent 30%
            radius = 15;
            Log.d("SOUND", "High");
        }
        Log.d("COLOR", "" + color);

        int[] array = {color, radius, fill};

        return array;
    }

    @Override
    public void onLocationChanged(Location location) {
        //// TODO: 2/24/17

        if (location != null) {
            previousPoint = currentLocation;
            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

            gotoLocation(currentLocation);

//            if (addPoint(location)) {
//                drawLine(currentLocation);
//            }
            drawCirlce(currentLocation);
        }
    }

    private boolean addPoint(Location location) {

        Log.d("LOCATION", "" + location);
        Log.d("LOCATION", "lat = " + location.getLatitude() + " long " + location.getLongitude());

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
