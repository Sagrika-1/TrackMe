package com.example.sagrika.navigate;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem item1 = navigationView.getMenu().getItem(0);
        item1.setVisible(false);

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
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_help)
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


    public void go(View view){
        switch (view.getId()){
            case R.id.manager:
                Intent intentManager = new Intent(this,managerLogin.class);
                startActivity(intentManager);
                // Toast.makeText(getApplicationContext(), "You are Manager", Toast.LENGTH_SHORT).show();
                break;
            case R.id.driver:
                Intent intentDriver = new Intent(this,driverLogin.class);
                startActivity(intentDriver);
                //Toast.makeText(getApplicationContext(), "You are Driver", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
