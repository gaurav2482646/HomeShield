package com.bricon.homeshield;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        runCountDownTimer();
    }
    public void runCountDownTimer()
    {
        new CountDownTimer(5000,1000)
        {
            @Override
            public void onTick(long millisecondAfter) {
                Log.e(TAG, "onTick: CountDowntimer calling" );
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                Log.e(TAG, "onFinish:CountDownTimer finished");
                finish();
            }
        }.start();
    }
}