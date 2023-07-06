package com.example.loginsignup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MenuActivity extends AppCompatActivity {

    private ArrayList<BluetoothDevice> machineList;
    RecyclerView recyclerView;
    private recyclerAdapter.RecyclerViewClickListener listener;
    BluetoothAdapter BTadapter;
    BluetoothLeScanner BTScanner;
    public static final int REQUEST_BT_ENABLE = 1;
    private boolean mScanning = false;
    Button ScanBtn;
    recyclerAdapter adapter;
    LocationManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ActionBar bar = getSupportActionBar();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E4941")));*/

        //bar.setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerView);
        machineList = new ArrayList<>();
        ScanBtn = findViewById(R.id.button);

        BTadapter = BluetoothAdapter.getDefaultAdapter();
        BTScanner = BTadapter.getBluetoothLeScanner();

        setOnClickListener();
        adapter = new recyclerAdapter(machineList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        checkPermissions();

        ScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
                BTadapter = BluetoothAdapter.getDefaultAdapter();
                BTScanner = BTadapter.getBluetoothLeScanner();
                if (mScanning){
                    mScanning = false;
                    scanLeDevice(false);
                    ScanBtn.setText("Scan");
                } else {
                    machineList.clear();
                    adapter.notifyDataSetChanged();
                    checkPermissions();
                    mScanning = true;
                    try{
                    scanLeDevice(true);}
                    catch (Exception e){
                        Log.v("boss", "error due to permissions");
                    }
                    ScanBtn.setText("Stop");
                }
            }
        });
    }
    private void setOnClickListener() {
        listener = new recyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                String macAddress = machineList.get(position).getAddress();
                scanLeDevice(false);
                ScanBtn.setText("Scan");
                machineList.clear();
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(getApplicationContext(), StayUprightActivity.class);

                SharedPreferences sho = getSharedPreferences("SharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sho.edit();
                myEdit.putString("mac", macAddress);

                myEdit.apply();
                //intent.putExtra("mac",macAddress);
                startActivity(intent);
                finish();

            }
        };
    }
    public void scanLeDevice(final boolean enable) {
        if (enable) {
            mScanning = true;
            Log.i("Scanning", "start");
            Toast.makeText(MenuActivity.this, "Starting Scan", Toast.LENGTH_SHORT).show();
            BTScanner.startScan(mLeScanCallback);


        } else {
            Log.i("Scanning", "stop");
            mScanning = false;
            BTScanner.stopScan(mLeScanCallback);
        }
    }

    public void checkPermissions(){
        if (!BTadapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_BT_ENABLE);
        }
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ActivityCompat.requestPermissions(MenuActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(MenuActivity.this, "Enable Location",Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }
    private ScanCallback mLeScanCallback =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, final ScanResult result) {
                    //Toast.makeText(MainActivity.this, "Scanning Devices", Toast.LENGTH_SHORT).show();
                    if(!(machineList.contains(result.getDevice()))
                    //        &&(result.getDevice().getName()=="Neck Band")
                        //    &&(result.getDevice().getAddress()=="FC:82:89:B5:E9:09")

                    ){
                        //Log.v("Device name", result.getDevice().getName());
                        try {
                            if (result.getDevice().getName().equals("Neck Band")) {

                                machineList.add(result.getDevice());
                                adapter.notifyDataSetChanged();
                            }
                        }catch (Exception e){
                            //do nothing
                        }
                    }
                }
                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                    Toast.makeText(MenuActivity.this, "Scan  Failed", Toast.LENGTH_LONG).show();
                    Log.i("BLE", "error");
                }
            };
}