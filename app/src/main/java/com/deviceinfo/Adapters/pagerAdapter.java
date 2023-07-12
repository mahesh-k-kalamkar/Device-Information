package com.deviceinfo.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.deviceinfo.Fragments.Battery_Fragment;
import com.deviceinfo.Fragments.CPU_Fragment;
import com.deviceinfo.Fragments.Device_Fragment;
import com.deviceinfo.Fragments.Display_Fragment;
import com.deviceinfo.Fragments.Memory_Fragment;
import com.deviceinfo.Fragments.Sensors_Fragment;
import com.deviceinfo.Fragments.System_Fragment;

public class pagerAdapter extends FragmentPagerAdapter {
    public pagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public pagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new CPU_Fragment();
        } else if (position == 1) {
            return new Device_Fragment();
        } else if (position == 2) {
            return new System_Fragment();
        } else if (position == 3) {
            return new Battery_Fragment();
        } else if (position == 4) {
            return new Sensors_Fragment();
        } else if (position == 5){
            return new Display_Fragment();
        } else {
            return new Memory_Fragment();
        }
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "CPU";
        } else if (position == 1) {
            return "Device";
        } else if (position == 2) {
            return "System";
        } else if (position == 3) {
            return "Battery";
        } else if (position == 4) {
            return "Sensors";
        } else if (position == 5) {
            return "Display";
        }else {
            return "Memory";
        }
    }
}
