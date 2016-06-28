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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Verify extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EditText editText;
    String ID_l;
    String newText;
    String manager_name,manager_pass;
    Spinner spinner;
    ArrayAdapter<CharSequence> arrayAdapter;
    TextView id_vehicle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


      manager_name = getIntent().getStringExtra("username");
        manager_pass =getIntent().getStringExtra("password");


        spinner = (Spinner)findViewById(R.id.spinner);
        arrayAdapter = ArrayAdapter.createFromResource(this,R.array.list_ids,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // spinner.setBackgroundResource(R.drawable.spinner_img);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+"selected",Toast.LENGTH_SHORT).show();
                newText =  parent.getItemAtPosition(position).toString();
                ID_l = newText;


                // id_vehicle =(TextView)findViewById(R.id.Id_vehicle);
                //id_vehicle.setVisibility(view.VISIBLE);
                //id_vehicle.setText(ID_l.toString());
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

        if(id == R.id.nav_home)
        {
            Intent i = new Intent(this,Verify.class);
            startActivity(i);

        }
        else if(id == R.id.nav_settings)
        {
            Intent i = new Intent(this,ChangeInfo.class);
            i.putExtra("pass",manager_pass);//added
            i.putExtra("username",manager_name);//added
            startActivity(i);

        }
        else if (id == R.id.nav_logout)
        {
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


    public void options_go(View view) {
        String method = "manager_id";
        ID_l = spinner.getSelectedItem().toString();
        //ManagerDriver managerDriver = new ManagerDriver(this);
       //managerDriver.execute(method,manager_name,ID_l);
        Intent i = new Intent(this,Location_new.class);
        startActivity(i);
    }
}
