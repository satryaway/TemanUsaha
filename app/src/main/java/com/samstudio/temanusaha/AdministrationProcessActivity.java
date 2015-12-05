package com.samstudio.temanusaha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.samstudio.temanusaha.util.CommonConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by satryaway on 10/17/2015.
 * activity for administration process
 */
public class AdministrationProcessActivity extends AppCompatActivity {
    private String date;
    private TextView dateTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent();
        initUI();
        setCallBack();
        setData();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        date = intent.getStringExtra(CommonConstants.DATE);
    }

    private void initUI() {
        setContentView(R.layout.administration_process_layout);
        dateTV = (TextView) findViewById(R.id.date_tv);
    }

    private void setCallBack() {

    }

    private void setData() {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");

        Date newDate = null;
        String str = null;

        try {
            newDate = inputFormat.parse(date);
            str = outputFormat.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();
        try {
            c.setTime(outputFormat.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);
        date = outputFormat.format(c.getTime());

        dateTV.setText(date);
    }
}
