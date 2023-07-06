package com.example.loginsignup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.sync.SyncConfiguration;

import static com.mongodb.client.model.Sorts.descending;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class anglesValue extends AppCompatActivity {
    Dialog myDialog;
    LinearLayout daily,weekly,monthly,yearly;
    ArrayList graphF = new ArrayList();
    ArrayList graphB = new ArrayList();
    ArrayList graphL = new ArrayList();
    ArrayList graphR = new ArrayList();
    ArrayList peakDay=new ArrayList();
    ArrayList peakWeek=new ArrayList();
    ArrayList peakMonth=new ArrayList();
    private static final String LIST_KEY="list_key";
    private static final String LIST_KEY1="list_key1";
    private static final String LIST_KEY2="list_key2";
    private static final String LIST_KEY3="list_key3";
    private static final String LIST_KEY4="list_key4";
    private static final String LIST_KEY5="list_key5";

//    String Appid = "integration-lhkmq";
//    MongoDatabase mongoDatabase;
//    MongoClient mongoClient;
//    MongoCollection<Document> mongoCollection;
//    App app = new App(new AppConfiguration.Builder(Appid).build());
Realm backgroundThreadRealm;
    String partitionValue;
    String Appid = "application-0-dslwi";
    App app;
    User user;
    RealmQuery data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(Appid).build());
        partitionValue = "My project";
        user = app.currentUser();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        backgroundThreadRealm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_angles_value);

       /* myDialog = new Dialog(this);
        daily = findViewById(R.id.daily);
        daily.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                TextView txtclose,textView14;
                Button btnry,btnly,btnfx,btnbx;
                myDialog.setContentView(R.layout.popup);
                txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
                textView14 =(TextView) myDialog.findViewById(R.id.textView14);
                retrieveFromDB();

                btnry = (Button) myDialog.findViewById(R.id.btnry);
                btnry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Gson gson2=new Gson();
                        String jsonstring2=gson2.toJson(graphR);
                        SharedPreferences sharedPreferences2 = getSharedPreferences("MySharedPrefForList2",MODE_PRIVATE);
                        SharedPreferences.Editor myEdit2 = sharedPreferences2.edit();
                        myEdit2.putString(LIST_KEY2,jsonstring2);
                        myEdit2.apply();
                        Intent intent = new Intent(getApplicationContext(),RightActivity.class);
                        startActivity(intent);
                    }
                });

                btnly = (Button) myDialog.findViewById(R.id.btnly);
                btnly.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Gson gson3=new Gson();
                        String jsonstring3=gson3.toJson(graphL);
                        SharedPreferences sharedPreferences3 = getSharedPreferences("MySharedPrefForList3",MODE_PRIVATE);
                        SharedPreferences.Editor myEdit3 = sharedPreferences3.edit();
                        myEdit3.putString(LIST_KEY3,jsonstring3);
                        myEdit3.apply();
                        Intent intent = new Intent(getApplicationContext(),LeftActivity.class);
                        startActivity(intent);


                    }
                });

                btnfx = (Button) myDialog.findViewById(R.id.btnfx);
                btnfx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Gson gson=new Gson();
                        String jsonstring=gson.toJson(graphF);
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPrefForList",MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString(LIST_KEY,jsonstring);
                        myEdit.apply();

                        Intent intent = new Intent(getApplicationContext(),ForwardActivity.class);
                        startActivity(intent);
                    }
                });

                btnbx = (Button) myDialog.findViewById(R.id.btnbx);
                btnbx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Gson gson1=new Gson();
                        String jsonstring1=gson1.toJson(graphB);
                        SharedPreferences sharedPreferences1 = getSharedPreferences("MySharedPrefForList1",MODE_PRIVATE);
                        SharedPreferences.Editor myEdit1 = sharedPreferences1.edit();
                        myEdit1.putString(LIST_KEY1,jsonstring1);
                        myEdit1.apply();
                        Intent intent = new Intent(getApplicationContext(),BackwardActivity.class);
                        startActivity(intent);
                    }
                });

                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();


            }
        });*/
        weekly=findViewById(R.id.week);
        weekly.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //backgroundThreadRealm.close();
                /*retrievePeakWeekly();
                Gson gson4=new Gson();
                String jsonstring4=gson4.toJson(peakWeek);
                SharedPreferences sharedPreferences4 = getSharedPreferences("MySharedPrefForList4",MODE_PRIVATE);
                SharedPreferences.Editor myEdit4 = sharedPreferences4.edit();
                myEdit4.putString(LIST_KEY4,jsonstring4);
                myEdit4.apply();*/
                Intent intent = new Intent(getApplicationContext(),weeklyGraph.class);
                startActivity(intent);
                finish();

            }
        });
        monthly=findViewById(R.id.month);
        monthly.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                Gson gson5=new Gson();
                String jsonstring5=gson5.toJson(peakMonth);
                SharedPreferences sharedPreferences5 = getSharedPreferences("MySharedPrefForList5",MODE_PRIVATE);
                SharedPreferences.Editor myEdit5 = sharedPreferences5.edit();
                myEdit5.putString(LIST_KEY5,jsonstring5);
                myEdit5.apply();
                Intent intent = new Intent(getApplicationContext(),monthlyGraph.class);
                startActivity(intent);
                finish();
            }
        });

        yearly=findViewById(R.id.year);
        yearly.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),yearlyGraph.class);
                startActivity(intent);
                finish();
            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(new Date());
        System.out.println(date);
        return date;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //if (clickCount!=0){
//            //transferPeakTodb();
//            transferToDb();
            backgroundThreadRealm.close();
        //}

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void retrieveFromDB() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                user = app.currentUser();
//                SyncConfiguration config = new SyncConfiguration.Builder(
//                        user,
//                        partitionValue
//                ).allowQueriesOnUiThread(true)
//                        .allowWritesOnUiThread(true)
//                        .build();
//                backgroundThreadRealm =

                Log.v("here", "below user");
                //data = backgroundThreadRealm.where(PeakPOJO.class).equalTo("name",user.getProfile().getEmail()).equalTo("date",getDate());
                RealmResults<AnglePOJO> dailyAngleData = backgroundThreadRealm.where(AnglePOJO.class).equalTo("name", user.getProfile().getEmail()).equalTo("date", getDate()).findAll();
                Log.v("here", "below loop");
                for (int i = 0; i < dailyAngleData.size(); i++) {
                    Log.v("here", "inside loop");
                    AnglePOJO values = dailyAngleData.get(i);
                    graphF.add(Math.round(Float.valueOf(values.getForwardAngle())));
                    graphB.add(Math.round(Float.valueOf(values.getBackwardAngle())));
                    graphL.add(Math.round(Float.valueOf(values.getLeftAngle())));
                    graphR.add(Math.round(Float.valueOf(values.getRightAngle())));
                    System.out.println(values.getForwardAngle());


                }
            }
        });


    }



}

