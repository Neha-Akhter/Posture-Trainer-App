package com.example.loginsignup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;

public class SignupActivity1 extends AppCompatActivity {

    String Appid = "application-0-dslwi";
    User user;

    EditText emailid,passid;
    String email, password;
    Button signupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        emailid = findViewById(R.id.userEmailid);
        //email = emailid.getText().toString();

        passid = findViewById(R.id.password);
        //password = passid.getText().toString();

        signupBtn = findViewById(R.id.signUpBtn);
        //Realm.init(this);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                App app = new App(new AppConfiguration.Builder(Appid).build());

                app.getEmailPassword().registerUserAsync(emailid.getText().toString(),passid.getText().toString(),it -> {
                    if (it.isSuccess()) {
                        Toast.makeText(SignupActivity1.this, "Registered", Toast.LENGTH_SHORT).show();
                        app.getEmailPassword().sendResetPasswordEmailAsync(emailid.getText().toString(), result ->{
                            Toast.makeText(SignupActivity1.this, "Email Sent", Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPref = getSharedPreferences("myKey", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("email", emailid.getText().toString());
                            editor.putString("password", passid.getText().toString());
                            editor.apply();

                        });
                    }
                    else{
                        Toast.makeText(SignupActivity1.this, "Failed to register", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }
}