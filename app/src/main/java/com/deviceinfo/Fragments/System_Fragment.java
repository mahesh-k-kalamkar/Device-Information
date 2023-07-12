package com.deviceinfo.Fragments;

import static java.lang.System.getProperty;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.deviceinfo.R;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class System_Fragment extends Fragment {

    ListView systemLV;
    ArrayList<String> SocList = new ArrayList<>();
    String codeName, bootloader, buildNo, baseBand, type, javaVm, kernel, lang, openGl, rootAccess;
    int api;
    ConfigurationInfo info;
    ActivityManager activityManager;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system_, container, false);

        systemLV = view.findViewById(R.id.systemLV);

        activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        info = activityManager.getDeviceConfigurationInfo();
        api = Build.VERSION.SDK_INT;
        codeName = CodeName(api);
        bootloader = Build.BOOTLOADER;
        buildNo = Build.DISPLAY;
        baseBand = Build.getRadioVersion();
        type = Build.TYPE;
        lang = Locale.getDefault().getDisplayLanguage();
        javaVm = getProperty("java.vm.version");
        kernel = getProperty("os.version");
        openGl = info.getGlEsVersion();
        rootAccess = getRootInfo();

        SocList.add("Android Version : " + codeName);
        SocList.add("API Level : " + api);
        SocList.add("Type : " + type);
        SocList.add("Bootloader : " + bootloader);
        SocList.add("java VM : " + javaVm);
        SocList.add("Kernel : " + kernel);
        SocList.add("OpenGL ES : " + openGl);
        SocList.add("Root Access : " + rootAccess);
        SocList.add("Build Number : " + buildNo);
        SocList.add("Baseband : " + baseBand);
        SocList.add("Language : " + lang);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, SocList);
        systemLV.setAdapter(adapter);

        return view;
    }

    public String CodeName(int level) {
        String[] api_level = new String[]{"Android 4.1 Jelly Bean", "Android 4.2 Jelly Bean", "Android 4.3 Jelly Bean", "Android 4.4 KitKat", "Android 4.4W KitKat", "Android 5.0 Lollipop", "Android 5.1 Lollipop", "Android 6.0 Marshmallow", "Android 7.0 Nougat", "Android 7.1 Nougat", "Android 8.0 Oreo", "Android 8.1.0 Oreo", "Android 9 Pie", "Android 10", "Android 11"};
        return (String) Array.get(api_level, level - 17);
    }

    public String getRootInfo() {
        if (checkRootFiles() || checkTags()) {
            return "Yes";
        }
        return "No";
    }

    public boolean checkRootFiles() {
        boolean root = false;
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            root = new File(path).exists();
            if (root)
                break;
        }
        return root;
    }

    public boolean checkTags() {
        String tag = Build.TAGS;
        return tag != null && tag.trim().contains("test-keys");
    }
}