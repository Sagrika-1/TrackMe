package com.example.sagrika.navigate;

//This is a fragment which opens inside TrackFleet activity and displays the route for the shipment to be tracked
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class check_route extends Fragment implements OnMapReadyCallback
{
    ArrayList<LatLng> route;     //contains a list of markers of the locations

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        route= new ArrayList<LatLng>();

        //Initialises 'markersArray' from ArrayList passed from the preceeding activity
        Bundle bundle = getArguments();
        route = bundle.getParcelableArrayList("route");

        //Starts a new map fragment
        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.marker);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap bitmap = Bitmap.createScaledBitmap(b, width, height, false);
        int last = route.size()-1;
        LatLng dest = route.get(last) ;
        PolylineOptions polylineOptions = new PolylineOptions();
        Log.e("0",route.get(0).toString());
        Log.e("last",route.get(last).toString());
        googleMap.addPolyline(polylineOptions
                .add(route.get(0))
                .width(8).color(Color.BLUE).geodesic(true));
        for (int i = 1; i < route.size(); i++)
        {
            googleMap.addPolyline(polylineOptions
                .add(route.get(i)));
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.get(0), 8));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 8));
        MarkerOptions marker = new MarkerOptions();
        marker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        googleMap.addMarker(new MarkerOptions().title("Source").position(route.get(0)));
        googleMap.addMarker(marker.title("Destination").position(dest));
    }
}