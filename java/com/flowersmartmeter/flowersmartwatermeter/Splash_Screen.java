package com.flowersmartmeter.flowersmartwatermeter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash_Screen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 5000;
    private SessionHandler session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        session = new SessionHandler(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //if(!session.getUserDetails().phonenumber.equals("")) {
                    Intent mainIntent = new Intent(Splash_Screen.this, Login_Manger.class);
                    startActivity(mainIntent);
                    finish();
               // }else{
                    ///Intent mainIntent = new Intent(Splash_Screen.this, Login_Manger.class);
                    ///startActivity(mainIntent);
                  //  finish();
                //}
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
