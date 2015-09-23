package com.paulsonnentag.balanceboard;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.provider.Settings;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.lang.Override;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

public class BalanceBoardPlugin extends CordovaPlugin {

    private Stack<JSONObject> events = new Stack<JSONObject>();
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    public void initialize (CordovaInterface cordova, CordovaWebView webView) {
        cordova.getActivity().registerReceiver(bcReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        cordova.getActivity().registerReceiver(bcReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("connect")) {
            addEvent("startconnect");
            connect();

        } else if (action.equals("getEvent")) {
            popEvent(callbackContext);
        } else {
            return false;
        }

        return true;
    }


    private void connect () {
        if(!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        if(!bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.startDiscovery();
        }
    }

    private void addEvent (String type, JSONObject data) {
        try {
            JSONObject event = new JSONObject();
            event.put("type", type);
            event.put("data", data);
            events.push(event);

        } catch (JSONException e) {}
    }

    private void popEvent (CallbackContext callbackContext) {
        if (events.empty()) {
            callbackContext.error("NO_EVENT");
        } else {
            callbackContext.success(events.pop());
        }
    }

    private void addEvent(String type) {
        addEvent(type, new JSONObject());
    }

    private final BroadcastReceiver bcReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


                //if (balanceBoard == null) {
                    addEvent("discovered");
                    //initBalanceBoard(device);
                    bluetoothAdapter.cancelDiscovery();
                //}

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                connect();
            }
        }
    };
}
