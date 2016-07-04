package com.example.sagrika.navigate;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class FragmentMap extends Fragment implements OnMapReadyCallback
{
    ArrayList<LatLng> markersArray;
    LatLng marker;
    Double lat, lng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //setRetainInstance(true);
        return inflater.inflate(R.layout.activity_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        markersArray= new ArrayList<LatLng>();
        Bundle bundle = getArguments();
        markersArray = bundle.getParcelableArrayList("markersList");

        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        for (int i = 0; i < markersArray.size(); i++)
        {
            marker = markersArray.get(i);
            //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 13));
            googleMap.addMarker(new MarkerOptions().title("Hello Google Maps!").position(marker));
        }
    }
}