package com.deviceinfo;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.text.DecimalFormat;

public class DeviceInformationUtil {


    Context context;
    boolean unit;

    public DeviceInformationUtil(Context context) {
        this.context = context;
    }

    public static long getTotalRAM(Context context) {
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memInfo);
        return memInfo.totalMem;
    }

    public static double convertBytesToGB(long bytes) {
        double gb = bytes / (1024.0 * 1024.0 * 1024.0);
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(gb));
    }

    public static long getAvailableRAM(Context context) {
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memInfo);
        return memInfo.availMem;
    }

    public static long getInternalStorageSize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = statFs.getBlockSizeLong();
        long totalBlocks = statFs.getBlockCountLong();
        return totalBlocks * blockSize;
    }

    public static long getAvailableInternalStorageSize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = statFs.getBlockSizeLong();
        long availableBlocks = statFs.getAvailableBlocksLong();
        return availableBlocks * blockSize;
    }

    public static long getRemainingExtStorage(Context context) {
//        StatFs statStorage = new StatFs(Environment.getExternalStorageDirectory().getPath());
//        long bytesAvailable = statStorage.getBlockSizeLong() * statStorage.getAvailableBlocksLong();
//        long megaBytesAvailable = bytesAvailable / (1024 * 1024);
//
//        return megaBytesAvailable >= 1024 ? megaBytesAvailable / 1024 : megaBytesAvailable;

        StatFs statStorage = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long blockSize = statStorage.getBlockSizeLong();
        long availableBlocks = statStorage.getAvailableBlocksLong();
        return availableBlocks * blockSize;
    }

    public static long getTotalExtStorage() {
        StatFs statStorage = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesSize = statStorage.getBlockSizeLong();
        long totalBlocks = statStorage.getBlockCountLong();

        return totalBlocks * bytesSize;
    }

}