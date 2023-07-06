package com.example.loginsignup;
//"mongodb+srv://cluster0.5djwa.mongodb.net/PostureData" --username nehaboss
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.material.navigation.NavigationView;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.bson.types.ObjectId;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static android.bluetooth.BluetoothAdapter.STATE_CONNECTING;
import static android.bluetooth.BluetoothAdapter.STATE_DISCONNECTED;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class showAngle extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RatingDialogListener {

    boolean onConnected=false;
    boolean doubleBackToExitPressedOnce = false;

    App app;
    String Appid = "application-0-dslwi";
    User user;
    Realm backgroundThreadRealm;
    //mongo "mongodb+srv://cluster0.5djwa.mongodb.net/PostureData" --username nehaboss
    ScheduledExecutorService service;

    String partitionValue, macAddress;

    int clickCount=0;
    int battery, pointer;

    float FP=0;
    float BP=0;
    float RP=0;
    float LP=0;
    float FP_db=0;
    float BP_db=0;
    float RP_db=0;
    float LP_db=0;
    float FP_final=0;
    float BP_final=0;
    float RP_final=0;
    float LP_final=0;

    Float maxProgress;

    byte[] bytesOfX;
    float valueOfX;
    byte[] bytesOfY;
    float valueOfY;

    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout mainDrawer;

    BluetoothGattCallback gattCallback;
    BluetoothDevice myDevice;
    BluetoothGatt bluetoothGatt;
    BluetoothAdapter BTadapter;
    BluetoothGattCharacteristic xCharacteristic, yCharacteristic, batteryCharacteristic;

    UUID AngleService = UUID.fromString("917649A0-D98E-11E5-9EEC-0002A5D5C51B");
    UUID X_angle_characteristic = UUID.fromString("917649A1-D98E-11E5-9EEC-0002A5D5C51B");
    UUID Y_angle_characteristic = UUID.fromString("917649A2-D98E-11E5-9EEC-0002A5D5C51B");
    UUID Battery_Service = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    UUID Battery_characteristic = UUID.fromString("00002101-0000-1000-8000-00805f9b34fb");


    String[][] nullList = {{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},
            {null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},
            {null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},
            {null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null}};


    String[][] listOfAttributes = {{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},
            {null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},
            {null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},
            {null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null},{null,null,null,null,null,null}};

    String[][] uploadingList;

    Button AngleBtn, visibilityButton, Disconnect_Btn, findDevice;

    CircleProgressBar yellow_circular, green_circular, red_circular;

    TextRoundCornerProgressBar progressBarOfX, progressBarOfXnegative, progressBarOfY, progressBarOfYnegative;

    TextView hourly_Angle, daily_Angle, weekly_Angle, Yangle, batteryPercentage, showAngle, forward,backward,right,left, startTextView;

    private boolean buttonState = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_angle);
        ActionBar bar = getSupportActionBar();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Initializing Realm and Background thread for data transfers
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(Appid).build());
        partitionValue = "My project";
        user = app.currentUser();
        backgroundThreadRealm = Realm.getDefaultInstance();

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String emailId = sh.getString("email", null);
        String passId = sh.getString("passw", null);
        Credentials credentials = Credentials.emailPassword(emailId, passId);
        try {
            Intent intent = getIntent();
            SharedPreferences sho = getSharedPreferences("SharedPref", MODE_PRIVATE);
            macAddress = sho.getString("mac", null);
        } catch (Exception e) {
            Log.v("Intent DAta", "Mac address not found");
        }


        visibilityButton = findViewById(R.id.button82);
        startTextView = findViewById(R.id.lastText);
        Disconnect_Btn = findViewById(R.id.button2);
        hourly_Angle = findViewById(R.id.textView3);
        daily_Angle = findViewById(R.id.textView4);
        weekly_Angle = findViewById(R.id.textView5);
        showAngle = findViewById(R.id.textviewOfX);
        findDevice = findViewById(R.id.connect_button);
        Yangle = findViewById(R.id.textviewOfY);
        batteryPercentage = findViewById(R.id.textviewOfBattery);
        forward = findViewById(R.id.textView16);
        backward = findViewById(R.id.textView50);
        right = findViewById(R.id.textView51);
        left = findViewById(R.id.textView52);
        AngleBtn = findViewById(R.id.button3);

        progressBarOfX = findViewById(R.id.linearBarX);
        progressBarOfX.setMax(180);
        progressBarOfXnegative=findViewById(R.id.linearBarXnegative);
        progressBarOfXnegative.setMax(180);
        progressBarOfY = findViewById(R.id.barOfY);
        progressBarOfY.setMax(180);
        progressBarOfYnegative= findViewById(R.id.negativeY);
        progressBarOfYnegative.setMax(180);

        red_circular = findViewById(R.id.red_one);
        yellow_circular=findViewById(R.id.yellow_one);
        green_circular=findViewById(R.id.green_one);
        green_circular.setMax(100);
        yellow_circular.setMax(100);
        red_circular.setMax(100);
        green_circular.setProgressStartColor(Color.parseColor("#02A162"));
        green_circular.setProgressEndColor(Color.parseColor("#02A162"));
        yellow_circular.setProgressStartColor(Color.parseColor("#EFB700"));
        yellow_circular.setProgressEndColor(Color.parseColor("#EFB700"));
        red_circular.setProgressStartColor(Color.parseColor("#F1394D"));
        red_circular.setProgressEndColor(Color.parseColor("#F1394D"));

        progressBarOfX.setVisibility(View.GONE);
        progressBarOfXnegative.setVisibility(View.GONE);
        progressBarOfY.setVisibility(View.GONE);
        progressBarOfYnegative.setVisibility(View.GONE);
        forward.setVisibility(View.GONE);
        backward.setVisibility(View.GONE);
        left.setVisibility(View.GONE);
        right.setVisibility(View.GONE);

        visibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonState){
                    red_circular.setVisibility(View.GONE);
                    yellow_circular.setVisibility(View.GONE);
                    green_circular.setVisibility(View.GONE);
                    startTextView.setVisibility(View.GONE);
                    progressBarOfX.setVisibility(View.VISIBLE);
                    progressBarOfXnegative.setVisibility(View.VISIBLE);
                    progressBarOfY.setVisibility(View.VISIBLE);
                    progressBarOfYnegative.setVisibility(View.VISIBLE);
                    forward.setVisibility(View.VISIBLE);
                    backward.setVisibility(View.VISIBLE);
                    left.setVisibility(View.VISIBLE);
                    right.setVisibility(View.VISIBLE);
                    buttonState=false;
                    visibilityButton.setText("Show Less");
                }
                else{
                    red_circular.setVisibility(View.VISIBLE);
                    yellow_circular.setVisibility(View.VISIBLE);
                    green_circular.setVisibility(View.VISIBLE);
                    startTextView.setVisibility(View.VISIBLE);
                    progressBarOfX.setVisibility(View.GONE);
                    progressBarOfXnegative.setVisibility(View.GONE);
                    progressBarOfY.setVisibility(View.GONE);
                    progressBarOfYnegative.setVisibility(View.GONE);
                    forward.setVisibility(View.GONE);
                    backward.setVisibility(View.GONE);
                    left.setVisibility(View.GONE);
                    right.setVisibility(View.GONE);
                    visibilityButton.setText("Show More");
                    buttonState=true;
                }
            }
        });

        Toolbar appBar = findViewById(R.id.appBar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(appBar);
        mainDrawer = (DrawerLayout) findViewById(R.id.mainDrawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mainDrawer, appBar, R.string.dash, R.string.dash);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);


        startTextView.setText("Connect");
        findDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intEnt = new Intent(showAngle.this, MenuActivity.class);
                startActivity(intEnt);
            }
        });

        gattCallback = new BluetoothGattCallback() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == STATE_CONNECTED) {
                    //StartTime=new SimpleDateFormat().format(Calendar.getInstance().getTime());
                    //Long S_time= Long.parseLong(StartTime);
                    findDevice.setClickable(false);
                    Disconnect_Btn.setClickable(true);
                    for(int i=0;i<60;i++){
                        listOfAttributes[i][0]=null;
                        listOfAttributes[i][1]=null;
                        listOfAttributes[i][2]=null;
                        listOfAttributes[i][3]=null;
                        listOfAttributes[i][4]=null;
                        listOfAttributes[i][5]=null;
                        onConnected=true;
                    }
                    System.out.println(Arrays.deepToString(listOfAttributes));

                    gatt.discoverServices();

                }
                if (newState == STATE_CONNECTING){
                    startTextView.setText("Connecting");
                }
                if (newState == STATE_DISCONNECTED) {
//                    transferToDatabaseRealtime(listOfAttributes);
//                    transferToDb();
                    service.shutdown();
                    findDevice.setClickable(true);
                    Disconnect_Btn.setClickable(false);
                    //startTextView.setText("Connect");

//                    green_circular.setProgress(0);
//                    yellow_circular.setProgress(0);
//                    red_circular.setProgress(0);
//                    progressBarOfX.setProgress(0);
//                    //progressBarOfX.setProgressText("0");
//                    progressBarOfXnegative.setProgress(0);
//                    // progressBarOfXnegative.setProgressText("0");
//                    progressBarOfY.setProgress(0);
//                    // progressBarOfY.setProgressText("0");
//                    progressBarOfYnegative.setProgress(0);
//                    //  progressBarOfYnegative.setProgressText("0");
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                xCharacteristic = gatt.getService(AngleService).getCharacteristic(X_angle_characteristic);
                yCharacteristic = gatt.getService(AngleService).getCharacteristic(Y_angle_characteristic);
                batteryCharacteristic = gatt.getService(Battery_Service).getCharacteristic(Battery_characteristic);
                ArrayList<BluetoothGattCharacteristic> characteristicArrayList = new ArrayList<BluetoothGattCharacteristic>();
                characteristicArrayList.add(xCharacteristic);
                characteristicArrayList.add(yCharacteristic);
                characteristicArrayList.add(batteryCharacteristic);

                //service = Executors.newScheduledThreadPool(10);
                service=Executors.newScheduledThreadPool(7);
                service.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        //gatt.readCharacteristic(xCharacteristic);
                        while (true) {
                            for (int i = 0; i < 3; i++) {
                                gatt.readCharacteristic(characteristicArrayList.get(i));

                            }
                        }
                    }
                }, 1, 1, TimeUnit.SECONDS);
                service.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void run() {
                                try {
                                    storing_values();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //Log.v("fromservices","I am running perfectly");
                            }
                        });

                    }
                }, 1, 1, TimeUnit.SECONDS);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (characteristic.getUuid().compareTo(xCharacteristic.getUuid()) == 0) {
                            bytesOfX = characteristic.getValue();
                            valueOfX = ByteBuffer.wrap(bytesOfX).order(ByteOrder.LITTLE_ENDIAN).getFloat();
//                            if(valueOfX>=-25 && valueOfX<=25){
//                                startTextView.setText("Posture Correct");
//                            }else{
//                                startTextView.setText("Warning!!!! Posture not Correct");
//                            }
                            if(valueOfX>=0){
                                if(valueOfX<=25){
                                    progressBarOfX.setProgressColor(Color.parseColor("#02A162"));

                                }
                                else{
//                                    progressBarOfX.setProgressColor(Color.parseColor("#F44336"));
                                    progressBarOfX.setProgressColors(showAngle.this.getResources().getIntArray(R.array.progress_gradient));
                                }
                                progressBarOfXnegative.setProgress(0);
                                progressBarOfXnegative.setProgressText("0");
                                progressBarOfX.setProgress(Math.abs(valueOfX));
                                progressBarOfX.setProgressText(String.valueOf(Math.abs(valueOfX)));
                            }
                            else{
                                if(Math.abs(valueOfX)<=25){
                                    progressBarOfXnegative.setProgressColor(Color.parseColor("#02A162"));
                                }
                                else{
//                                    progressBarOfX.setProgressColor(Color.parseColor("#F44336"));
                                    progressBarOfXnegative.setProgressColors(showAngle.this.getResources().getIntArray(R.array.progress_gradient));
                                }
                                progressBarOfX.setProgress(0);
                                progressBarOfX.setProgressText("0");
                                progressBarOfXnegative.setProgress(Math.abs(valueOfX));
                                progressBarOfXnegative.setProgressText(String.valueOf(Math.abs(valueOfX)));
                            }
//                            showAngle.setText(String.valueOf(valueOfX));
                        } else if (characteristic.getUuid().compareTo(yCharacteristic.getUuid()) == 0) {
                            bytesOfY = characteristic.getValue();
                            valueOfY = ByteBuffer.wrap(bytesOfY).order(ByteOrder.LITTLE_ENDIAN).getFloat();
//                            progressOfY.setProgress((int) valueOfY);
//                            if(valueOfY>=-25 && valueOfY<=25){
//                                startTextView.setText("Posture Correct");
//                            }else{
//                                startTextView.setText("Warning!!!! Posture not Correct");
//                            }
                            if(valueOfY>=0){
                                if(valueOfY<=25){
                                    progressBarOfY.setProgressColor(Color.parseColor("#02A162"));
                                }
                                else{
//                                    progressBarOfX.setProgressColor(Color.parseColor("#F44336"));
                                    progressBarOfY.setProgressColors(showAngle.this.getResources().getIntArray(R.array.progress_gradient));
                                }
                                progressBarOfYnegative.setProgress(0);
                                progressBarOfYnegative.setProgressText("0");
                                progressBarOfY.setProgress(Math.abs(valueOfY));
                                progressBarOfY.setProgressText(String.valueOf(Math.abs(valueOfY)));
                            }
                            else{
                                if(Math.abs(valueOfY)<=25){
                                    progressBarOfYnegative.setProgressColor(Color.parseColor("#02A162"));
                                }
                                else{
//                                    progressBarOfX.setProgressColor(Color.parseColor("#F44336"));
                                    progressBarOfYnegative.setProgressColors(showAngle.this.getResources().getIntArray(R.array.progress_gradient));
                                }
                                progressBarOfY.setProgress(0);
                                progressBarOfY.setProgressText("0");
                                progressBarOfYnegative.setProgress(Math.abs(valueOfY));
                                progressBarOfYnegative.setProgressText(String.valueOf(Math.abs(valueOfY)));
                            }
//                            Yangle.setText(String.valueOf(valueOfY));
                        } else {
                            battery = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);

                            batteryPercentage.setText(String.valueOf(battery) + " % ");
                        }
                        maxProgress = Math.max(progressBarOfX.getProgress(),Math.max(progressBarOfXnegative.getProgress(),Math.max(progressBarOfY.getProgress(),progressBarOfYnegative.getProgress())));
                        if(maxProgress<30){
                            green_circular.setProgress(Math.round(maxProgress));
                            yellow_circular.setProgress(0);
                            red_circular.setProgress(0);
                            startTextView.setText("Correct");
                        }
                        else if(maxProgress<60){
                            green_circular.setProgress(30);
                            yellow_circular.setProgress(Math.round(maxProgress));
                            red_circular.setProgress(0);
                            startTextView.setText("Critical");
                        }else{
                            green_circular.setProgress(30);
                            yellow_circular.setProgress(60);
                            red_circular.setProgress(Math.round(maxProgress));
                            startTextView.setText("Warning");
                        }
