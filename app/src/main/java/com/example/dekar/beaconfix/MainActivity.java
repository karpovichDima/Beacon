package com.example.dekar.beaconfix;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.dekar.beaconfix.ble_find.BluetoothLE;
import com.example.dekar.beaconfix.firebase.FBConnecting;
import com.example.dekar.beaconfix.lv_adapter.DeviceListAdapter;

/**
 * Edited by DeKar on 06.07.2018.
 */

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 1;

    private TextView tvFakeBeac;
    private DeviceListAdapter mDeviceListAdapter;
    private ListView listview_for_name_devices;
    private boolean findBtPressed = false;
    private ProgressBar progressBar;
    private FBConnecting fbConnecting;

    FloatingActionButton startScanningButton;
    BluetoothLE bluetoothLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        getInstanceBLE();
        getInstanceDLA();
        bluetoothLE.setmDeviceListAdapter(mDeviceListAdapter);
        getInstanceFB(bluetoothLE);

        listview_for_name_devices = findViewById(R.id.listview_for_name_devices_ID);
        progressBar = findViewById(R.id.progress_bar_ID);
        tvFakeBeac = (TextView) findViewById(R.id.tvFakeBeac);
        progressBar.setVisibility(View.INVISIBLE);

        startScanningButton = (FloatingActionButton) findViewById(R.id.startScanButton);
        startScanningButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // start/stop find
                pressFind();
            }
        });

        if (!(bluetoothLE.getEnableIntent() == null)) strtActvtyFrRslt();
        lctEnbld();
    }



    private void pressFind() {
        if (findBtPressed) {
            progressBar.setVisibility(View.INVISIBLE);
            bluetoothLE.stopScanning();
            findBtPressed = false;
            Toast stopedToast = Toast.makeText(getApplicationContext(),"stop", Toast.LENGTH_SHORT);
            stopedToast.show();
        } else if (!findBtPressed) {
            progressBar.setVisibility(View.VISIBLE);
            bluetoothLE.startScanning();
            findBtPressed = true;
            Toast startedToast = Toast.makeText(getApplicationContext(),"find", Toast.LENGTH_SHORT);
            startedToast.show();
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

    private void getInstanceDLA() {
        if (mDeviceListAdapter == null) mDeviceListAdapter = new DeviceListAdapter(this);
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