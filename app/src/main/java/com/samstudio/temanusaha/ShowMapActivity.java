package com.samstudio.temanusaha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.samstudio.temanusaha.entities.Partner;
import com.samstudio.temanusaha.util.Seeder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 10/6/2015.
 * activity to show the list of available partner nearby by using google Map
 */
public class ShowMapActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener {
    private List<Partner> partnerList = new ArrayList<>();
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        putData();
        initUI();
        setCallBack();
        createMapView();
        addMarkers();
    }

    private void initUI() {
        setContentView(R.layout.show_map_layout);
    }

    private void setCallBack() {

    }

    private void createMapView() {
        try {
            if (null == googleMap) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
                if (null == googleMap)
                    Toast.makeText(getApplicationContext(), "Error creating map", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException exception) {
            Log.e("mapApp", exception.toString());
        }

        googleMap.setOnMarkerClickListener(this);
    }

    private void addMarkers() {
        boolean isFirst = true;
        for (Partner partner : partnerList) {
            LatLng latLng = new LatLng(partner.getLat(), partner.getLng());
            addMarker(latLng, partner.getCompany());
            if (isFirst) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                isFirst = false;
            }
        }
    }

    private void addMarker(LatLng pos, String name) {
        if (null != googleMap) {
            googleMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(name)
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)));
        }
    }

    private void putData() {
        partnerList = Seeder.getPartners();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
