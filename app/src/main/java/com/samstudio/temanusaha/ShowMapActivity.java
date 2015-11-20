package com.samstudio.temanusaha;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.samstudio.temanusaha.entities.Partner;
import com.samstudio.temanusaha.util.APIAgent;
import com.samstudio.temanusaha.util.CommonConstants;
import com.samstudio.temanusaha.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by satryaway on 10/6/2015.
 * activity to show the list of available partner nearby by using google Map
 */
public class ShowMapActivity extends AppCompatActivity {
    private List<Partner> partnerList = new ArrayList<>();
    private GoogleMap googleMap;
    private int loanType, loanSegment, timeRange, personalityShape;
    private int chosenId = 1;
    private ImageView nextIV;
    private String usage;
    private HashMap<Marker, Integer> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();
        initUI();
        setCallBack();
        createMapView();
        putData();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        personalityShape = intent.getIntExtra(CommonConstants.SHAPE_CODE, 0);
        loanType = intent.getIntExtra(CommonConstants.LOAN_TYPE, 0);
        loanSegment = intent.getIntExtra(CommonConstants.LOAN_SEGMENT, 0);
        timeRange = intent.getIntExtra(CommonConstants.LOAN_PERIOD, 0);
        usage = intent.getStringExtra(CommonConstants.USAGE);
    }

    private void initUI() {
        setContentView(R.layout.show_map_layout);

        nextIV = (ImageView) findViewById(R.id.next_step_iv);
    }

    private void setCallBack() {
        nextIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowMapActivity.this, PartnerConfirmationActivity.class);
                intent.putExtra(CommonConstants.ID, chosenId);
                intent.putExtra(CommonConstants.LOAN_TYPE, loanType);
                intent.putExtra(CommonConstants.LOAN_SEGMENT, loanSegment);
                intent.putExtra(CommonConstants.LOAN_PERIOD, timeRange);
                intent.putExtra(CommonConstants.SHAPE_CODE, personalityShape);
                intent.putExtra(CommonConstants.USAGE, usage);
                startActivity(intent);
            }
        });
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

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void addCurrentLocation() {
        double lat = Double.valueOf(TemanUsahaApplication.getInstance().getSharedPreferences().getString(CommonConstants.LATITUDE, "0.0"));
        double lng = Double.valueOf(TemanUsahaApplication.getInstance().getSharedPreferences().getString(CommonConstants.LONGITUDE, "0.0"));
        LatLng latLng = new LatLng(lat, lng);
        if (null != googleMap) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.your_location))
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.tu_white)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            hashMap.put(marker, -1);
        }
    }

    private void addMarkers() {
        addCurrentLocation();
        addMarker();
    }

    private void addMarker() {

        int i = 0;
        for (Partner partner : partnerList) {
            LatLng latLng = new LatLng(partner.getLat(), partner.getLng());
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(partner.getCompany())
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon)));
            hashMap.put(marker, i);
            i++;
        }

        final HashMap<Marker, Integer> markerIntegerHashMap = hashMap;

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (markerIntegerHashMap.get(marker) != -1)
                    chosenId = partnerList.get(markerIntegerHashMap.get(marker)).getId();
                return false;
            }
        });
    }

    private void putData() {
        String url = CommonConstants.SERVICE_GET_PARTNER_LIST + loanType;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();

        APIAgent.get(url, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressDialog.setProgress(0);
                progressDialog.show();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getInt(CommonConstants.STATUS) == CommonConstants.STATUS_OK) {
                        partnerList = Utility.parsePartners(response);
                        addMarkers();
                    } else {
                        Toast.makeText(ShowMapActivity.this, R.string.no_correspondent_loan_type, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ShowMapActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(ShowMapActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });
    }
}
