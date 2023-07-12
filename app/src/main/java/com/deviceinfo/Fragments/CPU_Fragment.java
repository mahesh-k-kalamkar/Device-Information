package com.deviceinfo.Fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.deviceinfo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class CPU_Fragment extends Fragment {


    ListView CPULV;
    ArrayList<String> CPUList = new ArrayList<>();

    String cpuArch, processorName, family, machine, processor, cpuHardware, governor, cpuType, cpuDriver, runningCpu, vulkan, abi, usage, bogoMips, features, osArch, cpuFreq, memory = "", bandwidth = "", channel = "";
    int coresCount;
    String json = null, process = "", cpuFamily = "";


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_c_p_u_, container, false);
        CPULV = view.findViewById(R.id.CPULV);


        getHeader();
        abi = Arrays.toString(Build.SUPPORTED_ABIS).replace("[", "").replace("]", "");
        if (processorName != null) CPUList.add("Processor : " + processorName);
        if (cpuHardware == null) CPUList.add("CPU Hardware : " + Build.HARDWARE);
        else CPUList.add("CPU Hardware : " + cpuHardware);
        CPUList.add("Supported ABIs :" + abi);
        coresCount = getCoresCount();
        cpuArch = getCpuArchitecture();
        governor = getCpuGovernor();
        cpuDriver = getCpuDriver();
        runningCpu = getRunningCpuString();
        cpuFreq = getCpuFrequency();
        usage = getUsage();
        CPUList.add("CPU Architecture : " + cpuArch);
        CPUList.add("Cores : " + coresCount);
        CPUList.add("CPU Governor : " + governor);
        osArch = System.getProperty("os.arch");
        if (Arrays.toString(Build.SUPPORTED_ABIS).contains("64")) cpuType = "64 Bit";
        else cpuType = "32 Bit";
        CPUList.add("CPU Type : " + cpuType + " (" + osArch + ")");
        CPUList.add("CPU Scaling Driver : " + cpuDriver);
        CPUList.add("CPU Frequency : " + cpuFreq);
        CPUList.add("Running CPUs : \n\n" + runningCpu);
        getDataFromJson();
        if (bogoMips != null) CPUList.add("BogoMIPS : " + bogoMips);
        if (features != null) CPUList.add("Features : " + features);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            vulkan = "Not Supported : ";
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            vulkan = "Supported : " + " (1.0)";
        } else {
            vulkan = "Supported : " + " (1.1)";
        }
        CPUList.add("Vulkan Support : " + vulkan);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, CPUList);
        CPULV.setAdapter(adapter);
        return view;
    }


    @SuppressLint("SetTextI18n")

    private void getHeader() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("sys/devices/soc0/vendor"));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                processor = str;
            }
        } catch (IOException e) {
            processor = "error";
            e.printStackTrace();
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("sys/devices/soc0/machine"));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                machine = str;
            }
        } catch (IOException e) {
            machine = "error";
            e.printStackTrace();
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("sys/devices/soc0/family"));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                family = str;
            }
        } catch (IOException e) {
            family = "error";
            e.printStackTrace();
        }
        try {
            String a, b, key, value, str;
            BufferedReader reader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            while ((str = reader.readLine()) != null) {
                String[] data = str.trim().split(":");
                a = Arrays.toString(data);
                b = a.replace("[", "").replace("]", "").trim();
                if (b != null && b.contains(",")) {
                    key = b.substring(0, b.indexOf(","));
                    value = b.substring(b.indexOf(",") + 1);
                    if (key.trim().equals("Processor")) processorName = value.trim();
                    if (key.trim().equalsIgnoreCase("model name")) processorName = value.trim();
                    if (key.trim().equalsIgnoreCase("hardware")) cpuHardware = value.trim();
                    if (key.trim().equalsIgnoreCase("bogomips")) bogoMips = value.trim();
                    if (key.trim().equalsIgnoreCase("features")) features = value.trim();
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCoresCount() {
        int processorCount = 0;
        try {
            Process process = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/present");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            if ((line = reader.readLine()) != null) {
                processorCount = Integer.parseInt(line.substring(line.indexOf("-") + 1)) + 1;
            }
            process.destroy();
            reader.close();
        } catch (IOException | InterruptedException | NumberFormatException e) {
            processorCount = Runtime.getRuntime().availableProcessors();
            e.printStackTrace();
        }
        return processorCount;
    }

    @SuppressLint("DefaultLocale")
    public String getCpuArchitecture() {
        StringBuilder builder = new StringBuilder();
        int cores = getCoresCount();
        if (cores <= 4) {
            Process process;
            BufferedReader reader;
            try {
                process = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + 0 + "/cpufreq/cpuinfo_max_freq");
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                float f = 0f;
                if ((line = reader.readLine()) != null) {
                    f = Float.parseFloat(line) / 1000000f;
                }
                builder.append(cores).append(" x ").append(String.format("%.2f", f)).append(" GHz" + "\n");
            } catch (IOException e) {
                e.getMessage();
            }
        } else {
            Process process;
            BufferedReader reader;
            float[] arr = new float[cores];
            int i = 0;
            while (i < cores) {
                try {
                    process = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq");
                    reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    float f;
                    if ((line = reader.readLine()) != null) {
                        f = Float.parseFloat(line) / 1000000f;
                        arr[i] = f;
                    }
                } catch (IOException e) {
                    e.getMessage();
                }
                i++;
            }
            int count = 0;
            float freq = 0f;
            for (int j = 0; j < arr.length; j++) {
                if (j == 0) {
                    freq = arr[0];
                    count++;
                } else if (j >= 1) {
                    if (freq == arr[j]) count++;
                    else {
                        builder.append(count).append(" x ").append(String.format("%.2f", freq)).append(" GHz" + "\n");
                        freq = arr[j];
                        count = 1;
                    }
                }
                if (j == arr.length - 1)
                    builder.append(count).append(" x ").append(String.format("%.2f", freq)).append(" GHz" + "\n");
            }
        }
        if (builder.toString().length() > 0)
            return builder.substring(0, builder.toString().length() - 1);
        return "Unknown";
    }

    public String getCpuGovernor() {
        BufferedReader reader;
        String line;
        try {
            File file = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
            reader = new BufferedReader(new FileReader(file));
            line = reader.readLine();
            reader.close();
        } catch (IOException e) {
            line = "Unknown";
            e.printStackTrace();
        }
        return line;
    }

    public String getCpuDriver() {
        BufferedReader reader;
        String line;
        try {
            File file = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_driver");
            reader = new BufferedReader(new FileReader(file));
            line = reader.readLine();
            reader.close();
        } catch (IOException e) {
            line = "Unknown";
            e.printStackTrace();
        }
        return line;
    }

    public String getRunningCpuString() {
        StringBuilder cpuBuilder = new StringBuilder();
        String totalRunning;
        for (int i = 0; i < getCoresCount(); i++) {
            cpuBuilder.append("   Core ").append(i).append("       ").append(getRunningCpu(i, true)).append("\n\n");
        }
        totalRunning = cpuBuilder.toString();
        return totalRunning.substring(0, totalRunning.length() - 2);
    }

    @SuppressLint("DefaultLocale")
    public String getRunningCpu(int i, boolean bool) {
        Process process;
        BufferedReader reader;
        float freq;
        String frequency = "Unknown";
        int f;
        try {
            process = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_cur_freq");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            if ((line = reader.readLine()) != null) {
                if (bool) {
                    freq = Float.parseFloat(line) / 1000f;
                    frequency = String.format("%.2f", freq) + " MHz";
                } else {
                    f = Integer.parseInt(line) / 1000;
                    frequency = String.format("%d", f) + " MHz";
                }
            }
            process.destroy();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return frequency;
    }

    public String getCpuFrequency() {
        int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
        int minFreq, maxFreq;
        Process process1, process2;
        BufferedReader reader1, reader2;
        for (int i = 0; i < getCoresCount(); i++) {
            try {
                process1 = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_min_freq");
                reader1 = new BufferedReader(new InputStreamReader(process1.getInputStream()));
                process2 = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq");
                reader2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
                String line1, line2;
                if ((line1 = reader1.readLine()) != null) {
                    minFreq = Integer.parseInt(line1) / 1000;
                    if (min >= minFreq) min = minFreq;
                }
                if ((line2 = reader2.readLine()) != null) {
                    maxFreq = Integer.parseInt(line2) / 1000;
                    if (max <= maxFreq) max = maxFreq;
                }
                process1.destroy();
                reader1.close();
                process2.destroy();
                reader2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (min != Integer.MAX_VALUE && max != Integer.MIN_VALUE)
            return min + " Mhz" + " - " + max + " Mhz";
        return "Unknown";
    }

    public String getUsage() {
        int c = 0;
        for (int i = 0; i < getCoresCount(); i++) {
            c += (int) (getCpuUsage(i) * 100);
        }
        return c / getCoresCount() + "%";
    }

    public float getCpuUsage(int i) {
        Process process1, process2;
        BufferedReader reader1, reader2;
        int freq = 0, maxFreq = 0;
        try {
            process1 = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_cur_freq");
            reader1 = new BufferedReader(new InputStreamReader(process1.getInputStream()));
            process2 = Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq");
            reader2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
            String line1, line2;
            if ((line1 = reader1.readLine()) != null) {
                freq = Integer.parseInt(line1) / 1000;
            }
            if ((line2 = reader2.readLine()) != null) {
                maxFreq = Integer.parseInt(line2) / 1000;
            }
            process1.destroy();
            reader1.close();
            process2.destroy();
            reader2.close();
        } catch (IOException e) {
            e.getMessage();
        }
        if (maxFreq == 0) return 0.5f;
        return (float) freq / maxFreq;
    }

    public void getDataFromJson() {
        try {
            InputStream inputStream = getContext().getAssets().open("socList.json");
            int size = inputStream.available();
            byte[] bytes = new byte[size];
            inputStream.read(bytes);
            inputStream.close();
            json = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONObject object = jsonObject.getJSONObject(machine);
                if (!object.getString("CPU").equals("")) {
                    cpuFamily = object.getString("CPU");
                    CPUList.add("CPU Family : " + object.getString("CPU"));
                }
                if (!object.getString("FAB").equals("")) {
                    process = object.getString("FAB");
                    CPUList.add("CPU Process : " + object.getString("FAB"));
                }
                memory = object.getString("MEMORY");
                bandwidth = object.getString("BANDWIDTH");
                channel = object.getString("CHANNELS");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}