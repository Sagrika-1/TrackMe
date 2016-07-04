package com.example.sagrika.navigate;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class ChangeInfo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText currentP, newP, confirmPass;
    String currP, newPass, confPass,currpass,curruser;
    int len;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        currpass = getIntent().getStringExtra("pass");
        curruser = getIntent().getStringExtra("username");
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

        //noinspection SimplifiableIfStatement
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


    public void return_home(View view) {
        currentP = (EditText) findViewById(R.id.currentP);
        newP = (EditText) findViewById(R.id.newP);
        confirmPass = (EditText) findViewById(R.id.confirmP);
        currP = currentP.getText().toString();
        newPass = newP.getText().toString();
        confPass = confirmPass.getText().toString();

        new Update_pass().execute(curruser, confPass, currpass);

    }
      public  class Update_pass extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                String curruser, confPass, currpass;
                curruser = params[0];
                confPass = params[1];
                currpass = params[2];

                String update_url = "http://sagrika.netau.net/update_pass.php";

                try {//added
                    URL url = new URL(update_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("newPass", "UTF-8") + "=" + URLEncoder.encode(confPass, "UTF-8") + "&" +
                            URLEncoder.encode("currPass", "UTF-8") + "=" + URLEncoder.encode(currpass, "UTF-8") + "&" +
                            URLEncoder.encode("currUser", "UTF-8") + "=" + URLEncoder.encode(curruser, "UTF-8");
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


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }//added
                return null;
            }

          @Override
          protected void onPostExecute(String response) {

            Toast.makeText(ChangeInfo.this,response,Toast.LENGTH_SHORT).show();
              len = currP.length();
          if (len == 0) {
                    Toast.makeText(getApplicationContext(), "Enter current password", Toast.LENGTH_SHORT).show();
                } else if (currpass.equals(currP)) {
              if (newPass.equals(confPass)) {
                  Toast.makeText(getApplicationContext(), "Passwords changed successfully", Toast.LENGTH_SHORT).show();
                  Intent i = new Intent(ChangeInfo.this, managerLogin.class);
                  startActivity(i);

              } else {
                  Toast.makeText(getApplicationContext(), "Passwords did not match", Toast.LENGTH_SHORT).show();
              }

                } else {
              Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show(); //added

                }
              super.onPostExecute(response);            }


        }

    }

