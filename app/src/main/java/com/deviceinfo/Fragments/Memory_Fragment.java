package com.deviceinfo.Fragments;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.deviceinfo.DeviceInformationUtil;
import com.deviceinfo.R;

import java.util.ArrayList;

public class Memory_Fragment extends Fragment {

    ListView memoryList;
    ArrayList<String> memoryInfoList = new ArrayList<>();
    Handler broadcastHandler, cpuCoreHandler, ramHandler;

    ActivityManager.MemoryInfo memoryInfo;
    ActivityManager manager;

    DeviceInformationUtil deviceInformationUtil;

    double totalRAM, RAM, usedRAM, freeRam, AvailableRam, totalSys, usedSys,
            totalInternal, usedInternal, freeInternal, Internal, AvailableInternal,
            AvailableExternal, totalExternal, usedExternal, freeExternal, External;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_memory, container, false);

        memoryList = view.findViewById(R.id.memoryList);

        totalRAM = DeviceInformationUtil.getTotalRAM(getContext());
        RAM = DeviceInformationUtil.convertBytesToGB((long) totalRAM);
        AvailableRam = DeviceInformationUtil.getAvailableRAM(getContext());
        freeRam = DeviceInformationUtil.convertBytesToGB((long) AvailableRam);
        usedRAM = RAM - freeRam;

        totalInternal = DeviceInformationUtil.getInternalStorageSize();
        Internal = DeviceInformationUtil.convertBytesToGB((long) totalInternal);
        AvailableInternal = DeviceInformationUtil.getAvailableInternalStorageSize();
        freeInternal = DeviceInformationUtil.convertBytesToGB((long) AvailableInternal);
        usedInternal = Internal - freeInternal;


        External = DeviceInformationUtil.getTotalExtStorage();
        totalExternal = DeviceInformationUtil.convertBytesToGB((long) totalExternal);
        AvailableExternal = DeviceInformationUtil.getRemainingExtStorage(getContext());
        freeExternal = DeviceInformationUtil.convertBytesToGB((long) AvailableExternal);
        usedExternal = totalExternal - freeExternal;

        memoryInfoList.add("RAM : " + RAM + "GB");
        memoryInfoList.add("Used RAM : " + usedRAM + "GB");
        memoryInfoList.add("Free Ram : " + freeRam + "GB");
        memoryInfoList.add("");

        memoryInfoList.add("Internal : " + Internal + "GB");
        memoryInfoList.add("Free Internal : " + freeInternal + "GB");
        memoryInfoList.add("Used Internal : " + usedInternal + "GB");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, memoryInfoList);
        memoryList.setAdapter(adapter);

        return view;
    }
}