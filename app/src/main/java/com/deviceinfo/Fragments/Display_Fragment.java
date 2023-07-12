package com.deviceinfo.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.deviceinfo.R;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Display_Fragment extends Fragment {

    ListView DisplayList;
    ArrayList<String> DisplayInfoList = new ArrayList<>();

    String density, fontScale, size, refreshRate, hdr, hdrCapable, brightnessLevel, brightnessMode, timeout, orientation;
    StringBuilder str;

    @SuppressLint({"MissingInflatedId", "DefaultLocale"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_display_, container, false);

        int h, w, dpi;
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        h = metrics.heightPixels;
        w = metrics.widthPixels;
        dpi = metrics.densityDpi;
        refreshRate = String.format("%.1f", ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRefreshRate()) + " Hz";
        int brightness = 0, time = 0;
        float font = 0.0f;
        try {
            brightness = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            time = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
            font = getContext().getResources().getConfiguration().fontScale;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        brightnessLevel = (int) ((brightness * 100) / 255) + " %";
        timeout = (time / 1000) + " " + "Seconds";
        fontScale = String.valueOf(font);
        hdr = "Supported";
        hdrCapable = "None";
        str = new StringBuilder();
        density = getDensityDpi();
        size = getScreenSize();

        DisplayList = view.findViewById(R.id.DisplayList);


        DisplayInfoList.add("Resolution "+ " : "+ w + " x " + h + " " + "Pixels");
        DisplayInfoList.add("Density "+ " : " + dpi + " dpi" + density);
        DisplayInfoList.add("Font Scale " + " : " + fontScale);
        DisplayInfoList.add("Physical Size "+ " : " + size);
        DisplayInfoList.add("Refresh Rate "+ " : " + refreshRate);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Display.HdrCapabilities hdrCapabilities = ((Activity)getContext()).getWindowManager().getDefaultDisplay().getHdrCapabilities();
            if (hdrCapabilities.getSupportedHdrTypes().length == 0) {
                hdr = "Not Supported";
            } else {
                int[] hdrtypes = hdrCapabilities.getSupportedHdrTypes();
                for (int hdrtype : hdrtypes) {
                    switch (hdrtype) {
                        case 1:
                            str.append("Dolby Vision HDR\n");
                            break;
                        case 2:
                            str.append("HDR10\n");
                            break;
                        case 3:
                            str.append("Hybrid Log-Gamma HDR\n");
                            break;
                        case 4:
                            str.append("HDR10+\n");
                            break;
                    }
                }
            }
            if (!str.toString().equals(""))
                hdrCapable = str.toString().substring(0, str.toString().length() - 1);
        }
        brightnessMode = getBrightnessMode();
        orientation = getOrientation();
        DisplayInfoList.add("HDR "+ " : "+ hdr);
        DisplayInfoList.add("HDR Capabilities"+ " : "+ hdrCapable);
        DisplayInfoList.add("Brightness Level"+ " : "+ brightnessLevel);
        DisplayInfoList.add("Brightness Mode"+ " : "+ brightnessMode);
        DisplayInfoList.add("Screen Timeout"+ " : "+ timeout);
        DisplayInfoList.add("Orientation"+ " : "+ orientation);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, DisplayInfoList);
        DisplayList.setAdapter(adapter);

         return view;
    }

    public String getDensityDpi() {
        float density;
        String dpi;
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        density = metrics.density;
        if (density >= 0.75f && density < 1.0f) {
            dpi = " (LDPI)";
        } else if (density >= 1.0f && density < 1.5f) {
            dpi = " (MDPI)";
        } else if (density >= 1.5f && density <= 2.0f) {
            dpi = " (HDPI)";
        } else if (density > 2.0f && density <= 3.0f) {
            dpi = " (XHDPI)";
        } else if (density >= 3.0f && density < 4.0f) {
            dpi = " (XXHDPI)";
        } else {
            dpi = " (XXXHDPI)";
        }
        return dpi;
    }

    @SuppressLint("DefaultLocale")
    public String getScreenSize() {
        Point point = new Point();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Display.class.getMethod("getRealSize", Point.class).invoke(((Activity) getContext()).getWindowManager().getDefaultDisplay(), point);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
        double x = Math.pow(point.x / displayMetrics.xdpi, 2);
        double y = Math.pow(point.y / displayMetrics.ydpi, 2);
        double inches = (float) Math.round(Math.sqrt(x + y) * 10) / 10;
        return String.format("%.2f", inches);
    }


    public String getBrightnessMode() {
        String mode = null;
        try {
            switch (Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE)) {
                case 0:
                    mode = "Manual";
                    break;
                case 1:
                    mode = "Automatic";
                    break;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return mode;
    }

    public String getOrientation() {
        String orien = "";
        switch (getContext().getResources().getConfiguration().orientation) {
            case 0:
                orien = "Undefined";
                break;
            case 1:
                orien = "Portrait";
                break;
            case 2:
                orien = "Landscape";
                break;
            case 3:
                orien = "Square";
                break;
        }
        return orien;
    }


}