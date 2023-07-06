package com.example.loginsignup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class LaunchingActivity extends AppCompatActivity {
    String Appid = "application-0-dslwi";
    App app;

    private static int SPLASH_TIME_OUT=2500;

    ProgressBar progressBar;
    TextView tv1;
    ImageView imageView;
    View line0, line1, line2, line3, line4, line5, line6;
    int counter = 1;
    Animation topAnimation;
    boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);
        ActionBar bar = getSupportActionBar();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(Appid).build());

        progressBar= findViewById(R.id.progressBar);
        tv1 = findViewById(R.id.tv1);
        imageView = findViewById(R.id.img3);


        line0= findViewById(R.id.line0);
        line1= findViewById(R.id.line1);
        line2= findViewById(R.id.line2);
        line3= findViewById(R.id.line3);
        line4= findViewById(R.id.line4);
        line5= findViewById(R.id.line5);
        line6= findViewById(R.id.line6);

        topAnimation= AnimationUtils.loadAnimation(this, R.anim.top_anim);


        line1.setAnimation(topAnimation);
        line2.setAnimation(topAnimation);
        line3.setAnimation(topAnimation);
        line4.setAnimation(topAnimation);
        line5.setAnimation(topAnimation);
        line6.setAnimation(topAnimation);

        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.white_greyish),
                android.graphics.PorterDuff.Mode.SRC_IN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    String emailId = sh.getString("email", null);
                    String passId = sh.getString("passw", null);
                    Credentials credentials = Credentials.emailPassword(emailId, passId);

                    ConnectivityManager cm = (ConnectivityManager) LaunchingActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                    if(isConnected) {
                        app.loginAsync(credentials, new App.Callback<User>() {
                            @Override
                            public void onResult(App.Result<User> result) {
                                if (result.isSuccess()) {
                                    success = result.isSuccess();
                                  String  partitionValue = "My project";
                                   User user = app.currentUser();
                                    SyncConfiguration config = new SyncConfiguration.Builder(
                                            user,
                                            partitionValue
                                    ).allowQueriesOnUiThread(true)
                                            .allowWritesOnUiThread(true)
                                            .build();
                                    Realm.removeDefaultConfiguration();
                                    Realm.setDefaultConfiguration(config);
                                    //Realm   backgroundThreadRealm = Realm.getInstance(config);
                                    Intent intent = new Intent(LaunchingActivity.this,showAngle.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(LaunchingActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(LaunchingActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LaunchingActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }catch(Exception e){
                    Intent intent = new Intent(LaunchingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },SPLASH_TIME_OUT);

    }
    @Override
    public void onBackPressed() {
        return;
    }
}