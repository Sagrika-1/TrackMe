package com.example.sagrika.navigate;

//This displays a Splash Screen when the app starts
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Creates a thread different from main thread to display SplashScreen
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    //To check if manager was logged in
                    //If manager was logged in then MainActivity is started else TrackFleet is started
                    final DataBaseHelper info = new DataBaseHelper(SplashScreen.this);
                    cursor = info.getData();
                    if (!cursor.moveToFirst())
                    {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    }
                    else
                    {
                        String name = cursor.getString(0);
                        String pass = cursor.getString(1);

                        //Async Task to get the list of fleet IDs corresponding to logged in manager
                        SpinnerJSON spinner = new SpinnerJSON(SplashScreen.this);
                        spinner.execute(name,pass);
                    }
                    finish();


                }
            }

        };
        thread.start();

    }
}