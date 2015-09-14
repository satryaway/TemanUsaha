package com.samstudio.temanusaha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by satryaway on 9/14/2015.
 * activity to change user's password
 */
public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
    }

    private void initUI() {
        setContentView(R.layout.change_password_layout);
    }

    private void setCallBack() {
    }
}
