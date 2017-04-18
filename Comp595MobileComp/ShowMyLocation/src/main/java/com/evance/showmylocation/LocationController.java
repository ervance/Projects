package com.evance.showmylocation;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class LocationController extends Activity {

    private TextView latitude, longitude, altitude, location_data, min_location, max_location, pressure;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    private Sensor pressureSensor, tempSensor;
    private SensorEventListener sensorEventListener, sensorAltListener;
    private LocationListener locationListener, gpsListener;
    private int permissionCheck;
    private DecimalFormat dFormat = new DecimalFormat("#.###");
    private float lightMin, lightMax, barAlt, light;
    private Location mLocation;
    private List<Address> addresses;
    private boolean initiateMinMax = true;
    private boolean locationChange = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_controller_activity);


        //must have reverse geo location also
        //Latitude
        latitude = (TextView) findViewById(R.id.latitude_data);
        //Longitude
        longitude = (TextView) findViewById(R.id.longitude_data);
        //Altitude
        altitude = (TextView) findViewById(R.id.altitude_data);
        //Location Name
        location_data = (TextView) findViewById(R.id.location_data);
        //min and max
        min_location = (TextView) findViewById(R.id.min_and_location);
        max_location = (TextView) findViewById(R.id.max_and_location);


        pressure = (TextView) findViewById(R.id.pressure);



        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Geocoder geocoder = new Geocoder(LocationController.this, Locale.getDefault());
                locationChange = true;
                if (location != null) {
                    double lat, lng;
                    mLocation = location;
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    latitude.setText(String.valueOf(dFormat.format(lat)));
                    longitude.setText(String.valueOf(dFormat.format(lng)));
                    altitude.setText(String.valueOf(barAlt) + "ft");

                    try {
                        addresses = geocoder.getFromLocation(lat, lng, 1);
                        if(addresses != null){
                            location_data.setText(addresses.get(0).getAddressLine(0));
                        }
                        Log.d("ADDRESS", "" + addresses.get(0).getLocality());
                    }
                    catch (IOException e){
                        Log.e("IOEXCEPTION", e.toString());
                    }


                } else {
                    Toast.makeText(LocationController.this, "Location not known", Toast.LENGTH_LONG).show();
                    latitude.setText("Unknown");
                    longitude.setText("Unknown");
                }
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
        };


        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Geocoder geocoder = new Geocoder(LocationController.this, Locale.getDefault());
                    if (initiateMinMax) {
                        initiateMinMax = false;
                        light = event.values[0];
                        lightMin = event.values[0];
                        lightMax = event.values[0];
                        if(mLocation != null) {
                            try {
                                addresses = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                                if(addresses != null){
                                    location_data.setText(addresses.get(0).getLocality());
                                    min_location.setText("Min: " + lightMin + " Lat: " + dFormat.format(mLocation.getLatitude())
                                            + " Long: " + dFormat.format(mLocation.getLongitude())+ " Altitude: " + barAlt
                                            + " Location: " + addresses.get(0).getAddressLine(0));
                                    max_location.setText("Max " + lightMax + " Lat: " + dFormat.format(mLocation.getLatitude())
                                            + " Long: " + dFormat.format(mLocation.getLongitude()) + " Altitude: " + barAlt
                                            + " Location: " + addresses.get(0).getAddressLine(0));
                                }
                                Log.d("ADDRESS", "" + addresses.get(0).getLocality());
                            }
                            catch (IOException e){
                                Log.e("IOEXCEPTION", e.toString());
                            }

                        }
                        else{
                            min_location.setText("Min: " + lightMin + " Lat:  n/a  Long: n/a Altitude: n/a Location: n/a");
                            max_location.setText("Max: " + lightMax + " Lat:  n/a  Long: n/a Altitude: n/a Location: n/a");
                            initiateMinMax = false;
                        }
                        light = event.values[0];
                        pressure.setText("Light (lux): " + String.valueOf(light));
                    }
                    else {
                        light = event.values[0];
                        pressure.setText("Light (lux): " + String.valueOf(light));
                        updateMinMax(light);
                    }
                }


            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorAltListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                barAlt = event.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };


        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        permissionCheck = ContextCompat.checkSelfPermission(LocationController.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        sensorManager.registerListener(sensorAltListener, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onResume() {//register listener when activity resumes
        super.onResume();
        sensorManager.registerListener(sensorEventListener, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);

        permissionCheck = ContextCompat.checkSelfPermission(LocationController.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    protected void onPause() {//unregister listener to save battery when activity pauses
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener, pressureSensor);
        permissionCheck = ContextCompat.checkSelfPermission(LocationController.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED)
            locationManager.removeUpdates(locationListener);

    }

    private void updateMinMax(float value){

        if(value < lightMin) {
            lightMin = value;
            if (mLocation != null)
                min_location.setText("Min: " + lightMin + " Lat: " + dFormat.format(mLocation.getLatitude()) + " Long: " + dFormat.format(mLocation.getLongitude())
                        + " Altitude: " + barAlt + " Location: " + addresses.get(0).getAddressLine(0));
            else
                min_location.setText("Min: " + lightMin + " Lat:  n/a  Long: n/a Altitude: n/a Location: n/a");
        }
        if(value > lightMax) {
            lightMax = value;
            if (mLocation != null)
                max_location.setText("Max " + lightMax + " Lat: " + dFormat.format(mLocation.getLatitude()) + " Long: " + dFormat.format(mLocation.getLongitude())
                        + " Altitude: " + barAlt + " Location: " + addresses.get(0).getAddressLine(0));
            else
                max_location.setText("Max: " + lightMax + " Lat:  n/a  Long: n/a Altitude: n/a Location: n/a");
        }

    }
}
