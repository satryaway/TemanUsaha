package com.samstudio.temanusaha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.samstudio.temanusaha.entities.CreditCeiling;
import com.samstudio.temanusaha.entities.CreditPurpose;
import com.samstudio.temanusaha.entities.TimeRange;
import com.samstudio.temanusaha.util.Seeder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 9/30/2015.
 * activity to register applicant
 */
public class RegisterProfileActivity extends AppCompatActivity {
    private Spinner creditPurposeSP, creditCeilingSP, timeRangeSP;
    private List<String> creditPurposeList = new ArrayList<>();
    private List<String> creditCeilingList = new ArrayList<>();
    private List<String> timeRangeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
    }

    private void initUI() {
        setContentView(R.layout.get_loan_layout);

        creditPurposeSP = (Spinner) findViewById(R.id.tujuan_kredit_sp);
        creditCeilingSP = (Spinner) findViewById(R.id.plafon_kredit_sp);
        timeRangeSP = (Spinner) findViewById(R.id.jangka_waktu_sp);

        creditPurposeList = Seeder.getCreditPurposeList(this);
        creditCeilingList = Seeder.getCreditCeilingList(this);
        timeRangeList = Seeder.getTimeRangeList(this);

        ArrayAdapter<String> dataAdapter;

        dataAdapter = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, creditPurposeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        creditPurposeSP.setAdapter(dataAdapter);
        creditPurposeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dataAdapter = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, creditCeilingList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        creditCeilingSP.setAdapter(dataAdapter);
        creditCeilingSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dataAdapter = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, timeRangeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timeRangeSP.setAdapter(dataAdapter);
        timeRangeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setCallBack() {

    }
}
