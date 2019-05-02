package com.rajendra.bhajanaarti;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.rajendra.bhajanaarti.activities.HomeActivity;


public class SplashScreenActivity extends Activity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler = new Handler();
        handler.postDelayed(r, 2000);

    }

    Runnable r = new Runnable() {

        public void run() {
            // TODO Auto-generated method stub
            startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
            finish();
        }
    };

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        try {
            finish();
            if (handler != null) {
                handler.removeMessages(0);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
