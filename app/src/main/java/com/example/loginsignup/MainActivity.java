package com.example.loginsignup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

import static java.lang.reflect.Modifier.TRANSIENT;

public class MainActivity extends AppCompatActivity {

    String Appid = "application-0-dslwi";
    App app;
    User user;
    boolean doubleBackToExitPressedOnce = false;
    LinearLayout login_layout;
    TextView createAccount;
    EditText login_emailid;
    EditText login_password;
    CheckBox show_hide_password;
    TextView forgot_password;
    Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar bar = getSupportActionBar();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// hide the title bar
        login_layout= findViewById(R.id.login_layout);
        createAccount= findViewById(R.id.createAccount);
//        forgot_password= findViewById(R.id.forgot_password);
        login_emailid= findViewById(R.id.login_emailid);
        login_password= findViewById(R.id.login_password);
        show_hide_password= findViewById(R.id.show_hide_password);
        loginBtn= findViewById(R.id.loginBtn);

//        Realm.init(this);
//        app = new App(new AppConfiguration.Builder(Appid).build());
//        if(user == app.currentUser()){
//            Intent intent=new Intent(MainActivity.this,showAngle.class);
//            startActivity(intent);
//        }
        //Show password check//
        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    login_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else{
                    login_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn.setClickable(false);
                ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if(isConnected){
                Realm.init(MainActivity.this);
                app = new App(new AppConfiguration.Builder(Appid).build());
                Credentials credentials = Credentials.emailPassword(login_emailid.getText().toString(),login_password.getText().toString());
                app.loginAsync(credentials, new App.Callback<User>() {
                    @Override
                    public void onResult(App.Result<User> result) {
                        if (result.isSuccess()){
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

                                    //                            Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();

//                            CurrentUser userObj =new CurrentUser(12,"currentUser");
//                            sessionManagement sessionManagement=new sessionManagement(MainActivity.this);
//                            sessionManagement.saveSession(userObj);

                            Intent intent=new Intent(MainActivity.this,showAngle.class);
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("email", login_emailid.getText().toString());
                            myEdit.putString("passw", login_password.getText().toString());
                            myEdit.apply();
                            startActivity(intent);
                            finish();
                        }
                        else {
                            // Log.v("User", "Failed to Login");
                            loginBtn.setClickable(true);
                            Toast.makeText(MainActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                    loginBtn.setClickable(true);
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
        }
        });

    }
    public void forgot_password(View view) {
        Intent intent=new Intent(MainActivity.this,Forgot_PassActivity.class);
        startActivity(intent);
    }

    public void HeadToSignUp(View view) {
        Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
        startActivity(intent);

    }
    @Override
    public void onBackPressed() {

      return;

    }
}