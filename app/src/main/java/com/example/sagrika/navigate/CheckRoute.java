package com.example.sagrika.navigate;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class CheckRoute extends Fragment implements OnMapReadyCallback
{
    ArrayList<LatLng> markersArray;     //contains a list of markers
    LatLng marker;
    Double lat, lng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Gets the values passed from previous fragment
        Bundle bundle = getArguments();
        lat = bundle.getDouble("lat");
        lng = bundle.getDouble("lng");

        //Starts a new map fragment
        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {

        LatLng dest = new LatLng(lat,lng);
        if(lat==30.2367)
        {
            googleMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(29.728,76.397), new LatLng(30.152,76.694), new LatLng(30.204,77.018),
                            new LatLng(dest.latitude, dest.longitude))
                    .width(8).color(Color.BLUE).geodesic(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29.728,76.397), 8));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 8));
            googleMap.addMarker(new MarkerOptions().title("Source").position(new LatLng(29.728,76.397)));
            googleMap.addMarker(new MarkerOptions().title("Destination").position(dest));
        }
        else if(lat==30.2276)
        {
            googleMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(29.997,75.392), new LatLng(30.391,75.562), new LatLng(30.239,75.831),
                            new LatLng(dest.latitude, dest.longitude))
                    .width(8).color(Color.BLUE).geodesic(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29.997,75.392), 8));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 8));
            googleMap.addMarker(new MarkerOptions().title("Source").position(new LatLng(29.997,75.392)));
            googleMap.addMarker(new MarkerOptions().title("Destination").position(dest));
        }
        else if(lat==39.3211)
        {
            googleMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(38.665,86.553),new LatLng(39.003,86.410), new LatLng(39.225,86.783),
                            new LatLng(dest.latitude, dest.longitude))
                    .width(8).color(Color.BLUE).geodesic(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.665,86.553), 8));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 8));
            googleMap.addMarker(new MarkerOptions().title("Source").position(new LatLng(38.665,86.553)));
            googleMap.addMarker(new MarkerOptions().title("Destination").position(dest));
        }
        }
    }