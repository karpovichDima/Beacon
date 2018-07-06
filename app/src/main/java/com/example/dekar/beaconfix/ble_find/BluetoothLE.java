package com.example.dekar.beaconfix.ble_find;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.dekar.beaconfix.lv_adapter.DeviceListAdapter;

/**
 * Edited by DeKar on 06.07.2018.
 */

public class BluetoothLE {

    private DeviceListAdapter mDeviceListAdapter;
    private Intent enableIntent;
    private BluetoothLeScanner btScanner;
    private BluetoothAdapter btAdapter;
    private BluetoothLE bluetoothLE;
    private Context context;

    private boolean flagFind;

    public BluetoothLE(Context context) {
        this.context = context;
        initBLE();
    }

    private void initBLE() {
        BluetoothManager btManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (btManager != null) btAdapter = btManager.getAdapter();
        btScanner = btAdapter.getBluetoothLeScanner();
        if (btAdapter != null && !btAdapter.isEnabled()) enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    }

    private void eventsAfterFinded(int callbackType, ScanResult result) {
        mDeviceListAdapter.addDevice(result);
        mDeviceListAdapter.notifyDataSetChanged();
    }

    // Device scan callback.
    private ScanCallback leScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            eventsAfterFinded(callbackType, result);
        }
    };

    public Intent getEnableIntent() {
        return enableIntent;
    }

    public boolean isFlagFind() {
        return flagFind;
    }

    public void startScanning() {
        flagFind = true;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (btScanner == null) btScanner = btAdapter.getBluetoothLeScanner();
                btScanner.startScan(leScanCallback);
            }
        });
    }

    public void stopScanning() {
        flagFind = false;
        mDeviceListAdapter.clear();
        mDeviceListAdapter.notifyDataSetChanged();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                btScanner.stopScan(leScanCallback);
            }
        });
    }

    public void setmDeviceListAdapter(DeviceListAdapter deviceListAdapter){
        mDeviceListAdapter = deviceListAdapter;
    }


}
