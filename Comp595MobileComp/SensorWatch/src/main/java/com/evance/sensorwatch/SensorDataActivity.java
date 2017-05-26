package com.evance.sensorwatch;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SensorDataActivity extends Activity implements SensorEventListener {

    private SensorData sensorData;
    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView xDataView, yDataView, zDataView, xMinMaxView, yMinMaxView, zMinMaxView, otherDataView, otherMinMaxView, unavailableText;
    float [] intentMinMaxData;
    int [] sensorType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_data_activity);

        Intent intent = getIntent();
        //text views
        TextView sensorLabelView = (TextView) findViewById(R.id.sensor_label);
        unavailableText = (TextView) findViewById(R.id.unavailable_text);
        xDataView = (TextView) findViewById(R.id.x_data);
        yDataView = (TextView) findViewById(R.id.y_data);
        zDataView = (TextView) findViewById(R.id.z_data);
        xMinMaxView = (TextView) findViewById(R.id.x_min_max_data);
        yMinMaxView = (TextView) findViewById(R.id.y_min_max_data);
        zMinMaxView = (TextView) findViewById(R.id.z_min_max_data);
        otherDataView = (TextView) findViewById(R.id.other_raw_data);
        otherMinMaxView = (TextView) findViewById(R.id.other_min_max_data);

        unavailableText.setVisibility(View.INVISIBLE);//start it as invisible

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); //instantiate manager
        //Set text views of old minimum and get the sensor type
        if(intent != null){
            intentMinMaxData = intent.getFloatArrayExtra("MIN_MAX_ARRAY");
            sensorType = intent.getIntArrayExtra("SENSOR_TYPE");
            int type = sensorType[0];
            Log.d("SENSOR TYPE", "" + type);
            sensor = sensorManager.getDefaultSensor(type);
            sensorData = new SensorData(sensor, intentMinMaxData);

            //hide other data category if not needed
            //Other sensor data, ie 1 value, will have a length of 3
            if(sensorData.getMinMaxValues() != null && sensorData.getMinMaxValues().length > 3)
                hideOtherSensorText();
            else if (sensorData.getMinMaxValues() == null)
                sensorUnavailable();

        }
        //exit if intent is null go back to original class
        else{
            Intent exitIntent = new Intent(SensorDataActivity.this, SensorMain.class);
            startActivity(exitIntent);
        }

        sensorLabelView.setText(sensorData.getSensor().getName() + "\nUnits: " + modifySensorView(sensorData.getSensor()));

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        int numValues = event.values.length;
        sensorData.updateData(event);
        float [] minMaxValues = sensorData.getMinMaxValues();
        int minMaxLength = minMaxValues.length;


        if(numValues >= 3){
            xDataView.setText("" + event.values[0]);
            yDataView.setText("" + event.values[1]);
            zDataView.setText("" + event.values[2]);
            if(minMaxValues[minMaxLength-1] == 1){
                xMinMaxView.setText("" + minMaxValues[0] + " / " + minMaxValues[1]);
                yMinMaxView.setText("" + minMaxValues[2] + " / " + minMaxValues[3]);
                zMinMaxView.setText("" + minMaxValues[4] + " / " + minMaxValues[5]);
            }
        }
        else if(numValues == 1){
            otherDataView.setText("" + event.values[0]);
            if(minMaxValues[minMaxLength-1] == 1){
                otherMinMaxView.setText("" + minMaxValues[0]);
            }
        }


    }

    private void hideOtherSensorText(){
        TextView otherLabelData = (TextView) findViewById(R.id.other_raw_data_label);
        TextView otherLabelMinMax = (TextView) findViewById(R.id.other_min_max_label);
        otherLabelData.setVisibility(View.INVISIBLE);
        otherLabelMinMax.setVisibility(View.INVISIBLE);
        otherMinMaxView.setVisibility(View.INVISIBLE);
        otherDataView.setVisibility(View.INVISIBLE);

    }

    private void sensorUnavailable(){
        //Hide all text and set unavailable to Red
        hideOtherSensorText();
        hideLabels();
        unavailableText.setVisibility(View.VISIBLE);
    }

    private void hideLabels(){
        RelativeLayout rawDataContainer = (RelativeLayout) findViewById(R.id.raw_data);
        RelativeLayout minMaxContainer = (RelativeLayout) findViewById(R.id.min_max_data_label);
        rawDataContainer.setVisibility(View.INVISIBLE);
        minMaxContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onResume() {//register listener when activity resumes
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {//unregister listener to save battery when activity pauses
        super.onPause();
        sensorManager.unregisterListener(this, sensor);
    }


    private String modifySensorView(Sensor sensor){
        String units = "";
        switch (sensor.getType()) {

            case Sensor.TYPE_ACCELEROMETER:
                units = "m/s^2";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                units = "uT";
                break;
            case Sensor.TYPE_GYROSCOPE:
                units = "radians/second";
                break;
            case Sensor.TYPE_LIGHT:
                //Ambient Light level in [0]
                units = "lux";
                changeLabels();
                break;
            case Sensor.TYPE_PRESSURE:
                units = "hPa (millibar)";
                changeLabels();
                break;
            case Sensor.TYPE_PROXIMITY:
                changeLabels();
                units = "cm";
                break;
            case Sensor.TYPE_GRAVITY:
                units = "m/s^2";
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                units = "m/s^2";
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                units = "degrees";
                break;
            case Sensor.TYPE_ORIENTATION:
                units = "degrees";
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                changeLabels();
                units = "percent";
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                changeLabels();
                units = "celsius";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                units = "micro-Tesla (uT)";
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                units = "rad/s";
                break;
            default:
                units = "Unknown";
        }

        return units;

    }

    private void changeLabels(){
        TextView x, y, z, zdata, ydata, yMinMax, zMinMax, ymindata, zmindata, xminmaxlabel;
        x = (TextView) findViewById(R.id.x_label);
        y = (TextView) findViewById(R.id.y_label);
        z = (TextView) findViewById(R.id.z_label);
        ydata = (TextView) findViewById(R.id.y_data);
        zdata = (TextView) findViewById(R.id.z_data);
        yMinMax = (TextView) findViewById(R.id.y_min_max_label);
        zMinMax = (TextView) findViewById(R.id.z_min_max_label);
        ymindata = (TextView) findViewById(R.id.y_min_max_data);
        zmindata = (TextView) findViewById(R.id.z_min_max_data);
        xminmaxlabel = (TextView) findViewById(R.id.x_min_max_label);
        x.setVisibility(View.INVISIBLE);
        y.setVisibility(View.INVISIBLE);
        z.setVisibility(View.INVISIBLE);
        ydata.setVisibility(View.INVISIBLE);
        zdata.setVisibility(View.INVISIBLE);
        yMinMax.setVisibility(View.INVISIBLE);
        zMinMax.setVisibility(View.INVISIBLE);
        ymindata.setVisibility(View.INVISIBLE);
        zmindata.setVisibility(View.INVISIBLE);
        xminmaxlabel.setVisibility(View.INVISIBLE);
    }


}
