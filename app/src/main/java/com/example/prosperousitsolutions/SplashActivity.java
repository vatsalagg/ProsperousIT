package com.example.prosperousitsolutions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.example.prosperousitsolutions.user_session.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();
      //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                    Intent intent2 =new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent2);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    return;
                }
                Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        },SPLASH_TIME_OUT);


    }
}