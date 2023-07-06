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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.BasicDBObject;


import org.bson.Document;
import org.bson.types.ObjectId;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.sync.SyncConfiguration;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Realm backgroundThreadRealm;
    String partitionValue = "Users";

    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    MongoCollection<Document> mongoCollection;
    App app;
    String Appid = "application-0-dslwi";
    User user;

    String gen;
    TextView signUpText;
    EditText fullName;
    EditText gender;
    EditText age;
    EditText password;
    EditText confirmPassword;
    CheckBox terms_conditions;
    Button signUpBtn;
    TextView already_user;
    String[] genders = { "Male", "Female", "Other" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        signUpText = findViewById(R.id.signUpText);
        fullName = findViewById(R.id.fullName);
        age = findViewById(R.id.age);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        terms_conditions = findViewById(R.id.terms_conditions);
        signUpBtn = findViewById(R.id.signUpBtn);
        already_user = findViewById(R.id.already_user);
        Spinner spin = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);



        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpBtn.setClickable(false);

                ConnectivityManager cm = (ConnectivityManager) SignUpActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if(password.getText().toString().equals(confirmPassword.getText().toString())){
                    if(Integer.parseInt(age.getText().toString()) < 100 && Integer.parseInt(age.getText().toString()) > 10){
                        if(terms_conditions.isChecked()){
                            if(isConnected){
                                if (password.length() < 6) {
                                    signUpBtn.setClickable(true);
                                    Toast.makeText(SignUpActivity.this, "Password must be atleast 6 characters", Toast.LENGTH_SHORT).show();
                                }else{
                                    Realm.init(SignUpActivity.this);
                                    app = new App(new AppConfiguration.Builder(Appid).build());

                                    Credentials credentials = Credentials.emailPassword(fullName.getText().toString(), password.getText().toString());
                                    app.getEmailPassword().registerUserAsync(fullName.getText().toString(), password.getText().toString(), result -> {
                                        if (result.isSuccess()) {
//                                            Toast.makeText(SignUpActivity.this, "Successfully registered user.", Toast.LENGTH_SHORT).show();

                                            app.loginAsync(credentials, new App.Callback<User>() {
                                                @Override
                                                public void onResult(App.Result<User> result) {
                                                    if (result.isSuccess()) {
                                                        User user = app.currentUser();
                                                        SyncConfiguration config = new SyncConfiguration.Builder(
                                                                user,
                                                                partitionValue
                                                        ).allowQueriesOnUiThread(true)
                                                                .allowWritesOnUiThread(true)
                                                                .build();
                                                        Realm.deleteRealm(Realm.getDefaultConfiguration());
                                                        Realm.setDefaultConfiguration(config);
                                                        backgroundThreadRealm = Realm.getInstance(config);

                                                        UsersPOJO userdata = new UsersPOJO(
                                                                new ObjectId(), partitionValue, fullName.getText().toString(), gen,
                                                                age.getText().toString()
                                                        );
                                                        backgroundThreadRealm.executeTransaction(transactionRealm -> {
                                                            transactionRealm.insert(userdata);
                                                        });
                                                        backgroundThreadRealm.close();
                                                        Toast.makeText(SignUpActivity.this, "User registered", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(SignUpActivity.this, showAngle.class);
                                                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                                        myEdit.putString("email", fullName.getText().toString());
                                                        myEdit.putString("passw", password.getText().toString());
                                                        myEdit.apply();
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                    else{
                                                        Toast.makeText(SignUpActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                            });

                                        } else {

                                            signUpBtn.setClickable(true);
                                            fullName.setError("Username Already In Use");
                                        }

                                    });
                                } }
                            else{
                                signUpBtn.setClickable(true);
                                Toast.makeText(SignUpActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            signUpBtn.setClickable(true);
                            Toast.makeText(SignUpActivity.this, "Please agree to terms and conditions", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        signUpBtn.setClickable(true);
                        Toast.makeText(SignUpActivity.this, "Age not acceptable", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    signUpBtn.setClickable(true);
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public void HeadToLogin(View view) {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        gen = genders[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //
    }
}
