package com.example.paul.fitass;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Home extends AppCompatActivity {

    final String TAG = "Home";

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BalanceBoard balanceBoard = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    @Override
    protected void onStart() {
        super.onStart();

        this.registerReceiver(bcReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        this.registerReceiver(bcReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    public void discoverBalanceBoard (View view) {
        if(!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        if(!bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.startDiscovery();
        }

        Log.d(TAG, "start scan");
    }

    private final BroadcastReceiver bcReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Log.d(TAG, "Found device " + device.getAddress() + " " + device.getName());

                if (balanceBoard == null) {
                    initBalanceBoard(device);
                    bluetoothAdapter.cancelDiscovery();
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d(TAG, "scan complete");
            }
        }
    };

    private void initBalanceBoard (BluetoothDevice device) {
        balanceBoard = new BalanceBoard(bluetoothAdapter, device, wmListener);
    }


    private final BalanceBoard.Listener wmListener = new BalanceBoard.Listener() {
        @Override
        public void onWiimoteConnecting(BalanceBoard wm)
        {
            Log.d(TAG, "board connecting");
        }

        @Override
        public void onWiimoteConnected(BalanceBoard wm)
        {
            Log.d(TAG, "board connected");
        }

        @Override
        public void onWiimoteDisconnected(BalanceBoard wm)
        {
            Log.d(TAG, "board disconnected");
            balanceBoard = null;
        }

        @Override
        public void onWiimoteLEDChange(BalanceBoard wm)
        {
            Log.d(TAG, "board led");
        }

        @Override
        public void onWiimoteData(BalanceBoard wm, BalanceBoard.Data data)
        {
            final float tl = data.getTopLeft();
            final float tr = data.getTopRight();
            final float bl = data.getBottomLeft();
            final float br = data.getBottomRight();
            Log.d(TAG, "board data: " + tl + ", " + tr + ", " + bl + ", " + br);
        }
    };
}
