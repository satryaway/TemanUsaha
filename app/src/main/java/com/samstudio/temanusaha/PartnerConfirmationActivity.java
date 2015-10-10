package com.samstudio.temanusaha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by satryaway on 10/10/2015.
 * activity to confirm the loan on customer side
 */
public class PartnerConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
    }

    private void initUI() {
        setContentView(R.layout.partner_confirmation_layout);
    }

    private void setCallBack() {

    }
}
