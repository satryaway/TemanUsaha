package com.jixstreet.temanusaha;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.jixstreet.temanusaha.util.APIAgent;
import com.jixstreet.temanusaha.util.CommonConstants;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by satryaway on 10/25/2015.
 * waiting for approval page
 */
public class WaitingForConfirmationActivity extends AppCompatActivity {
    private String date, appId;
    private TextView dateTV;
    private Button confirmAppIV;
    private Button cancelAppIV;

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
        appId = intent.getStringExtra(CommonConstants.ID);
    }

    private void initUI() {
        setContentView(R.layout.waiting_for_confirmation_layout);
        dateTV = (TextView) findViewById(R.id.date_tv);
        confirmAppIV = (Button) findViewById(R.id.confirm_app_btn);
        cancelAppIV = (Button) findViewById(R.id.cancel_app_btn);
    }

    private void setCallBack() {
        confirmAppIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveApplication(true);
            }
        });

        cancelAppIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveApplication(false);
            }
        });
    }

    private void saveApplication(boolean b) {
        String url = CommonConstants.SERVICE_PROCESS_APPLICATION;

        RequestParams requestParams = new RequestParams();
        requestParams.put(CommonConstants.APPLICATION_ID, appId);
        requestParams.put(CommonConstants.STATUS, b ? CommonConstants.CONFIRMED : CommonConstants.CANCELLED);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));

        APIAgent.post(url, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onFinish() {
                progressDialog.hide();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int status = response.getInt(CommonConstants.STATUS);
                    if (status == CommonConstants.STATUS_OK) {
                        Toast.makeText(WaitingForConfirmationActivity.this, response.getString(CommonConstants.MESSAGE), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(WaitingForConfirmationActivity.this, AppStatusActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(WaitingForConfirmationActivity.this, R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(WaitingForConfirmationActivity.this, R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void putData() {
        dateTV.setText(date);
    }
}
