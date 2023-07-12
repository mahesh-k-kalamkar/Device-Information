package com.deviceinfo.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.deviceinfo.R;

import java.util.ArrayList;

public class Battery_Fragment extends Fragment {
    ListView BatteryInfoLV;
    ArrayList<String> BatteryInfoList = new ArrayList<>();
    String health, powerSource, status, level, voltage, tech, temp;
    boolean unit = false;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_battery_, container, false);

        BatteryInfoLV = view.findViewById(R.id.BatteryInfoLV);

        health = getBatteryHealth();
        level = getBatteryLevel();
        voltage = getVoltage();
        powerSource = getPowerSource();
        tech = getBatteryTechnology();
        temp = getBatteryTemp();
        status = getBatteryStatus();

        BatteryInfoList.add("Battery Health: " + health);
        BatteryInfoList.add("Battery Status: " + status);
        BatteryInfoList.add("Battery Level: " + level);
        BatteryInfoList.add("Battery Voltage: " + voltage);
        BatteryInfoList.add("Charging Source: " + powerSource);
        BatteryInfoList.add("Technology : " + tech);
        BatteryInfoList.add("Temperature: " + temp);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, BatteryInfoList);
        BatteryInfoLV.setAdapter(adapter);

        return view;
    }

    public String getBatteryLevel() {
        int pct = 0;
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatusIntent = getActivity().registerReceiver(null, intentFilter);
        if (batteryStatusIntent != null) {
            int level = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            pct = level * 100 / scale;
        }
        return pct + "%";
    }

    public String getBatteryHealth() {
        String health = null;
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batterystat = getContext().registerReceiver(null, intentFilter);
        int status = 0;
        if (batterystat != null) {
            status = batterystat.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        }
        switch (status) {
            case BatteryManager.BATTERY_HEALTH_COLD:
                health = "Cold";
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD:
                health = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                health = "Good";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                health = "Overvoltage";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                health = "Overheat";
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                health = "Unknown";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                health = "Unspecified Failure";
                break;
        }
        return health;
    }

    public String getPowerSource() {
        String source;
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatusIntent = getActivity().registerReceiver(null, intentFilter);
        int status = 0;
        if (batteryStatusIntent != null) {
            status = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        }
        switch (status) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                source = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                source = "USB Port";
                break;
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                source = "Wireless";
                break;
            default:
                source = "Battery";
                break;
        }
        return source;
    }

    public String getBatteryTechnology() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batterystat = getActivity().registerReceiver(null, intentFilter);
        String tech = null;
        if (batterystat != null) {
            tech = batterystat.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
        }
        return tech;
    }

    @SuppressLint("DefaultLocale")
    public String getBatteryTemp() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batterystat = getActivity().registerReceiver(null, intentFilter);
        float temp = 0;
        if (batterystat != null) {
            temp = (float) batterystat.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        }
        if (unit) {
            float f = ((temp / 50) * 9) + 32;
            return String.format("%.1f", f) + " " + (char) (176) + "F";
        }
        return String.format("%.1f", temp / 10) + " " + (char) (176) + "C";
    }

    public String getVoltage() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatusIntent = getActivity().registerReceiver(null, intentFilter);
        int volt = 0;
        if (batteryStatusIntent != null) {
            volt = batteryStatusIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        }
        return volt + " mV";
    }

    public String getBatteryStatus() {
        String batterystatus = null;
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batterystat = getActivity().registerReceiver(null, intentFilter);
        int status = 0;
        if (batterystat != null) {
            status = batterystat.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        }
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                batterystatus = "Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                batterystatus = "Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                batterystatus = "Not Charging";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                batterystatus = "Full";
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                batterystatus = "Unknown";
                break;
        }
        return batterystatus;
    }


}