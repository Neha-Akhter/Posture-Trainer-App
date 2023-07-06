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

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.progressbar.CircleProgressBar;

import org.bson.Document;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;
import static android.bluetooth.BluetoothAdapter.STATE_DISCONNECTED;

public class SignupActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    int pointer;

    Timer timer;
    ScheduledExecutorService service;
    int battery;
    BluetoothGattCharacteristic xCharacteristic;
    BluetoothGattCharacteristic yCharacteristic;
    BluetoothGattCharacteristic batteryCharacteristic;

    MongoDatabase mongoDatabase1;
    MongoClient mongoClient1;
    MongoCollection<Document> mongoCollection1;


    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout mainDrawer;
    private Toolbar appBar;

    private static final String LIST_KEY1="list_key1";
    private static final String LIST_KEY2="list_key2";
    private static final String LIST_KEY3="list_key3";

    String currentDate;


    ArrayList Forwardplotx =new ArrayList();
    ArrayList Backwardplotx =new ArrayList();

    ArrayList Rightploty =new ArrayList();
    ArrayList Leftploty =new ArrayList();


    private static final String LIST_KEY="list_key";

    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;

    App app;
    String Appid = "application-0-dslwi";
    User user;
    byte[] bytesOfX;
    float valueOfX;
    byte[] bytesOfY;
    float valueOfY;
    Long start_time;
    //Long end_time;
    //Long time= start_time-end_time;

    BluetoothGattCallback gattCallback;
    BluetoothDevice myDevice;
    BluetoothGatt bluetoothGatt;
    BluetoothAdapter BTadapter;

    UUID AngleService = UUID.fromString("917649A0-D98E-11E5-9EEC-0002A5D5C51B");
    UUID X_angle_characteristic = UUID.fromString("917649A1-D98E-11E5-9EEC-0002A5D5C51B");
    UUID Y_angle_characteristic = UUID.fromString("917649A2-D98E-11E5-9EEC-0002A5D5C51B");
    UUID Battery_Service = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    UUID Battery_characteristic = UUID.fromString("00002101-0000-1000-8000-00805f9b34fb");

    ArrayList avgPlotx=new ArrayList();

    String[][] nullList = {{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},
            {null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},
            {null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},
            {null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null}};

    String[][] listOfAttributes = {{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},
            {null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},
            {null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},
            {null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null},{null,null,null}};

    String[][] uploadingList;
    ArrayList plotx =new ArrayList();
    ArrayList ploty =new ArrayList();

    ArrayList forwardAngles=new ArrayList();
    ArrayList BackwardAngles=new ArrayList();
    //String[] plotX={null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};
    //String[] plotY={null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null};

    TextView showAngle;
    Button AngleBtn;
    Button Disconnect_Btn;
    String macAddress;
    Button findDevice;

    CircleProgressBar progressOfX;
    CircleProgressBar progressOfY;
    CircleProgressBar circular_hourly;
    CircleProgressBar circular_daily;
    CircleProgressBar circular_weekly;

    TextView hourly_Angle;
    TextView daily_Angle;
    TextView weekly_Angle;
    TextView Yangle;
    TextView batteryPercentage;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_angle);
        ActionBar bar = getSupportActionBar();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1E4941")));
        //getSupportActionBar().setTitle("Dashboard");

        app = new App(new AppConfiguration.Builder(Appid).build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("PostureData");
        mongoCollection = mongoDatabase.getCollection("AngleTimeSpam");


        mongoClient1 = user.getMongoClient("mongodb-atlas");
        mongoDatabase1 = mongoClient1.getDatabase("Trial");
        mongoCollection1 = mongoDatabase1.getCollection("Statistics");

        Gson gson=new Gson();
        String jsonstring=gson.toJson(avgPlotx);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPrefForList",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(LIST_KEY,jsonstring);
        myEdit.apply();



        Disconnect_Btn = findViewById(R.id.button2);
        hourly_Angle = findViewById(R.id.textView3);
        daily_Angle = findViewById(R.id.textView4);
        weekly_Angle = findViewById(R.id.textView5);
        showAngle = findViewById(R.id.textviewOfX);
        findDevice = findViewById(R.id.connect_button);
        Yangle = findViewById(R.id.textviewOfY);
        batteryPercentage = findViewById(R.id.textviewOfBattery);

        progressOfX = findViewById(R.id.line_progressX);
        progressOfX.setMax(360);
        progressOfY = findViewById(R.id.line_progressY);
        progressOfY.setMax(360);

        circular_daily = findViewById(R.id.line_progress_daily);
        circular_daily.setMax(360);
        circular_daily.setProgress(50);
        daily_Angle.setText("50");

        circular_weekly = findViewById(R.id.red_one);
        circular_weekly.setMax(360);
        circular_weekly.setProgress(40);
        weekly_Angle.setText("40");

        circular_hourly = findViewById(R.id.line_progress_hourly);
        circular_hourly.setMax(360);
        circular_hourly.setProgress(30);
        hourly_Angle.setText("30");



        Toolbar appBar = findViewById(R.id.appBar);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(appBar);
        mainDrawer = (DrawerLayout) findViewById(R.id.mainDrawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mainDrawer, appBar, R.string.dash, R.string.dash);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);


        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String emailId = sh.getString("email", null);
        String passId = sh.getString("passw", null);
        Credentials credentials = Credentials.emailPassword(emailId, passId);


        findDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intEnt = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intEnt);
            }
        });

        try {
            Intent intent = getIntent();
            SharedPreferences sho = getSharedPreferences("SharedPref", MODE_PRIVATE);
            macAddress = sho.getString("mac", null);

        } catch (Exception e) {
            Log.v("Intent DAta", "Mac address not found");
        }


        Disconnect_Btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                getDate();
                storing_peak_values(Forwardplotx);
                bluetoothGatt.disconnect();
                //Long difference=System.currentTimeMillis()-start_time;
                //System.out.println(difference);
            }
        });


        AngleBtn = findViewById(R.id.button3);
        AngleBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    BTadapter = BluetoothAdapter.getDefaultAdapter();
                    myDevice = BTadapter.getRemoteDevice(macAddress);
                    bluetoothGatt = myDevice.connectGatt(getApplicationContext(), true, gattCallback);
                    Long start_time=System.currentTimeMillis();
                    //bluetoothGatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Device not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == STATE_CONNECTED) {
                    findDevice.setClickable(false);
                    Disconnect_Btn.setClickable(true);
                    gatt.discoverServices();

                }
                if (newState == STATE_DISCONNECTED) {

                    transferToDatabase(listOfAttributes);
                    service.shutdown();
                    findDevice.setClickable(true);
                    Disconnect_Btn.setClickable(false);
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

                service = Executors.newScheduledThreadPool(7);
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
                                storing_values();
                                //Log.v("fromservices","I am running perfectly");
                            }
                        });

                    }
                }, 1, 1, TimeUnit.SECONDS);


            }

            ;

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (characteristic.getUuid().compareTo(xCharacteristic.getUuid()) == 0) {
                            bytesOfX = characteristic.getValue();
                            valueOfX = ByteBuffer.wrap(bytesOfX).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                            if (valueOfX > 25) {
                                progressOfX.setProgressStartColor(Color.parseColor("#990000"));
                            } else {
                                progressOfX.setProgressStartColor(Color.parseColor("#26AE94"));
                            }
                            progressOfX.setProgress((int) valueOfX);
                            showAngle.setText(String.valueOf(valueOfX));
                        } else if (characteristic.getUuid().compareTo(yCharacteristic.getUuid()) == 0) {
                            bytesOfY = characteristic.getValue();
                            valueOfY = ByteBuffer.wrap(bytesOfY).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                            progressOfY.setProgress((int) valueOfY);
                            Yangle.setText(String.valueOf(valueOfY));
                        } else {
                            battery = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                            batteryPercentage.setText(String.valueOf(battery) + " %");
                        }
                    }
                });
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                bytesOfX = characteristic.getValue();
                valueOfX = ByteBuffer.wrap(bytesOfX).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                showAngle.setText(String.valueOf(valueOfX));
            }
        };

    }
    public void storing_peak_values(ArrayList forwardAngles){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (forwardAngles.size() <= 0) {
                    throw new IllegalArgumentException("The array is empty");
                } else {
                    int max = Integer.MIN_VALUE;
                    for (int i = 0; i < forwardAngles.size(); i++) {
                        if (Integer.valueOf((Integer) forwardAngles.get(i)) > max) {
                            max = (int) forwardAngles.get(i);
                        }
                    }
                    // TRANSFERRING TO ATLAS
                    int finalMax = max;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mongoCollection1.insertOne(new Document("Username", user.getProfile().getEmail()).append("Peak Value", finalMax).append("Date", currentDate))
                                    .getAsync(result -> {
                                        if (result.isSuccess()) {
                                            Log.v("sending", "Peak values");
                                        } else {
                                            Log.v("Failed to send", "Peak values");
                                        }

                                    });
                        }

                    });


                }
            }
        });
    }

    public void getDate(){
        DateFormat Date = DateFormat.getDateInstance();
        Calendar cals = Calendar.getInstance();
        currentDate = Date.format(cals.getTime());
        //System.out.println("Formatted Date: " + currentDate);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void storing_values() {
        if (pointer == 60) {
            pointer = 0;
            uploadingList = listOfAttributes;
            //Log.v("uploading","Going to transfer function");
            getValueForGraphX(uploadingList);
            getValueForGraphY(uploadingList);
            transferToDatabase(uploadingList);
            listOfAttributes = nullList;
            listOfAttributes[pointer][0] = getTime();
            listOfAttributes[pointer][1] = String.valueOf(valueOfX);
            listOfAttributes[pointer][2] = String.valueOf(valueOfY);
            pointer += 1;
        } else {
            listOfAttributes = nullList;
            listOfAttributes[pointer][0] = getTime();
            listOfAttributes[pointer][1] = String.valueOf(valueOfX);
            listOfAttributes[pointer][2] = String.valueOf(valueOfY);
            pointer += 1;


        }
    }

    public void getValueForGraphX(String[][] uploadingList){

        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                Log.v("graph","getting values for plotting");
                for(int j=0;j<uploadingList.length;j++) {
                    if (Float.valueOf(uploadingList[j][1])>0){
                        Forwardplotx.add(Math.round(Float.valueOf(uploadingList[j][1])));
                        Backwardplotx.add(0);
                    }
                    else{
                        Backwardplotx.add(Math.abs(Math.round(Float.valueOf(uploadingList[j][1]))));
                        Forwardplotx.add(0);
                    }
                    //plotx.add(Float.valueOf(uploadingList[j][1]));
                    //ploty.add(Float.valueOf(uploadingList[j][2]));


                }
                Log.v("xyz","ForwardplotX");
                System.out.println(Forwardplotx);
                Log.v("xyz","BackwardplotX");
                System.out.println(Backwardplotx);
                //findAvgX(plotx);
                //plotx.clear();
                //findAvgY(ploty);
                //ploty.clear();
            }

        });

    }

    public void getValueForGraphY(String[][] uploadingList){

        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                Log.v("graph","getting values for plotting");
                for(int j=0;j<uploadingList.length;j++) {
                    if (Float.valueOf(uploadingList[j][2])>0){
                        Rightploty.add(Math.round(Float.valueOf(uploadingList[j][2])));
                        Leftploty.add(0);
                    }
                    else{
                        Leftploty.add(Math.abs(Math.round(Float.valueOf(uploadingList[j][2]))));
                        Rightploty.add(0);
                    }
                    //plotx.add(Float.valueOf(uploadingList[j][1]));
                    //ploty.add(Float.valueOf(uploadingList[j][2]));


                }
                Log.v("xyz","Rightploty");
                System.out.println(Rightploty);
                Log.v("xyz","Leftploty");
                System.out.println(Leftploty);

            }

        });

    }




    public void findAvg(ArrayList plotx) {
        float sum=0;
        for(int j=0;j<plotx.size();j++){
            sum +=((Float) plotx.get(j)); ;
        }
        float result =sum/plotx.size();

        int i = (int) result;
        avgPlotx.add(i);

        Log.v("avg ","it is avg of 60 angles"+avgPlotx);
    }

    public void transferToDatabase(String[][] list1){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.v("inTheDatabase","waiting to go down");
                for(int i=0;i<list1.length;i++){
                    if(!((list1[i][0] == null) || (list1[i][1] == null)|| (list1[i][2] == null))){
                        mongoCollection.insertOne(new Document("Username",user.getProfile().getEmail()).append("Time",list1[i][0]).append("X Angle Value",list1[i][1]).append("Y Angle Value",list1[i][2]))

                                .getAsync(result ->{if(result.isSuccess()){
                                    //Toast.makeText(showAngle.this, "Successful", Toast.LENGTH_SHORT).show();
                                    Log.v("uploading","successfull");
                                }
                                else{
                                    //Toast.makeText(showAngle.this, "Not Successful", Toast.LENGTH_SHORT).show();
                                    Log.v("Uploading","not successful");
                                }

                                });
                        //Log.v("uploading","uploading");
                    }


                }
                //Log.v("atTheendOfDatabase","I think successfully uploaded");
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



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.stats:
                Gson gson=new Gson();
                String jsonstring=gson.toJson(Forwardplotx);
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPrefForList",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString(LIST_KEY,jsonstring);
                myEdit.apply();

                Gson gson1=new Gson();
                String jsonstring1=gson1.toJson(Backwardplotx);
                SharedPreferences sharedPreferences1 = getSharedPreferences("MySharedPrefForList1",MODE_PRIVATE);
                SharedPreferences.Editor myEdit1 = sharedPreferences1.edit();
                myEdit1.putString(LIST_KEY1,jsonstring1);
                myEdit1.apply();

                Gson gson2=new Gson();
                String jsonstring2=gson2.toJson(Rightploty);
                SharedPreferences sharedPreferences2 = getSharedPreferences("MySharedPrefForList2",MODE_PRIVATE);
                SharedPreferences.Editor myEdit2 = sharedPreferences2.edit();
                myEdit2.putString(LIST_KEY2,jsonstring2);
                myEdit2.apply();

                Gson gson3=new Gson();
                String jsonstring3=gson3.toJson(Leftploty);
                SharedPreferences sharedPreferences3 = getSharedPreferences("MySharedPrefForList3",MODE_PRIVATE);
                SharedPreferences.Editor myEdit3 = sharedPreferences3.edit();
                myEdit3.putString(LIST_KEY3,jsonstring3);
                myEdit3.apply();

                Intent intent=new Intent(getApplicationContext(),ChartsActivity.class);
                startActivity(intent);
                break;
            case R.id.rate:
                alertDialog();
                break;
            case R.id.logout:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        app.currentUser().logOutAsync(new App.Callback<User>() {
                            @Override
                            public void onResult(App.Result<User> result) {
                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("email", null);
                                myEdit.putString("passw", null);
                                myEdit.apply();
                                if(result.isSuccess()){
                                    Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });}});
                break;
            default:
                return true;
        }
        return false;
    }

    private void alertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        final RatingBar ratingBar = new RatingBar(getApplicationContext());
        ratingBar.setMax(4);
        ratingBar.setRating(0);
        ratingBar.setStepSize(0);
        ratingBar.setNumStars(5);
        dialog.setMessage("If you like our App, then rate us");
        dialog.setTitle("RATE US");

        dialog.setView(ratingBar);
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Toast.makeText(getApplicationContext(), "Yes is clicked", Toast.LENGTH_LONG).show();
                    }
                });
        dialog.setNegativeButton("cancel", null);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = 560;
        lp.height = 500;
        lp.x=-170;
        lp.y=100;
        alertDialog.getWindow().setAttributes(lp);
    }



    public void Share(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT,"https://developer.android.com/studio/intro"); //app link
        Intent.createChooser(sendIntent,"Share via");
        startActivity(sendIntent);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}