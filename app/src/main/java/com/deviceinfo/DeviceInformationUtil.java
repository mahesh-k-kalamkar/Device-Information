package com.deviceinfo;

import static android.telephony.TelephonyManager.PHONE_TYPE_CDMA;
import static android.telephony.TelephonyManager.PHONE_TYPE_GSM;
import static android.telephony.TelephonyManager.PHONE_TYPE_NONE;
import static android.telephony.TelephonyManager.PHONE_TYPE_SIP;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Size;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class DeviceInformationUtil {


    public DeviceInformationUtil(Context context) {
        this.context = context;
    }

    Context context;
    boolean unit;

    public static String getDeviceManufacturer(){
        return Build.MANUFACTURER;
    }

    public static String getModelNumber(){
        return Build.MODEL;
    }

    public static String getModelName(){
        return Build.DEVICE;
    }

    public static String getRamStorage(Context context){
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager  = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);

        long totalRamStorage = memoryInfo.totalMem;
        return String.valueOf(totalRamStorage/(1024*1024));
    }

    public static String getRemainingExtStorage(){
        StatFs statStorage = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = statStorage.getBlockSizeLong()*statStorage.getAvailableBlocksLong();
        long megaBytesAvailable = bytesAvailable/(1024*1024);

        return String.valueOf( megaBytesAvailable>=1024 ? megaBytesAvailable/1024 : megaBytesAvailable);
    }

    public static String getBatteryLevel(Context context){
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null,intentFilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE,-1);

        return String.valueOf((level*100)/(float)scale);
    }

    public static String getAndroidVersion(){
        String android_release = Build.VERSION.RELEASE;
        return android_release;
    }

    public static String getAndroidSDK(){
        int sdk_ver = Build.VERSION.SDK_INT;
        return String.valueOf(sdk_ver);
    }

    public static String getCameraPixels(Context context){
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        double MegaPixels_result = 0;

        try{
            for(String cameraId : cameraManager.getCameraIdList()){
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                StreamConfigurationMap configurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);


                if(configurationMap==null)continue;

                Size[] sizes =  configurationMap.getOutputSizes(ImageFormat.JPEG);
              //  double max_mega_pixels = 0;
                for(Size size: sizes){
                    double temp_MP = (size.getHeight()*size.getWidth())/1000000.0;
                        MegaPixels_result = Math.max(MegaPixels_result,temp_MP);
                }
            }
        }catch (CameraAccessException e){
            e.printStackTrace();
        }

        return String.valueOf(MegaPixels_result);
    }

    public static String getCameraAperture(Context context){
            CameraManager cameraManager =  (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            String cameraAperture_array = "";
            try{
                for(String camId : cameraManager.getCameraIdList()){
                    CameraCharacteristics chars = cameraManager.getCameraCharacteristics(camId);
                    cameraAperture_array  += " " + Arrays.toString(chars.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES));
                    Log.d("tag", "getCameraAperture: "+chars.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES));
                }
            }catch (CameraAccessException e){
                e.printStackTrace();
            }

            return cameraAperture_array;
    }

    public static String getProcessorInformation(){
        return Build.HARDWARE;
    }

    public static String getGpuInformation(Context context){
        String gpuInfo = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().getGlEsVersion();
        return gpuInfo;
    }

}