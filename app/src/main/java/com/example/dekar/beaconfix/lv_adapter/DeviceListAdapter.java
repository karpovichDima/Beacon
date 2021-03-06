package com.example.dekar.beaconfix.lv_adapter;

import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.dekar.beaconfix.R;
import com.example.dekar.beaconfix.ble_find.BluetoothLE;
import com.example.dekar.beaconfix.firebase.FBConnecting;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Edited by DeKar on 06.07.2018.
 */

public class DeviceListAdapter extends BaseAdapter {

    private ArrayList<ScanResult> mLeDevices;
    private ArrayList<String> devicesName;
    private FBConnecting fbConnecting;
    private LayoutInflater mInflator;
    private String deviceName;
    private Context context;

    public DeviceListAdapter(Context context, BluetoothLE bluetoothLE) {
        super();
        this.context = context;
        devicesName = new ArrayList<>();
        mLeDevices = new ArrayList<>();
        this.mInflator = LayoutInflater.from(context);
        creatingFBCon();
    }

    public void addDevice(ScanResult result) {
        if(result.getDevice().getName() != null) {
            if (!devicesName.contains(result.getDevice().getName())) {
                deviceName = result.getDevice().getName();
                mLeDevices.add(result);
                devicesName.add(deviceName);
                fbConnecting.connectedToFirebase(deviceName);
                Toast.makeText(context, deviceName, Toast.LENGTH_SHORT).show();
            } else {
                replacement(BluetoothLE.device);
                Toast.makeText(context, deviceName, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void replacement(ScanResult result) {
        ListIterator<ScanResult> iterator = mLeDevices.listIterator();
        while (iterator.hasNext()) {
            ScanResult next = iterator.next();
            if (next.getDevice().getName().equals(result.getDevice().getName())) {
                iterator.set(result);
            }
        }
        checkDistance(result, iterator);
    }

    private void checkDistance(ScanResult result,ListIterator<ScanResult> iterator) {
        if (result.getRssi() >= 120){
            while (iterator.hasNext()) {
                ScanResult next = iterator.next();
                if (next.getDevice().getName().equals(result.getDevice().getName())) {
                    mLeDevices.remove(iterator);
                }
            }
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = mInflator.inflate(R.layout.listitem_device, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = view.findViewById(R.id.device_address);
            viewHolder.deviceName = view.findViewById(R.id.device_name);
            viewHolder.deviceRSSI = view.findViewById(R.id.rssi_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ScanResult result = mLeDevices.get(i);
        final String deviceName = result.getDevice().getName();
        viewHolder.deviceName.setText("Name: " + deviceName);
        viewHolder.deviceAddress.setText(result.getDevice().getAddress());
        viewHolder.deviceRSSI.setText("RSSI " + result.getRssi());
        return view;
    }
    @Override
    public int getCount() {
        return mLeDevices.size();
    }
    public void clear() {
        mLeDevices.clear();
    }

    public ScanResult getDevice(int position) {
        return mLeDevices.get(position);
    }

    public ArrayList<String> getDevicesName() {
        return devicesName;
    }

    private void creatingFBCon(){
        if (fbConnecting == null) fbConnecting = new FBConnecting(context);
    }

}
