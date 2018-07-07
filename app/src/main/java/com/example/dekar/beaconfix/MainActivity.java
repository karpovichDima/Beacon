package com.example.dekar.beaconfix;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dekar.beaconfix.ble_find.BluetoothLE;
import com.example.dekar.beaconfix.firebase.FBConnecting;
import com.example.dekar.beaconfix.lv_adapter.DeviceListAdapter;

/**
 * Edited by DeKar on 06.07.2018.
 */

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 1;
    private FloatingActionButton startScanningButton;
    private DeviceListAdapter mDeviceListAdapter;
    private ListView listview_for_name_devices;
    private FloatingActionButton fBMapActivity;
    private boolean findBtPressed = false;
    private FBConnecting fbConnecting;
    private ProgressBar progressBar;
    private BluetoothLE bluetoothLE;
    private Context context = this;
    private TextView tvFakeBeac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        getInstanceBLE();
        getInstanceDLA(bluetoothLE);
        bluetoothLE.setmDeviceListAdapter(mDeviceListAdapter);
        getInstanceFB(bluetoothLE);

        listview_for_name_devices = findViewById(R.id.listview_for_name_devices_ID);
        progressBar = findViewById(R.id.progress_bar_ID);
        tvFakeBeac = findViewById(R.id.tvFakeBeac);
        progressBar.setVisibility(View.INVISIBLE);

        startScanningButton = findViewById(R.id.startScanButton);
        startScanningButton.setOnClickListener((View v) -> pressFind());

        fBMapActivity = findViewById(R.id.map_activity);
        fBMapActivity.setOnClickListener((View v) -> startMapActivity());

        if (!(bluetoothLE.getEnableIntent() == null)) strtActvtyFrRslt();
        lctEnbld();
    }

    private void pressFind() {
        if (findBtPressed) {
            progressBar.setVisibility(View.INVISIBLE);
            bluetoothLE.stopScanning();
            findBtPressed = false;
        } else if (!findBtPressed) {
            progressBar.setVisibility(View.VISIBLE);
            bluetoothLE.startScanning();
            findBtPressed = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    public void getInstanceBLE(){
        if (bluetoothLE == null) bluetoothLE = new BluetoothLE(this);
    }

    private void getInstanceDLA(BluetoothLE bluetoothLE) {
        if (mDeviceListAdapter == null) mDeviceListAdapter = new DeviceListAdapter(this, bluetoothLE);
    }

    private void getInstanceFB(BluetoothLE bluetoothLE) {
        if(fbConnecting == null) fbConnecting = new FBConnecting(this, bluetoothLE, mDeviceListAdapter);
    }

    private void strtActvtyFrRslt() {
        startActivityForResult(bluetoothLE.getEnableIntent(),REQUEST_ENABLE_BT);
    }

    private void lctEnbld() {

        // Make sure we have access coarse location enabled, if not, prompt the user to enable it
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("This app needs location access");
            builder.setMessage("Please grant location access so this app can detect peripherals.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            });
            builder.show();
        }

    }

    private void startMapActivity(){
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bluetoothLE.isFlagFind()) {
            bluetoothLE.stopScanning();
            bluetoothLE.startScanning();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        listview_for_name_devices.setAdapter(mDeviceListAdapter);
    }
}