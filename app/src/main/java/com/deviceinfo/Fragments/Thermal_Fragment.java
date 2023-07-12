package com.deviceinfo.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.deviceinfo.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Thermal_Fragment extends Fragment {

    ListView ThermalLV;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> records;

    private Handler handler;

    public Thermal_Fragment() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thermal, container, false);

        ThermalLV = view.findViewById(R.id.ThermalLV);
        records = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, records);
        ThermalLV.setAdapter(adapter);

        handler = new Handler(Looper.getMainLooper());

        retrieveThermalData();

        return view;
    }

    private void retrieveThermalData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String temp, type;
                for (int i = 0; i < 29; i++) {
                    temp = thermalTemp(i);
                    if (!temp.equals("0.0")) {
                        type = thermalType(i);
                        if (type != null) {
                            final String record = "ThermalValues " + type + " : " + temp + "\n";
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    records.add(record);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }
            }
        }).start();
    }

    private String thermalTemp(int i) {
        String path = "/sys/class/thermal/thermal_zone" + i + "/temp";
        StringBuilder temp = new StringBuilder();
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);

            String line;
            while ((line = reader.readLine()) != null) {
                temp.append(line);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp.toString();
    }

    private String thermalType(int i) {
        String path = "/sys/class/thermal/thermal_zone" + i + "/type";
        StringBuilder type = new StringBuilder();
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);

            String line;
            while ((line = reader.readLine()) != null) {
                type.append(line);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type.toString();
    }

}
