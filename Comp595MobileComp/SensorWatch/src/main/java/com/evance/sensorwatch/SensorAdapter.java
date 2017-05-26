package com.evance.sensorwatch;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class SensorAdapter extends ArrayAdapter<Sensor> {

    private final Context context;
    private final List<Sensor> sensorList;

    public SensorAdapter (Context context, List<Sensor> sensorList){
        super(context, 0, sensorList);
        this.context = context;
        this.sensorList = sensorList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.sensor_text_view, parent, false);
            holder = new ViewHolder();
            holder.sensorData = (TextView) view.findViewById(R.id.sensor_list_text_view);
            view.setTag(holder);
        }
        else
            holder = (ViewHolder) view.getTag();

        holder.sensorData.setText(sensorList.get(position).getName());

        return view;
    }

    private static class ViewHolder {

        private TextView sensorData;

    }

}


