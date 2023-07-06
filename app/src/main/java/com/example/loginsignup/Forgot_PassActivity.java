package com.example.loginsignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Forgot_PassActivity extends AppCompatActivity {
    TextView registration;
    EditText registered_emailid;
    Button backToLoginBtn;
    Button forgot_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__pass);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        registration= findViewById(R.id.registration);
        registered_emailid= findViewById(R.id.registered_emailid);
        backToLoginBtn= findViewById(R.id.backToLoginBtn);
        forgot_button= findViewById(R.id.forgot_button);

    }

    public void BackToLogin(View view) {
        Intent intent=new Intent(Forgot_PassActivity.this,MainActivity.class);
        startActivity(intent);
    }
}