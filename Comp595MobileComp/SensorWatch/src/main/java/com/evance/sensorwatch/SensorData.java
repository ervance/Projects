package com.evance.sensorwatch;


import android.hardware.SensorEvent;
import android.hardware.Sensor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

//holds sensor data
class SensorData {

    private Sensor sensor;
    private int sensorType;

    private float [] minMaxValues;

    private float xAxis, yAxis, zAxis, xMin, xMax, yMin, yMax, zMin, zMax, singleValue, minSingle, maxSingle;
    public SensorData(Sensor sensor){
        this.sensor = sensor;
        sensorType = sensor.getType();
        xMin = xMax = yMin = yMax = zMin = zMax = xAxis = yAxis = zAxis = singleValue = minSingle = maxSingle = 0;
        minMaxValues = null;//not available yet

    }

    public SensorData(Sensor sensor, float [] minMaxValues){//this will be used to pass through the intent
        this.sensor = sensor;
        this.minMaxValues = minMaxValues;
        xAxis = yAxis = zAxis = 0;
        //Log.d("DEBUG", "minMaxLength for " + sensor.getName() + " " + minMaxValues.length);
        if (minMaxValues != null && minMaxValues.length == 3){
            minSingle = minMaxValues[0];
            maxSingle = minMaxValues[1];
        }
        else if(minMaxValues != null && minMaxValues.length == 7) {
            xMin = minMaxValues[0];
            xMax = minMaxValues[1];
            yMin = minMaxValues[2];
            yMax = minMaxValues[3];
            zMin = minMaxValues[4];
            zMax = minMaxValues[5];
        }

    }

    public void updateData(SensorEvent event){

        /*  NAME                            VALUE COUNT TYPE NUMBER
        *   Accelerometer                   - 3 values      1
            Linear Accelerometer            - 3 values      10
            Gravity Sensor                  - 3             9
            Gyroscope                       - 3 values      4
            Ambient light sensor            - 1 value       5
            Ambient magnetic field sensor   - 3 values      2
            Proximity sensor                - 1 value       8
            Pressure sensor (barometer)     - 1 value       6
            Ambient temperature sensor      - 1 value       13
        * */

        float [] values = event.values;
        int numValues = event.values.length;


        if(numValues == 3){
            xAxis = values[0];
            yAxis = values[1];
            zAxis = values[2];
        }
        else if (numValues == 1){//for barometer etc.
            singleValue = values[0];
        }

        //first half of the array holds the min second half holds the max, last spot is a 0 or 1 to denote updated values or not
        if(minMaxValues == null)
            initMinMaxData(numValues);

        updateMinMax(numValues);

    }


    private void updateMinMax(int numValues){

        if(numValues == 3){
            //update min
            if(xAxis < xMin) {
                xMin = xAxis;
                minMaxValues[0] = xAxis;
                minMaxValues[6] = 1;
            }
            if(xAxis > xMax) {
                xMax = xAxis;
                minMaxValues[1] = xAxis;
                minMaxValues[6] = 1;
            }
            if(yAxis < yMin) {
                yMin = yAxis;
                minMaxValues[2] = yAxis;
                minMaxValues[6] = 1;
            }
            if(yAxis > yMax) {
                yMax = yAxis;
                minMaxValues[3] = yAxis;
                minMaxValues[6] = 1;
            }
            if(zAxis < zMin) {
                zMin = zAxis;
                minMaxValues[4] = zAxis;
                minMaxValues[6] = 1;
            }
            //update max
            if(zAxis > zMax) {
                zMax = zAxis;
                minMaxValues[5] = zAxis;
                minMaxValues[6] = 1;
            }
        }
        else if(numValues == 1){
            //update min
            if(singleValue < minSingle) {
                minSingle = singleValue;
                minMaxValues[0] = singleValue;
                minMaxValues[2] = 1;
            }
            if(singleValue > maxSingle) {
                maxSingle = singleValue;
                minMaxValues[1] = singleValue;
                minMaxValues[2] = 1;
            }
        }

    }

    private void initMinMaxData(int numValues){

        minMaxValues = new float [(numValues * 2) + 1];
        //set all min max to the current value
        //initialize the min and max to be more accurate if min not below 0.0
        if (minMaxValues.length == 7) {
            xMin = xMax = xAxis;
            yMin = yMax = yAxis;
            zMin = zMax = zAxis;
            minMaxValues[0] = xMin;
            minMaxValues[1] = xMax;
            minMaxValues[2] = yMin;
            minMaxValues[3] = yMax;
            minMaxValues[4] = zMin;
            minMaxValues[5] = zMax;
            minMaxValues[6] = 1;//set flag to show it was changed
        }
        else if (minMaxValues.length == 3){
            minSingle = maxSingle = singleValue;
            minMaxValues[0] = minSingle;
            minMaxValues[1] = maxSingle;
            minMaxValues[2] = 1;//set flag to show it was changed
        }

    }

    public Sensor getSensor(){
        return sensor;
    }


    public float[] getMinMaxValues() {
        return minMaxValues;
    }

    public float getxAxis() {
        return xAxis;
    }

    public float getyAxis() {
        return yAxis;
    }

    public float getzAxis() {
        return zAxis;
    }

    public float getxMin() {
        return xMin;
    }

    public float getxMax() {
        return xMax;
    }

    public float getyMin() {
        return yMin;
    }

    public float getyMax() {
        return yMax;
    }

    public float getzMin() {
        return zMin;
    }

    public float getzMax() {
        return zMax;
    }

    public float getSingleValue() {
        return singleValue;
    }

    public float getMinSingle() {
        return minSingle;
    }

    public float getMaxSingle() {
        return maxSingle;
    }

}
