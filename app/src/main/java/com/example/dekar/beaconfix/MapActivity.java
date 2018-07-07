package com.example.dekar.beaconfix;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dekar.beaconfix.ble_find.BluetoothLE;
import com.example.dekar.beaconfix.custom_view.CustomView;


public class MapActivity extends AppCompatActivity {

    private BluetoothLE bluetoothLE;
    private CustomView customView;
    private boolean flagStartMap;
    private ImageView imageView;
    private TextView tvRssiMap;
    private boolean flagFind;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flagStartMap = true;
        setContentView(R.layout.activity_map);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        tvRssiMap = findViewById(R.id.tvMap);
        customView = findViewById(R.id.customView);

        flagFind = BluetoothLE.flagFind;
        context = getApplicationContext();

        getInstanceBLE();
    }

    private void getInstanceBLE(){
        if (bluetoothLE == null) bluetoothLE = new BluetoothLE(context);
    }
}