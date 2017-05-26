package com.evance.sensorwatch;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class GyroSensor extends Activity implements SensorEventListener{

    private SensorManager gyroSensorManager;
    private Sensor gyroSensor;
    private float xmin, xmax, ymin, ymax, zmin, zmax;



    public GyroSensor(){
        gyroSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = gyroSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        xmin = xmax = ymin = ymax = zmin = zmax = 0;//is this bad form?
    }

    protected void startListener(){
        gyroSensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    protected void onResume() {
        super.onResume();
        gyroSensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        gyroSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float xaxis = event.values[0];
        float yaxis = event.values[1];
        float zaxis = event.values[2];

        //update min and max values
        if (xaxis < xmin)
            xmin = xaxis;
        if (xaxis > xmax)
            xmax = xaxis;
        if (yaxis < ymin)
            ymin = yaxis;
        if (yaxis > ymax)
            ymax = yaxis;
        if (zaxis < zmin)
            zmin = zaxis;
        if (zaxis > zmax)
            zmax = zaxis;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public float [] getGyroMinMax(){
        float [] minMaxArr = {xmin, xmax, ymin, ymax, zmin, zmax};//can this be done differently
        return minMaxArr;
    }
}
