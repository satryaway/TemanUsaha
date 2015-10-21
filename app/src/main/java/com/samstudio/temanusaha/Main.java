package com.samstudio.temanusaha;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by aneh on 8/15/2014.
 */
public class Main extends Activity {

    ShareExternalServer appUtil;
    String regId;
    AsyncTask<Void, Void, String> shareRegidTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        appUtil = new ShareExternalServer();

        regId = getIntent().getStringExtra("regId");
        Log.d("GCMMainActivity", "regId: " + regId);

        final Context context = this;
        shareRegidTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = appUtil.shareRegIdWithAppServer(context, regId);
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                shareRegidTask = null;
                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_LONG).show();
            }

        };
        shareRegidTask.execute(null, null, null);
    }

}

