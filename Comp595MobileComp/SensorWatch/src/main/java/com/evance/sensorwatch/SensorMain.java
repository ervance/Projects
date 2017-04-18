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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;//for the onClickListener

import java.util.ArrayList;
import java.util.List;

public class SensorMain extends Activity implements SensorEventListener{

    private SensorManager sensorManager;
    private List<Sensor> availableSensors;
    private final List<Sensor> SENSOR_LIST = new ArrayList<>();
    private final List<SensorData> SENSORDATA_LIST = new ArrayList<>();


    private RelativeLayout sensorLayout;
    private ListView listView;
    private SensorAdapter sensorAdapter;
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

    private ArrayList<float []> sensorValues = new ArrayList<>();//hold values for sensors

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_list_view);

        sensorLayout = (RelativeLayout) findViewById(R.id.sensor_main_relative);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        availableSensors= sensorManager.getSensorList(Sensor.TYPE_ALL);



        for (Sensor sensor : availableSensors)
        {//Start all sensors listening
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                SENSOR_LIST.add(sensor);
                SENSORDATA_LIST.add(new SensorData(sensor));
        }

        listView = (ListView) sensorLayout.findViewById(R.id.sensor_list_view);
        sensorAdapter = new SensorAdapter(SensorMain.this, SENSOR_LIST);
        listView.setAdapter(sensorAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                float [] minMaxArr = SENSORDATA_LIST.get(position).getMinMaxValues();//send sensory min max info
                int [] sensorType = new int [1];
                sensorType[0] = SENSORDATA_LIST.get(position).getSensor().getType();//send sensor type

                Intent intent = new Intent(SensorMain.this, SensorDataActivity.class);

                intent.putExtra("MIN_MAX_ARRAY", minMaxArr);
                intent.putExtra("SENSOR_TYPE", sensorType);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {//register listener when activity resumes
        super.onResume();
        for (Sensor sensor : SENSOR_LIST){
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(sensor.getType()), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    protected void onPause() {//unregister listener to save battery when activity pauses
        super.onPause();
        for (Sensor sensor : SENSOR_LIST){
            sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(sensor.getType()));
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int eventSensorType, sensorType;
        eventSensorType = event.sensor.getType();
        for (SensorData sensorData: SENSORDATA_LIST)
        {
            sensorType = sensorData.getSensor().getType();
            if(eventSensorType == sensorType)
                sensorData.updateData(event);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private boolean hasSensor(int type){
        //returns true if the phone has that type of sensor
        return type == 1 || type == 10 || type == 9 || type == 4 || type == 5 || type == 2 || type == 8 || type == 13 || type == 6;
    }


}

