package com.deviceinfo.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.deviceinfo.R;

import java.util.List;

public class Sensors_Fragment extends Fragment {
    ListView SensorsLV;
    SensorManager sensorManager;
    List<Sensor> sensor;
    String name, vendor, power, type, type_specific;

    SensorManager sm;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sensors_, container, false);

        SensorsLV = view.findViewById(R.id.SensorsLV);

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getSensorList(Sensor.TYPE_ALL);

        SensorsLV.setAdapter(new ArrayAdapter<Sensor>(getContext(), android.R.layout.simple_list_item_1, sensor));

        return view;
    }

}