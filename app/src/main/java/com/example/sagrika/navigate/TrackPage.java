package com.example.sagrika.navigate;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

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

public class TrackPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    ArrayList<LatLng> markersArray = new ArrayList<LatLng>();
    ArrayList<String> vehicleList;
    String passID;
    Double lat,lng;
    String manager_name,manager_pass,new_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_page);

        manager_name = getIntent().getStringExtra("username");
        manager_pass =getIntent().getStringExtra("password");
        vehicleList = getIntent().getStringArrayListExtra("vehicleList");

        final DataBaseHelper info = new DataBaseHelper(TrackPage.this);
        info.putData(manager_name,manager_pass);
        Cursor cursor = info.getData();
        if(cursor.moveToFirst()) {
            do {
                new_name = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem item = navigationView.getMenu().getItem(0);
        item.setVisible(false);

        new MapJSON().execute("nisha");

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        // Locate the spinner in activity_main.xml
        ArrayAdapter<String> locationAdapter =
                new ArrayAdapter<String>(TrackPage.this, android.R.layout.simple_spinner_item, vehicleList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationAdapter.notifyDataSetChanged();
        mySpinner.setAdapter(locationAdapter);

        // Spinner on item click listener
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                if (position != 0)
                    passID = parent.getItemAtPosition(position).toString();
                else
                    passID = "nothing";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        final DataBaseHelper info = new DataBaseHelper(this);
        Cursor cursor = info.getData();
        String name = null,pass = null;
        if(cursor.moveToFirst())
        {
            name = cursor.getString(0);
            pass = cursor.getString(1);
        }

        if(id == R.id.nav_settings)
        {
            Intent i = new Intent(this,ChangeInfo.class);
            i.putExtra("pass",pass);//added
            i.putExtra("username",name);//added
            startActivity(i);
        }
        else if (id == R.id.nav_logout)
        {
            info.delData();
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_help)
        {
            Intent i = new Intent(this,Help_page.class);
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

    public class MapJSON extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            String get_url = "http://sagrika.netau.net/get_all.php";
            String username = params[0];

            try {
                URL url = new URL(get_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
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
        protected void onPostExecute(String args)
        {
            try
            {
                JSONObject obj = new JSONObject(args);
                JSONArray arr = obj.getJSONArray("response");
                for (int i = 0; i < arr.length(); i++)
                {
                    JSONObject object = new JSONObject(arr.getString(i));
                    String latitude = object.getString("Lat");
                    String longitude = object.getString("Lng");
                    lat = Double.parseDouble(latitude);
                    lng = Double.parseDouble(longitude);
                    markersArray.add(new LatLng(lat, lng));
                }
                Log.e("marker",markersArray.get(0).toString());
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("markersList",markersArray);
            FragmentMap obj = new FragmentMap();
            obj.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, obj).commit();
        }
    }

    public void Btrack(View view)
    {
        if(passID.equals("nothing"))
        {
            Toast.makeText(getBaseContext(),"Select a vehicle",Toast.LENGTH_SHORT).show();
        }
        else
            new TrackJSON().execute(passID);
    }

    public void Bcheck(View view)
    {
        Toast.makeText(getBaseContext(),"Check Route pressed",Toast.LENGTH_SHORT).show();
    }

    public class TrackJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String get_url = "http://sagrika.netau.net/Latlng.php";
            String ID = params[0];

            try {
                URL url = new URL(get_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String data = URLEncoder.encode("ID", "UTF-8") + "=" + URLEncoder.encode(ID, "UTF-8");
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
        protected void onPostExecute(String args) {
            try {

                JSONObject obj = new JSONObject(args);
                JSONArray arr = obj.getJSONArray("jsonstring");
                // Log.e("printed",arr.getString(0));
                JSONObject object = new JSONObject(arr.getString(0));
                String latitude = object.getString("Lat");
                String longitude = object.getString("Lng");
                Log.e("Lat",latitude);
                lat = Double.parseDouble(latitude);
                lng = Double.parseDouble(longitude);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Bundle bundle = new Bundle();
            bundle.putDouble("lat",lat);
            bundle.putDouble("lng",lng);
            MapsFragment obj = new MapsFragment();
            obj.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, obj).commit();
        }
    }
}
