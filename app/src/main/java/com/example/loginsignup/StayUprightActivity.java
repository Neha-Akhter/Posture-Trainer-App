package com.example.loginsignup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class StayUprightActivity extends AppCompatActivity {
    TextView textView5,textView6,textView7;
    Button Posture_btn;
    pl.droidsonroids.gif.GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay_upright);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar bar = getSupportActionBar();
        textView5=(TextView)findViewById(R.id.textView5);
        //textView6=(TextView)findViewById(R.id.textView16);
        textView7=(TextView)findViewById(R.id.textView7);
        Posture_btn=(Button)findViewById(R.id.Posture_btn);
        gifImageView=(GifImageView)findViewById(R.id.imageView2);

        Posture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LoadingActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}