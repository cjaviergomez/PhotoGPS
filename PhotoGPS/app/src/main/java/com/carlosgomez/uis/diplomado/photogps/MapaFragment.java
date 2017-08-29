package com.carlosgomez.uis.diplomado.photogps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



public class MapaFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap map;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Location miUbicacion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapa, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            FragmentManager manager = getChildFragmentManager();
            mapFragment = SupportMapFragment.newInstance();
            manager.beginTransaction().replace(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng marker = new LatLng(-33.867, 151.206);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 16));

        googleMap.addMarker(new MarkerOptions().title("Hello Google Maps!").position(marker));

        if (checkLocationPermission()) {
            LocationManager lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
            miUbicacion = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (miUbicacion != null) {
                double longactual = miUbicacion.getLongitude();
                double latactual = miUbicacion.getLatitude();
                Toast.makeText(getContext(), "Ubicación: "+latactual+", "+longactual, Toast.LENGTH_SHORT).show();
                LatLng yo = new LatLng(latactual, longactual);
                map.addMarker(new MarkerOptions().position(yo).title("YO"));
            }
            else {
                Toast.makeText(getContext(), "Ubicación no generada", Toast.LENGTH_SHORT).show();
            }
            map.setMyLocationEnabled(true);
        }
    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
}

