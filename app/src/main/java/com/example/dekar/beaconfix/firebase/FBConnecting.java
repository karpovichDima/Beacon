package com.example.dekar.beaconfix.firebase;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.example.dekar.beaconfix.ble_find.BluetoothLE;
import com.example.dekar.beaconfix.custom_view.CustomView;
import com.example.dekar.beaconfix.lv_adapter.DeviceListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class FBConnecting {

    private HashMap<String, int[]> hashmapCoordinates;
    private DeviceListAdapter deviceListAdapter;
    private ArrayList<String> devicesArray;
    private ConnectivityManager manager;
    private String deviceName = " ";
    private BluetoothLE bluetoothLE;
    private CustomView cvObject;
    private Context context;
    private boolean is3g;
    private String output;
    private String snaps;

    public FBConnecting(Context context) {
        creatingHashMap();
        this.context = context;
    }

    public FBConnecting(Context context, BluetoothLE bluetoothLE, DeviceListAdapter deviceListAdapter) {
        this.context = context;
        this.bluetoothLE = bluetoothLE;
        this.deviceListAdapter = deviceListAdapter;
        creatingHashMap();
    }

    public void connectedToFirebase(String name_branch) {

        if (name_branch == null) {
            name_branch = "error";
        }

        //check3g();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef = database.getReference(name_branch);
        databaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                snaps = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                convertValueFarebase(snaps);

                // databaseRef.child("iNode-C21A9A").child("X1").setValue("5");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void creatingHashMap(){
        if (hashmapCoordinates == null) hashmapCoordinates = new HashMap<String, int[]>();
    }

    private void convertValueFarebase(String input) {

        int value_x_int = 0;
        int value_y_int = 0;
        // for x
        Pattern PATTERNx = Pattern.compile("x=(.*?)\\,");
        Matcher matcherX = PATTERNx.matcher(input);
        if (matcherX.find()) {
            String value_x_string = matcherX.group(1);
            value_x_int = Integer.parseInt(value_x_string);
        }

        // for y
        Pattern PATTERNy = Pattern.compile("y=(.*?)\\}");
        Matcher matcherY = PATTERNy.matcher(input);
        if (matcherY.find()) {
            String value_y_string = matcherY.group(1);
            value_y_int = Integer.parseInt(value_y_string);
        }
        if(!(deviceListAdapter == null)) {
            devicesArray = deviceListAdapter.getDevicesName();
            deviceName = devicesArray.get(devicesArray.size());
        } else {
            deviceName = BluetoothLE.device.getDevice().getName();
        }

        hashmapCoordinates(deviceName, value_x_int, value_y_int);
        // fakeBeacCoordinator();
    }

    private void hashmapCoordinates(String deviceName, int x, int y) {

        hashmapCoordinates.put(deviceName, new int[]{x, y});
        int[] arr_after_hashmap = hashmapCoordinates.get(deviceName);
        getInstanceCV();
        cvObject.setValue_X_Y_after_hash(arr_after_hashmap[0], arr_after_hashmap[1]);
        cvObject.invalidate();
        Toast toast = Toast.makeText(context, "HashMap " + arr_after_hashmap[0] + " " + arr_after_hashmap[1], Toast.LENGTH_SHORT);
        toast.show();
    }

    private void getInstanceCV() {
        if (cvObject == null) cvObject = new CustomView(context);
    }
}


