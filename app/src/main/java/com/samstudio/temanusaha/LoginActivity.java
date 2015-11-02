package com.samstudio.temanusaha;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.samstudio.temanusaha.util.CommonConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by satryaway on 9/10/2015.
 * Login Activity
 */
public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private EditText emailET, passwordET;
    private String email, password;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = TemanUsahaApplication.getInstance().getSharedPreferences().getString(CommonConstants.GCM_TOKEN, "");
        initUI();
        setCallBack();
    }

    private void initUI() {
        setContentView(R.layout.login_layout);
        loginBtn = (Button) findViewById(R.id.login_btn);
        emailET = (EditText) findViewById(R.id.email_et);
        passwordET = (EditText) findViewById(R.id.password_et);
    }

    private void setCallBack() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFieldFilled()) {
                    email = emailET.getText().toString();
                    password = passwordET.getText().toString();
                    validateParameter();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.input_email_and_password, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validateParameter() {
        String url =CommonConstants.SERVICE_LOGIN_CUSTOMER;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.hide();
                            JSONObject jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt(CommonConstants.STATUS);
                            if (status == CommonConstants.STATUS_OK) {
                                Toast.makeText(LoginActivity.this, R.string.login_succeed_text, Toast.LENGTH_SHORT).show();
                                saveDataInPreferences(jsonObject.getJSONObject(CommonConstants.RETURN_DATA));
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, R.string.login_failed_text, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(CommonConstants.EMAIL, email);
                params.put(CommonConstants.PASSWORD, password);
                params.put(CommonConstants.DEVICE_ID, token);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void saveDataInPreferences(JSONObject jsonObject) {
        SharedPreferences.Editor editor = TemanUsahaApplication.getInstance().getSharedPreferences().edit();
        editor.apply();
    }

    private boolean isFieldFilled() {
        return !(emailET.getText().toString().equals("") || passwordET.getText().toString().equals(""));
    }
}
