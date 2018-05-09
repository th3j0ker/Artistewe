package com.typhon.studios.artistewe_prototype_3.AdditionalWedgets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.typhon.studios.artistewe_prototype_3.Home.MainActivity;
import com.typhon.studios.artistewe_prototype_3.R;

public class SplashScreen extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_splash);
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);
    }
}
