package com.deviceinfo.Fragments;

import static android.telephony.TelephonyManager.PHONE_TYPE_CDMA;
import static android.telephony.TelephonyManager.PHONE_TYPE_GSM;
import static android.telephony.TelephonyManager.PHONE_TYPE_NONE;
import static android.telephony.TelephonyManager.PHONE_TYPE_SIP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.deviceinfo.DeviceInformationUtil;
import com.deviceinfo.R;

import java.util.ArrayList;

public class Device_Fragment extends Fragment {

    ListView DeviceInfoLV;
    ArrayList<String> DeviceInfoList = new ArrayList<>();
    String usbSupport, androidID, model, manufacturer, brand, device_lav, board, hardware, netOperator, macAddress, fingerprint, phoneType, advertId;
    DeviceInformationUtil deviceInformationUtil;
    WifiManager wifiManager;
    TelephonyManager telephonyManager;

    public Device_Fragment() {
    }

    @SuppressLint({"MissingInflatedId", "DefaultLocale"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_, container, false);
        DeviceInfoLV = view.findViewById(R.id.DeviceInfoLV);

        if (DeviceInfoList != null)
            DeviceInfoList.clear();
        usbSupport = "Supported";
        if (getContext() != null && !getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_USB_HOST)) {
            usbSupport = "Not Supported";
        }

        if (getContext() != null) {
            wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            androidID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        }
        model = Build.MODEL;
        manufacturer = Build.MANUFACTURER;
        brand = Build.BRAND;
        device_lav = Build.DEVICE;
        board = Build.BOARD;
        hardware = Build.HARDWARE;
        phoneType = PhoneType();
        fingerprint = Build.FINGERPRINT;

        DeviceInfoList.add("Model : " + model);
        DeviceInfoList.add("Manufacturer : " + manufacturer);
        DeviceInfoList.add("Brand : " + brand);
        DeviceInfoList.add("Device : " + device_lav);
        DeviceInfoList.add("Board : " + board);
        DeviceInfoList.add("Hardware : " + hardware);
        DeviceInfoList.add("Android Device ID : " + androidID);
        DeviceInfoList.add("Device Type : " + phoneType);
        DeviceInfoList.add("Build Fingerprint : " + fingerprint);
        DeviceInfoList.add("USB Host : " + usbSupport);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, DeviceInfoList);
        DeviceInfoLV.setAdapter(adapter);

        return view;
    }

    private String PhoneType() {
        TelephonyManager manager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String devicetype = "";
        switch (manager.getPhoneType()) {
            case PHONE_TYPE_NONE:
                devicetype = "NONE";
                break;
            case PHONE_TYPE_CDMA:
                devicetype = "CDMA";
                break;
            case PHONE_TYPE_GSM:
                devicetype = "GSM";
                break;
            case PHONE_TYPE_SIP:
                devicetype = "SIP";
                break;
        }
        return devicetype;
    }

}