package com.example.sagrika.navigate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class managerLogin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String login_name,login_pass;
    EditText userName = null, userPassword= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem item2 = navigationView.getMenu().getItem(1);
        item2.setVisible(false);

        MenuItem item3 = navigationView.getMenu().getItem(2);
        item3.setVisible(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
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
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.nav_help)
        {
            Intent i = new Intent(this,Help_main.class);
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


    public void verification(View view)
    {
     userName = (EditText) findViewById(R.id.userName);
        userPassword = (EditText) findViewById(R.id.userPassword);
        login_name = userName.getText().toString().trim();
        login_pass = userPassword.getText().toString().trim();
        int a=login_name.length(),b=login_pass.length();
        String method = "login";
        if (a==0 && b==0) {
           userName.setError("Username Required");
            userPassword.setError("Password Required");
        }
        else if(a==0){
            userName.setError("Username Required");
        }
        else if(b==0){
            userPassword.setError("Password Required");
        }
        else
        {
            Log.e("name",login_name);
            Log.e("pass",login_pass);
            Retrieve retrieve = new Retrieve(this);
            retrieve.execute(method, login_name, login_pass);

        }



        }

    public void forgot(View view){
        Intent i = new Intent(this,Forgot.class);
        startActivity(i);
    }


    @Override
    protected void onPause(){
        super.onPause();

        // Clear all value here
        if(userName!=null)
            userName.setText("");
        if(userPassword!=null)
            userPassword.setText("");
    }

    }




