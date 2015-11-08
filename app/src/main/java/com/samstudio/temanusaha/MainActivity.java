package com.samstudio.temanusaha;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

/**
 * Created by satryaway on 9/12/2015.
 * Show the main activity if user has logged in
 */
public class MainActivity extends AppCompatActivity {
    private ImageView optionMenuIV, profileIV;
    private Context context = MainActivity.this;
    private ImageView grabIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallback();
        putData();
    }

    private void initUI() {
        setContentView(R.layout.main_layout);
        optionMenuIV = (ImageView) findViewById(R.id.option_menu_iv);
        profileIV = (ImageView) findViewById(R.id.profile_iv);
        grabIV = (ImageView) findViewById(R.id.grab_iv);
    }

    private void setCallback() {
        optionMenuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, optionMenuIV);
                popup.getMenuInflater().inflate(R.menu.option_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent();
                        switch (item.getItemId()) {
                            case R.id.term_of_service:
                                intent.setClass(context, TermsActivity.class);
                                break;

                            case R.id.change_password:
                                intent.setClass(context, ChangePasswordActivity.class);
                                break;
                            default:
                                break;
                        }
                        startActivity(intent);
                        return true;
                    }
                });

                popup.show();
            }
        });

        grabIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PickShapeActivity.class));
            }
        });

        profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CustomerProfileActivity.class));
            }
        });
    }

    private void putData() {
        if(!TemanUsahaApplication.getInstance().isCustomer()) {
            grabIV.setImageResource(R.drawable.get_cust);
        }
    }
}
