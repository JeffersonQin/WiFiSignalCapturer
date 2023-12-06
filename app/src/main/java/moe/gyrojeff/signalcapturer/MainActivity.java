package moe.gyrojeff.signalcapturer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Handler handler;
    private TextView wifiInfoText;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiInfoText = findViewById(R.id.wifi_info_text);
        handler = new Handler(Looper.getMainLooper());

        context = this;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String fileName = "WiFiCapture_" + currentDateandTime + ".csv";

        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File log_file = new File(root, fileName);

        try {
            if (!root.exists()) {
                if (!root.mkdirs()) {
                    wifiInfoText.setText("Root create failed");
                }
            }
            if (!log_file.exists()) {
                if (!log_file.createNewFile()) {
                    wifiInfoText.setText("Create log file failed");
                }
            }

            FileWriter writer = new FileWriter(log_file);
            writer.append("write_time,SSID,BSSID,level,time,capabilities,centerFreq0,centerFreq1,channelWidth,freq,operatorFriendlyName,venue,WiFiSSID,ApMldMacAdrs,WiFiStandard,ApMloLinkId,AffiliatedMloLinks\n");
            writer.flush();

            // 获取系统wifi服务
            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            wm.startScan();
            // 获取当前所连接wifi的信息
            // WifiInfo wi = wm.getConnectionInfo();

            // Run this every second
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // permission check
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    wm.startScan();
                    List<ScanResult> scanResults = wm.getScanResults();

                    StringBuilder view_sb = new StringBuilder();
                    StringBuilder log_sb = new StringBuilder();
                    // Get the current time
                    Date currentTime = new Date();

                    // Define the desired date format
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // Convert the current time to a string
                    view_sb.append("Refreshing at ").append(dateFormat.format(currentTime)).append("\n");

                    for (ScanResult result : scanResults) {
                        log_sb.append(dateFormat.format(currentTime)).append(",");
                        view_sb.append(result.SSID);
                        log_sb.append(result.SSID).append(",");
                        view_sb.append("; BSSID: ");
                        view_sb.append(result.BSSID);
                        log_sb.append(result.BSSID).append(",");
                        view_sb.append("; Level: ");
                        view_sb.append(result.level);
                        log_sb.append(result.level).append(",");
                        view_sb.append("dBm; time: ");
                        view_sb.append(result.timestamp);
                        log_sb.append(result.timestamp).append(",");
                        view_sb.append("; capabilities: ");
                        view_sb.append(result.capabilities);
                        log_sb.append(result.capabilities).append(",");
                        view_sb.append("; centerFreq0: ");
                        view_sb.append(result.centerFreq0);
                        log_sb.append(result.centerFreq0).append(",");
                        view_sb.append("; centerFreq1: ");
                        view_sb.append(result.centerFreq1);
                        log_sb.append(result.centerFreq1).append(",");
                        view_sb.append("; channelWidth: ");
                        view_sb.append(result.channelWidth);
                        log_sb.append(result.channelWidth).append(",");
                        view_sb.append("; freq: ");
                        view_sb.append(result.frequency);
                        log_sb.append(result.frequency).append(",");
                        view_sb.append("; operatorFriendlyName: ");
                        view_sb.append(result.operatorFriendlyName);
                        log_sb.append(result.operatorFriendlyName).append(",");
                        view_sb.append("; venue: ");
                        view_sb.append(result.venueName);
                        log_sb.append(result.venueName).append(",");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            view_sb.append("; ---optional--- WiFiSSID: ");
                            view_sb.append(result.getWifiSsid());
                            log_sb.append(result.getWifiSsid()).append(",");
                            view_sb.append("; ApMldMacAdrs: ");
                            view_sb.append(result.getApMldMacAddress());
                            log_sb.append(result.getApMldMacAddress()).append(",");
                            view_sb.append("; WiFiStandard: ");
                            view_sb.append(result.getWifiStandard());
                            log_sb.append(result.getWifiStandard()).append(",");
                            view_sb.append("; ApMloLinkId: ");
                            view_sb.append(result.getApMloLinkId());
                            log_sb.append(result.getApMloLinkId()).append(",");
                            view_sb.append("; AffiliatedMloLinks: ");
                            view_sb.append(result.getAffiliatedMloLinks());
                            log_sb.append(result.getAffiliatedMloLinks()).append(",");
                            //                        sb.append("; InfElem: ");
                            //                        sb.append(result.getInformationElements().get(0));
                        }
                        view_sb.append("\n");
                        log_sb.append("\n");
                    }

                    wifiInfoText.setText(view_sb.toString());
                    try {
                        writer.append(log_sb.toString());
                        writer.flush();
                    } catch (IOException e) {
                        wifiInfoText.setText("LOG FILE UPDATE ERROR!!!");
                    }
                    handler.postDelayed(this, 1000);
                }
            }, 1000);
        } catch (IOException e) {
            wifiInfoText.setText(e.toString());
        }
    }
}