package com.example.sagrika.navigate;

//This page is displayed when user clicks on help before logging in as manager
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.iid.FirebaseInstanceId;

public class Help_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

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
        MenuItem item1 = navigationView.getMenu().getItem(1);
        item1.setVisible(false);

        MenuItem item2 = navigationView.getMenu().getItem(2);
        item2.setVisible(false);

        //The following code opens a url in the app
        wv = (WebView) findViewById(R.id.WVhelp);
        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl("http://www.gaiasmartcities.com");
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
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_home)     //selecting home starts MainActivity
        {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }
        else if (id == R.id.nav_help)       //selecting help starts Help_main
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
}