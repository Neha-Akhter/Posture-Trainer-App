package com.example.loginsignup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


public class ChartsActivity extends AppCompatActivity {
    Button fx_btn,ry_btn,ly_btn,bx_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        ActionBar bar = getSupportActionBar();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        fx_btn=(findViewById(R.id.fx_btn));
        ry_btn=(findViewById(R.id.ry_btn));
        ly_btn=(findViewById(R.id.ly_btn));
        bx_btn=(findViewById(R.id.bx_btn));

        fx_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), BackwardActivity.class);
                startActivity(intent);
            }
        });


        bx_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(getApplicationContext(), ForwardActivity.class);
                startActivity(intent1);
            }
        });


        ry_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(getApplicationContext(), RightActivity.class);
                startActivity(intent2);
            }
        });


        ly_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(getApplicationContext(), LeftActivity.class);
                startActivity(intent3);
            }
        });






        }







}