package com.example.dekar.beaconfix;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

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
        setContentView(R.layout.activity_map);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        flagStartMap = true;

        customView = findViewById(R.id.customView);

        flagFind = BluetoothLE.flagFind;
        context = getApplicationContext();

        getInstanceBLE();
    }

    private void getInstanceBLE(){
        if (bluetoothLE == null) bluetoothLE = new BluetoothLE(context);
    }
}