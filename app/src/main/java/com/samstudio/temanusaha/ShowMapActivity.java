package com.samstudio.temanusaha;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by satryaway on 10/6/2015.
 * activity to show the list of available partner nearby by using google Map
 */
public class ShowMapActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
    }

    private void initUI() {
        setContentView(R.layout.show_map_layout);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setCallBack() {

    }

    @Override
    public void onMapReady(GoogleMap map) {
            LatLng sydney = new LatLng(-34, 151);
            map.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title("Marker in Sydney")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)));
            map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
