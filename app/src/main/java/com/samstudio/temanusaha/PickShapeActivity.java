package com.samstudio.temanusaha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by satryaway on 9/25/2015.
 * picking preferre
 */
public class PickShapeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallBack();
    }

    private void initUI() {
        setContentView(R.layout.pick_shape_layout);
    }

    private void setCallBack() {

    }
}