//                        if((valueOfX>=-25 && valueOfX<=25) && (valueOfY>=-25 && valueOfY<=25)){
//                            startTextView.setText("Posture is Correct");
//                        }
//                        else{
//                            startTextView.setText("Warning!! Posture is incorrect");
//                        }
                    }
                });
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                bytesOfX = characteristic.getValue();
                valueOfX = ByteBuffer.wrap(bytesOfX).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                showAngle.setText(String.valueOf(valueOfX));
//                progressBarOfX.setVisibility(View.GONE);
            }
        };

        try {
            BTadapter = BluetoothAdapter.getDefaultAdapter();
            myDevice = BTadapter.getRemoteDevice(macAddress);
            Log.v("device name",myDevice.getName());
            bluetoothGatt = myDevice.connectGatt(showAngle.this, false, gattCallback);
            clickCount+=1;
        } catch (Exception e) {
            Toast.makeText(showAngle.this, "Connect Device", Toast.LENGTH_SHORT).show();
            clickCount -= 1;
            SharedPreferences sho1 = getSharedPreferences("SharedPref",MODE_PRIVATE);
            SharedPreferences.Editor myeDit = sho1.edit();
            myeDit.putString("mac", null);
            myeDit.apply();
        }

        Disconnect_Btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try{
                    bluetoothGatt.disconnect();
                    transferToDatabaseRealtime(listOfAttributes);
                    transferToDb();
                }catch (Exception e){
                    Toast.makeText(com.example.loginsignup.showAngle.this, "No device connected", Toast.LENGTH_SHORT).show();
                }
                SharedPreferences sho1 = getSharedPreferences("SharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myeDit = sho1.edit();
                myeDit.putString("mac", null);
                myeDit.apply();

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void storing_values() throws InterruptedException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pointer == 60) {
                    pointer = 0;
                    uploadingList = listOfAttributes;
                    //Checking Connectivity
//                    ConnectivityManager cm = (ConnectivityManager) showAngle.this.getSystemService(Context.CONNECTIVITY_SERVICE);
//                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();


                    transferToDatabaseRealtime(uploadingList);
                    listOfAttributes = nullList;
                    for(int i=0;i<60;i++){
                        listOfAttributes[i][0]=null;
                        listOfAttributes[i][1]=null;
                        listOfAttributes[i][2]=null;
                        listOfAttributes[i][3]=null;
                        listOfAttributes[i][4]=null;
                        listOfAttributes[i][5]=null;
                    }
                    System.out.println(Arrays.deepToString(listOfAttributes));
                    //listOfAttributes[pointer][0] = getTime();
                    listOfAttributes[pointer][0] = gettingTime();
                    listOfAttributes[pointer][1] = String.valueOf(progressBarOfX.getProgress());
                    listOfAttributes[pointer][2] = String.valueOf(progressBarOfXnegative.getProgress());
                    listOfAttributes[pointer][3] = String.valueOf(progressBarOfY.getProgress());
                    listOfAttributes[pointer][4] = String.valueOf(progressBarOfYnegative.getProgress());
                    listOfAttributes[pointer][5] = gettingYearMonth();
                    pointer += 1;
                } else {
                    listOfAttributes = nullList;
                    //listOfAttributes[pointer][0] = getTime();
                    listOfAttributes[pointer][0] = gettingTime();
                    listOfAttributes[pointer][1] = String.valueOf(progressBarOfX.getProgress());
                    listOfAttributes[pointer][2] = String.valueOf(progressBarOfXnegative.getProgress());
                    listOfAttributes[pointer][3] = String.valueOf(progressBarOfY.getProgress());
                    listOfAttributes[pointer][4] = String.valueOf(progressBarOfYnegative.getProgress());
                    listOfAttributes[pointer][5] = gettingYearMonth();
                    pointer += 1;
                    //Log.v("abcd","in the else of storing values");

                }
            }
        });
    }

    public void transferToDatabaseRealtime(String[][] list1){
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Log.v("inTheDatabase","waiting to go down");
                System.out.println(Arrays.deepToString(list1));
                for(int i=0;i<list1.length;i++){
                    if(!((list1[i][0] == null) || (list1[i][1] == null)|| (list1[i][2] == null))){
                        if(FP< Float.parseFloat(list1[i][1])){
                            FP=Float.parseFloat(list1[i][1]);
                        }
                        if(BP< Float.parseFloat(list1[i][2])){
                            BP=Float.parseFloat(list1[i][2]);
                        }
                        if(RP< Float.parseFloat(list1[i][3])){
                            RP=Float.parseFloat(list1[i][3]);
                        }
                        if(LP< Float.parseFloat(list1[i][4])){
                            LP=Float.parseFloat(list1[i][4]);
                        }
                        AnglePOJO data=new AnglePOJO(
                                new ObjectId(),partitionValue,user.getProfile().getEmail(),list1[i][0],getDate(),
                                list1[i][1],list1[i][2],list1[i][3],list1[i][4],list1[i][5]);
                        backgroundThreadRealm.executeTransaction (transactionRealm -> {
                            transactionRealm.insert(data);
                        });

                    }


                }
                //Log.v("atTheendOfDatabase","I think successfully uploaded");
                System.out.println(FP);
                System.out.println(BP);
                System.out.println(RP);
                System.out.println(LP);
                System.out.println(gettingYearMonth());
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTime(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stats:
                // backgroundThreadRealm.close();
                if (clickCount!=0){
                    //transferPeakTodb();
                    transferToDb();
                    //backgroundThreadRealm.close();
                }
                Intent intent=new Intent(getApplicationContext(),anglesValue.class);
                startActivity(intent);
                break;
            case R.id.rate:
                showDialog();
                break;

            case R.id.logout:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                        bluetoothGatt.disconnect();}
                        catch (Exception e){
                            Log.v("aby yaar","No device Connected");
                        }
//                        try{
//                            backgroundThreadRealm.close();
//                            Realm.deleteRealm(Realm.getDefaultConfiguration());
//
//                            app.currentUser().logOutAsync(new App.Callback<User>() {
//                                @Override
//                                public void onResult(App.Result<User> result) {
//                                    if(!result.isSuccess()){
//                                        Toast.makeText(showAngle.this, "Error logging out. Please try again later", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                }
//                            });
//
//                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
//                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                            myEdit.putString("email", null);
//                            myEdit.putString("passw", null);
//                            myEdit.apply();
//                            Toast.makeText(showAngle.this, "Logged out", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(showAngle.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                        catch (Exception e){
//                            e.printStackTrace();
//                            Toast.makeText(showAngle.this, "Failed to logout. Make sure you have an active internet connection", Toast.LENGTH_SHORT).show();
//                            backgroundThreadRealm = Realm.getDefaultInstance();
//                            Toast.makeText(showAngle.this, "Connect to the internet and try again after a few seconds", Toast.LENGTH_LONG).show();
//                        }

                        //Checking Connectivity
                    ConnectivityManager cm = (ConnectivityManager) showAngle.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                    if(isConnected){
                        app.currentUser().logOutAsync(new App.Callback<User>() {
                            @Override
                            public void onResult(App.Result<User> result) {
                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("email", null);
                                myEdit.putString("passw", null);
                                myEdit.apply();
                                if(result.isSuccess()){
//                                    SharedPreferences sharedPreferences30 = getSharedPreferences("MySharedPref",MODE_PRIVATE);
//                                    SharedPreferences.Editor myEdit30 = sharedPreferences30.edit();
//                                    myEdit30.putString("email", null);
//                                    myEdit30.putString("passw", null);
//                                    myEdit30.apply();
                                    backgroundThreadRealm.close();
                                    Realm.deleteRealm(Realm.getDefaultConfiguration());
                                    Toast.makeText(showAngle.this, "Logged out", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(showAngle.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
//                                    Intent intent = new Intent(showAngle.this, MainActivity.class);
//                                    startActivity(intent);
//                                    finish();
                                    Toast.makeText(showAngle.this, "Failed to logout. Please try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(showAngle.this, "Failed to logout. Make sure you are connected to the internet", Toast.LENGTH_SHORT).show();
                    }

                    }});
                break;
            default:
                return true;
        }
        return false;
    }

    private void alertDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final RatingBar ratingBar = new RatingBar(getApplicationContext());
        ratingBar.setMax(5);
      //  ratingBar.setRating(0);
       // ratingBar.setStepSize(2);
     //   ratingBar.setNumStars(5);

      /*  dialog.setMessage("If you like our App, then rate us");
        dialog.setTitle("RATE US");

        dialog.setView(ratingBar);
        dialog.setPositiveButton("Ok",

                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Toast.makeText(getApplicationContext(), "We appreciate your response, Thank you", Toast.LENGTH_LONG).show();
                    }
                });
       dialog.setNegativeButton("Not sure", null);*/

       /* AlertDialog alertDialog = dialog.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = 490;
        lp.height = 500;
        lp.x=170;
        lp.y=100;
        alertDialog.getWindow().setAttributes(lp);
        alertDialog.show();*/

       ViewGroup.LayoutParams params  =  ratingBar.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;

        ratingBar.setLayoutParams(params);
        dialog.show();
//        alertDialog.getWindow().setAttributes(lp);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String gettingTime(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedtime = myDateObj.format(myFormatObj);
        System.out.println(formattedtime);
        return formattedtime;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date=dateFormat.format(new Date());
        System.out.println(date);

        return date;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clickCount!=0){
            try{
                //transferPeakTodb();
                //transferToDb();
                bluetoothGatt.disconnect();}
            catch (Exception e){
                Log.v("error in destroy","but handled");
            }
            //backgroundThreadRealm.close();
        }
        backgroundThreadRealm.close();
        //Realm.removeDefaultConfiguration();
        //super.onDestroy();
    }

    private void showDialog() {
        new AppRatingDialog.Builder().setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setDefaultRating(2)
                .setNumberOfStars(5)
                .setTitle("Rate Us")
                .setDescription("Please rate and give your feedback")
                .setCommentInputEnabled(true)
                .setStarColor(R.color.barcolor)
             //   .setNoteDescriptionTextColor(R.color.white)
                .setTitleTextColor(R.color.barcolor)
                .setDescriptionTextColor(R.color.barcolor)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.button_selectorcolor)
                .setCommentTextColor(R.color.black)
                .setCommentBackgroundColor(R.color.white_greyish)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(showAngle.this)
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void transferToDb(){
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                PeakPOJO peakAngle;
                Log.v("pwka","above angle");
                peakAngle = backgroundThreadRealm.where(PeakPOJO.class).equalTo("name",user.getProfile().getEmail()).equalTo("date",getDate()).findFirst();
                Log.v("pwka","if");
                if(peakAngle == null){
                    Log.v("pwka","inside if");
                    PeakPOJO newPeak = new PeakPOJO( new ObjectId(),partitionValue,user.getProfile().getEmail(),gettingTime(),getDate(),
                            String.valueOf(FP),String.valueOf(BP),String.valueOf(RP),String.valueOf(LP),gettingYearMonth());
                    backgroundThreadRealm.executeTransaction (transactionRealm -> {
                        transactionRealm.insert(newPeak);
                    });
                }
                else{
                    Log.v("pwka","inside else");
                    FP_db=Float.parseFloat(peakAngle.getForwardAngle());
                    BP_db=Float.parseFloat(peakAngle.getBackwardAngle());
                    RP_db=Float.parseFloat(peakAngle.getRightAngle());
                    LP_db=Float.parseFloat(peakAngle.getLeftAngle());
                    if(FP_db>FP){
                        FP_final=FP_db;
                    }
                    else{
                        FP_final=FP;
                    }
                    if(BP_db>BP){
                        BP_final=BP_db;
                    }
                    else{
                        BP_final=BP;
                    }
                    if(RP_db>RP){
                        RP_final=RP_db;
                    }
                    else{
                        RP_final=RP;
                    }
                    if(LP_db>LP){
                        LP_final=LP_db;
                    }
                    else{
                        LP_final=LP;
                    }

                    backgroundThreadRealm.executeTransaction( transactionRealm -> {
                        PeakPOJO modifiedPeakAngle = backgroundThreadRealm.where(PeakPOJO.class).equalTo("name",user.getProfile().getEmail()).equalTo("date",getDate()).findFirst();
                        modifiedPeakAngle.setBackwardAngle(String.valueOf(BP_final));
                        modifiedPeakAngle.setForwardAngle(String.valueOf(FP_final));
                        modifiedPeakAngle.setLeftAngle(String.valueOf(LP_final));
                        modifiedPeakAngle.setRightAngle(String.valueOf(RP_final));
                        modifiedPeakAngle.setTime(gettingTime());
                        modifiedPeakAngle.setMonthYear(gettingYearMonth());
                    });

                }

            }
        });


    }

     @RequiresApi(api = Build.VERSION_CODES.O)
     public String gettingYearMonth(){
         YearMonth ym = YearMonth.now();
         String MonthYear= null;
         if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
             MonthYear = ym.format(DateTimeFormatter.ofPattern("MM yyyy"));
         }
         return MonthYear;

}

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            // bluetoothGatt.disconnect();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void storing_values_realtime() throws InterruptedException {
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Log.v("inTheDatabase","waiting to go down");
                AnglePOJO data=new AnglePOJO(
                        new ObjectId(),partitionValue,user.getProfile().getEmail(),gettingTime(),getDate(),
                        String.valueOf(progressBarOfX.getProgress()),String.valueOf(progressBarOfXnegative.getProgress()),
                        String.valueOf(progressBarOfY.getProgress()),String.valueOf(progressBarOfYnegative.getProgress()),
                        gettingYearMonth());

                backgroundThreadRealm.executeTransaction (transactionRealm -> {
                    Log.v("values", "sending values");
                    transactionRealm.insert(data);
                });
            }
        });
    }

    @Override
    public void onPositiveButtonClicked(int rate, String comment) {

        Toast.makeText(getApplicationContext(), "We appreciate your response, Thank you", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }


}