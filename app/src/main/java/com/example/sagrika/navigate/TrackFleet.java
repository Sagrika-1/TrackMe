package com.example.sagrika.navigate;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class TrackFleet extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    AutoCompleteTextView autoCompleteTextView;
    ArrayList<LatLng> markersArray;
    ArrayList<String> vehicleList;
    String passID=null,selected=null;
    Double lat,lng;
    String manager_name,manager_pass,new_name;
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_page);

        fa=this;

        manager_name = getIntent().getStringExtra("username");
        manager_pass =getIntent().getStringExtra("password");
        vehicleList = getIntent().getStringArrayListExtra("vehicleList");

        final DataBaseHelper info = new DataBaseHelper(TrackFleet.this);
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

        new MapJSON().execute(manager_name);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.ACTV);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,vehicleList);
        autoCompleteTextView.setThreshold(0);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                autoCompleteTextView.showDropDown();
                return false;
            }
        });

        // AutoCompleteTextView on item click listener
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                       View view, int position, long id)
            {
                passID = parent.getItemAtPosition(position).toString();

                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.length()==0)
                    passID = null;
                else
                    passID = autoCompleteTextView.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
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

        if (id == R.id.action_home)
        {
            final DataBaseHelper info = new DataBaseHelper(this);
            Cursor cursor = info.getData();
            String name,pass;
            if(cursor.moveToFirst())
            {
                name = cursor.getString(0);
                pass = cursor.getString(1);
                SpinnerJSON spinner = new SpinnerJSON(this);
                spinner.execute(name, pass);
            }return true;
        }

        return super.onOptionsItemSelected(item);
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

        if(id == R.id.nav_home)
        {
            SpinnerJSON spinner = new SpinnerJSON(this);
            spinner.execute(name,pass);

        }
        else if(id == R.id.nav_settings)
        {
            Intent i = new Intent(this,ChangeInfo.class);
            i.putExtra("pass",pass);//added
            i.putExtra("username",name);//added
            startActivity(i);
            finish();
        }
        else if (id == R.id.nav_logout)
        {
            info.delData();
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
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

    //AsyncTask to extract the locations of IDs owned by the logged in manager and pass to FragmentMap to display their locations
    public class MapJSON extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            String get_url = "http://192.168.0.108:80/TrackMe/get_all.php";
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
                markersArray = new ArrayList<LatLng>();
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
            fragmentManager.beginTransaction().replace(R.id.content_frame, obj).commitAllowingStateLoss();
        }
    }

    public void Btrack(View view)
    {
        if(passID==null)
        {
            Toast.makeText(getBaseContext(),"Select a vehicle",Toast.LENGTH_SHORT).show();
        }
        else
            new TrackJSON().execute(passID);
    }

    public void Bcheck(View view)
    {
        if(passID==null)
        {
            Toast.makeText(getBaseContext(),"Select a vehicle",Toast.LENGTH_SHORT).show();
        }
        else
        {
            selected=null;

            //The following code snippet is for displaying an alert box to select the
            //number of hours to display the route information
            CharSequence colors[] = new CharSequence[] {"1","2","3","4","5","6","7","8","9","10"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the tracking hours");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int option)
                {
                    selected = Integer.toString(option);
                    Log.e("option",selected);
                    new CheckJSON().execute(passID,selected);   //AsyncTask CheckJSON is started after an option is selected
                }
            });
            builder.show();
        }
    }

    //AsyncTask to extract the location of ID selected by manager and pass to MapsFragment to display its location
    public class TrackJSON extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            String get_url = "http://192.168.0.108:80/TrackMe/Latlng.php";
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
        protected void onPostExecute(String args)
        {
            try
            {
                JSONObject obj = new JSONObject(args);
                String json = obj.getString("jsonstring");
                Log.e("string",args);

                switch (json)
                {
                    case "no":      //This is executed if ID entered does not exist in database
                        Log.e("string","Invalid ID");
                        Toast.makeText(getApplicationContext(), "Invalid ID", Toast.LENGTH_LONG).show();
                        break;
                    case "yes":     //This is executed if ID entered exists in database
                        JSONArray arr = obj.getJSONArray("response");
                        // Log.e("printed",arr.getString(0));
                        JSONObject object = new JSONObject(arr.getString(0));
                        String latitude = object.getString("Lat");
                        String longitude = object.getString("Lng");
                        Log.e("Lat",latitude);
                        lat = Double.parseDouble(latitude);
                        lng = Double.parseDouble(longitude);
                        Bundle bundle = new Bundle();
                        bundle.putDouble("lat",lat);
                        bundle.putDouble("lng",lng);
                        MapsFragment mapsFragment = new MapsFragment();
                        mapsFragment.setArguments(bundle);

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, mapsFragment).commit();
                        break;
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //AsyncTask to display the route of ID selected by manager and pass to check_route to display its route
    public class CheckJSON extends AsyncTask<String, Void, CheckJSON.pass> {

        public class pass
        {
            String respond=null,select=null;
        }
        @Override
        protected pass doInBackground(String... params) {
            String get_url = "http://192.168.0.108:80/TrackMe/Check.php";
            String ID = params[0];
            String option = params[1];

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
                pass p = new pass();
                p.respond = response;
                p.select = option;
                return p;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(pass args)
        {
            try
            {
                JSONObject obj = new JSONObject(args.respond);
                String json = obj.getString("jsonstring");
                Log.e("string",args.respond);

                switch (json)
                {
                    case "no":      //This is executed if ID entered does not exist in database
                        Log.e("string","Invalid ID");
                        Toast.makeText(getApplicationContext(), "Invalid ID", Toast.LENGTH_LONG).show();
                        break;
                    case "yes":     //This is executed if ID entered exists in database
                        markersArray = new ArrayList<LatLng>();
                        JSONArray arr = obj.getJSONArray("response");
                        int l = arr.length();
                        int start = l - 2 * (Integer.parseInt(args.select)+1);
                        for (int i = start; i < l; i++)
                        {
                            JSONObject object = new JSONObject(arr.getString(i));
                            String latitude = object.getString("Lat");
                            String longitude = object.getString("Lng");
                            Log.e("Latcheck",latitude);
                            Log.e("Lngcheck",longitude);
                            lat = Double.parseDouble(latitude);
                            lng = Double.parseDouble(longitude);
                            markersArray.add(new LatLng(lat, lng));
                        }

                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("route",markersArray);
                        check_route checkRoute = new check_route();
                        checkRoute.setArguments(bundle);

                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, checkRoute).commit();
                        break;
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
}
