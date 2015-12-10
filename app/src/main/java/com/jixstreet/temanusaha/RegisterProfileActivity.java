package com.jixstreet.temanusaha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.jixstreet.temanusaha.util.CommonConstants;
import com.jixstreet.temanusaha.util.Seeder;

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
    private int creditPurpose = 0, creditCeiling = 0, timeRange = 0;
    private int shapeCode;
    private ImageView searchPartnerIV;
    private EditText usageET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();
        initUI();
        setCallBack();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        shapeCode = intent.getIntExtra(CommonConstants.SHAPE_CODE, 1);
    }

    private void initUI() {
        setContentView(R.layout.get_loan_layout);

        searchPartnerIV = (ImageView) findViewById(R.id.search_partner_iv);

        creditPurposeSP = (Spinner) findViewById(R.id.tujuan_kredit_sp);
        creditCeilingSP = (Spinner) findViewById(R.id.plafon_kredit_sp);
        timeRangeSP = (Spinner) findViewById(R.id.jangka_waktu_sp);
        usageET = (EditText) findViewById(R.id.usage_et);

        creditPurposeList = Seeder.getCreditPurposeList(this);
        creditCeilingList = Seeder.getCreditCeilingList(this);
        timeRangeList = Seeder.getTimeRangeList(this);

        ArrayAdapter<String> dataAdapter;

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, creditPurposeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        creditPurposeSP.setAdapter(dataAdapter);
        creditPurposeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                creditPurpose = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, creditCeilingList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        creditCeilingSP.setAdapter(dataAdapter);
        creditCeilingSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                creditCeiling = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeRangeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timeRangeSP.setAdapter(dataAdapter);
        timeRangeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeRange = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void setCallBack() {
        searchPartnerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!usageET.getText().toString().isEmpty()) {
                    Intent intent = new Intent(RegisterProfileActivity.this, ShowMapActivity.class);
                    intent.putExtra(CommonConstants.SHAPE_CODE, shapeCode);
                    intent.putExtra(CommonConstants.LOAN_TYPE, creditPurpose);
                    intent.putExtra(CommonConstants.LOAN_SEGMENT, creditCeiling);
                    intent.putExtra(CommonConstants.LOAN_PERIOD, timeRange);
                    intent.putExtra(CommonConstants.USAGE, usageET.getText().toString());
                    startActivityForResult(intent, CommonConstants.CREATE_LOAN_CODE);
                } else {
                    usageET.setError(getString(R.string.should_not_be_empty_error));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CommonConstants.CREATE_LOAN_CODE && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
