package com.jixstreet.temanusaha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jixstreet.temanusaha.util.CommonConstants;

/**
 * Created by satryaway on 10/25/2015.
 * approved app activity
 */
public class AppConfirmedActivity extends AppCompatActivity {
    private String date;
    private TextView dateTV;
    private Button okBtn;
    private boolean isConfirmed;
    private TextView messageTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();
        initUI();
        setCallBack();
        putData();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        date = intent.getStringExtra(CommonConstants.DATE);
        isConfirmed = intent.getBooleanExtra(CommonConstants.IS_CONFIRMED, false);
    }

    private void initUI() {
        setContentView(R.layout.app_approved_layout);
        dateTV = (TextView) findViewById(R.id.date_tv);
        okBtn = (Button) findViewById(R.id.ok_btn);
        messageTV = (TextView) findViewById(R.id.message_tv);
    }

    private void setCallBack() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void putData() {
        dateTV.setText(date);
        if(isConfirmed)
            messageTV.setText(R.string.you_have_confirmed);
    }
}
