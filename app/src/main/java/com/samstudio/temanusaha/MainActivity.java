package com.samstudio.temanusaha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

/**
 * Created by satryaway on 9/12/2015.
 * Show the main activity if user has logged in
 */
public class MainActivity extends AppCompatActivity {
    private ImageView optionMenuIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        setCallback();
    }

    private void initUI() {
        setContentView(R.layout.main_layout);
        optionMenuIV = (ImageView) findViewById(R.id.option_menu_iv);
    }

    private void setCallback() {
        optionMenuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, optionMenuIV);
                popup.getMenuInflater().inflate(R.menu.option_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();
            }
        });
    }
}
