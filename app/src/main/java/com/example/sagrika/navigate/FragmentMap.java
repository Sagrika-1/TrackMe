package com.example.sagrika.navigate;

import android.app.Fragment;
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

import java.util.ArrayList;

public class FragmentMap extends Fragment implements OnMapReadyCallback
{
    ArrayList<LatLng> markersArray;     //contains a list of markers
    LatLng marker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        markersArray= new ArrayList<LatLng>();

        //Initialises 'markersArray' from ArrayList passed from the precceeding activity
        Bundle bundle = getArguments();
        markersArray = bundle.getParcelableArrayList("markersList");

        //Starts a new map fragment
        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //adds markers on map corresponding to the fleet of the logged in manager
        for (int i = 0; i < markersArray.size(); i++)
        {
            marker = markersArray.get(i);
            //sets the marker and zoom value
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 1));
            //sets the title of marker
            googleMap.addMarker(new MarkerOptions().title("Hello Google Maps!").position(marker));
        }
    }
}