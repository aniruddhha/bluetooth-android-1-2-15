package com.melayer.bluetoothdemo;

import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private BroadcastReceiver receiverFound = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			BluetoothDevice remoteDevice = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			
			Log.i("####### DEVICE NAME #######", "" + remoteDevice.getName());
			Log.i("####### DEVICE ID #######", "" + remoteDevice.getAddress());
		}
	};

	private Button btnStartDiscovery, btnMakeDiscoverable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

		if (adapter == null) {

			Toast.makeText(this, "Buluetooth NA", Toast.LENGTH_LONG).show();
			return;
		}

		if (!adapter.isEnabled()) {

			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, 1234);

		}

		Set<BluetoothDevice> setBondedDevices = adapter.getBondedDevices();

		Iterator<BluetoothDevice> itDevices = setBondedDevices.iterator();

		while (itDevices.hasNext()) {

			BluetoothDevice device = itDevices.next();

			Log.i("###### DEVICE INFO #####", "Name - " + device.getName());
			Log.i("###### DEVICE INFO #####", "ADD - " + device.getAddress());
		}

		btnStartDiscovery = (Button) findViewById(R.id.btnStartDiscovery);
		btnStartDiscovery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				adapter.startDiscovery();
			}
		});

		btnMakeDiscoverable = (Button) findViewById(R.id.btnMakeDiscoverable);
		btnMakeDiscoverable.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent makeItDiscoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				makeItDiscoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				
				startActivity(makeItDiscoverable);
				
				
			}
		});
		IntentFilter filterFound = new IntentFilter();
		filterFound.addAction(BluetoothDevice.ACTION_FOUND);
		registerReceiver(receiverFound, filterFound);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1234) {

			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Enabled", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "NOt Enabled", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
