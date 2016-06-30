package com.example.sagrika.navigate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {
        Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();

                    final DataBaseHelper info = new DataBaseHelper(SplashScreen.this);
                    cursor = info.getData();
                    if(!cursor.moveToFirst()){
                        startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    }
                    else{
                        startActivity(new Intent(SplashScreen.this,Verify.class));
                    }


                  //  Intent i = new Intent(SplashScreen.this, MainActivity.class);
                   // startActivity(i);
                }
            };

        };
        thread.start();

    }


}
