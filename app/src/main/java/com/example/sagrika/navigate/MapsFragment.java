package com.example.sagrika.navigate;

//This fragment opens inside TrackFleet and displays the current position of the ID selected
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

//This fragment displays the current location of selected vehicle
public class MapsFragment extends Fragment implements OnMapReadyCallback
{
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
        marker = new LatLng(lat, lng);

        //Sets the marker on map and also the zoom value of camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 8));

        //Sets the title for marker
        googleMap.addMarker(new MarkerOptions().title("Hello Google Maps!").position(marker));
    }
}