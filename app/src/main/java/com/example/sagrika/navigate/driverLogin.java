package com.example.sagrika.navigate;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

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

public class driverLogin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    EditText editText1;
    String stg,latitude,longitude;
    int len = 0;
    Double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        //The following code snippet is for initialising and displaying navigation menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //The following sets the two items as invisible in navigation menu
        //These items do not appear in navigation menu of this page
        MenuItem item2 = navigationView.getMenu().getItem(1);
        item2.setVisible(false);

        MenuItem item3 = navigationView.getMenu().getItem(2);
        item3.setVisible(false);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_home) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)        //selecting home starts MainActivity
        {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.nav_help)       //selecting help starts Help_main
        {
            Intent i = new Intent(this, Help_main.class);
            startActivity(i);
        }
        else if (id == R.id.nav_about)
        {

        }
        else if (id == R.id.nav_policy)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //This method is invoked when user presses 'GET LOCATION' button on Driver Login Page
    public void optionsDriver(View view)
    {
        editText1 = (EditText) findViewById(R.id.vehicled);
        stg = editText1.getText().toString();
        len = stg.length();
        if (len == 0)
        {
            editText1.setError("Enter Vehicle ID");
        }
        else
        {
            new driver().execute(stg);
        }

    }


    public class driver extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            String driver_url = "http://192.168.0.109:80/TrackMe/driverLogin.php";
            String driverId = params[0];


            try
            {
                URL url = new URL(driver_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("driverId", "UTF-8") + "=" + URLEncoder.encode(driverId, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            try
            {
                JSONObject obj = new JSONObject(s);
                String json = obj.getString("jsonstring");
                Log.e("string",s);

                switch (json)
                {
                    case "no":      //This is executed if ID entered does not exist in database
                        Log.e("string","Invalid ID");
                        Toast.makeText(getApplicationContext(), "Invalid ID", Toast.LENGTH_LONG).show();
                        break;
                    case "yes":     //This is executed if ID entered exists in database
                        JSONArray arr = obj.getJSONArray("driver");
                        JSONObject object = new JSONObject(arr.getString(0));
                        latitude = object.getString("Lat");
                        longitude = object.getString("Lng");
                        lat = Double.parseDouble(latitude);
                        lng = Double.parseDouble(longitude);

                        Intent i = new Intent(driverLogin.this,Driver_map.class);
                        //Passes the latitude and longitude value of vehicle ID entered to the Driver_map Activity
                        i.putExtra("lat",lat);
                        i.putExtra("lng",lng);
                        startActivity(i);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // Clears the editText value here if it is not null
        if(editText1!=null)
            editText1.setText("");
    }
}