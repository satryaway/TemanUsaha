package com.jixstreet.temanusaha.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.jixstreet.temanusaha.AdministrationProcessActivity;
import com.jixstreet.temanusaha.AppConfirmedActivity;
import com.jixstreet.temanusaha.AppRejectedActivity;
import com.jixstreet.temanusaha.MeetUpProcessActivity;
import com.jixstreet.temanusaha.R;
import com.jixstreet.temanusaha.TemanUsahaApplication;
import com.jixstreet.temanusaha.WaitingForConfirmationActivity;
import com.jixstreet.temanusaha.adapters.AppStatusListAdapter;
import com.jixstreet.temanusaha.entities.Application;
import com.jixstreet.temanusaha.util.APIAgent;
import com.jixstreet.temanusaha.util.CommonConstants;
import com.jixstreet.temanusaha.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by satryaway on 10/17/2015.
 * Fragment for application status display
 */
public class AppStatusFragment extends Fragment {
    private View view;
    private static String POSITION_ARG = "POSITION";
    private ListView listView;
    private AppStatusListAdapter listAdapter;
    private int position;
    private List<Application> applicationList = new ArrayList<>();

    public static AppStatusFragment newInstance(int position) {
        AppStatusFragment appStatusFragment = new AppStatusFragment();
        Bundle b = new Bundle();
        b.putInt(POSITION_ARG, position);
        appStatusFragment.setArguments(b);

        return appStatusFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(POSITION_ARG);
        view = inflater.inflate(R.layout.app_status_fragment_layout, null);
        initUI();
        setCallBack();

        getData();
        return view;
    }

    private void initUI() {
        listView = (ListView) view.findViewById(R.id.app_status_lv);
        listAdapter = new AppStatusListAdapter(getActivity(), new ArrayList<Application>());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;

                switch (applicationList.get(position).getStatus()) {
                    case "administration process":
                        intent = new Intent(getActivity(), AdministrationProcessActivity.class);
                        intent.putExtra(CommonConstants.DATE, applicationList.get(position).getDatetime());
                        break;

                    case "meet up":
                        intent = new Intent(getActivity(), MeetUpProcessActivity.class);
                        intent.putExtra(CommonConstants.DATE, applicationList.get(position).getProcessDatetime());
                        intent.putExtra(CommonConstants.PHONE_NUMBER, applicationList.get(position).getPartner().getPhoneNumber());
                        intent.putExtra(CommonConstants.MEETUP_DATETIME, applicationList.get(position).getMeetupDatetime());
                        intent.putExtra(CommonConstants.MEETUP_VENUE, applicationList.get(position).getMeetupVenue());
                        intent.putExtra(CommonConstants.NOTES, applicationList.get(position).getNotes());
                        break;

                    case "process":
                        intent = new Intent(getActivity(), WaitingForConfirmationActivity.class);
                        intent.putExtra(CommonConstants.DATE, applicationList.get(position).getDatetime());
                        intent.putExtra(CommonConstants.ID, applicationList.get(position).getId());
                        break;

                    case "rejected":
                        intent = new Intent(getActivity(), AppRejectedActivity.class);
                        break;

                    case "cancelled":
                        intent = new Intent(getActivity(), AppRejectedActivity.class);
                        intent.putExtra(CommonConstants.IS_CANCELLED, true);
                        break;

                    case "confirmed":
                        intent = new Intent(getActivity(), AppConfirmedActivity.class);
                        intent.putExtra(CommonConstants.IS_CONFIRMED, true);
                        intent.putExtra(CommonConstants.DATE, applicationList.get(position).getProcessDatetime());
                        break;

                    default:
                        intent = new Intent(getActivity(), AppConfirmedActivity.class);
                        intent.putExtra(CommonConstants.DATE, applicationList.get(position).getDatetime());
                        break;
                }

                startActivity(intent);
            }
        });
    }

    private void setCallBack() {

    }

    private void getData() {
        /*applicationList = position == 0 ? Seeder.getPartners() : Seeder.getPartnersWithResult();
        listAdapter.update(applicationList);*/
        String url = CommonConstants.SERVICE_GET_CONNECTED_PARTNER + TemanUsahaApplication.getInstance().getSharedPreferences().getInt(CommonConstants.ID, 1);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.please_wait));

        APIAgent.get(url, null, new JsonHttpResponseHandler(){

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
                        JSONArray jsonArray = response.getJSONArray(CommonConstants.RETURN_DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Application application = Utility.parseApplications(jsonArray.getJSONObject(i));
                            if (position == 0 && (application.getStatus().equals(CommonConstants.ADMINISTRATION_PROCESS) || application.getStatus().equals(CommonConstants.MEET_UP) || application.getStatus().equals(CommonConstants.PROCESS))) {
                                applicationList.add(application);
                            }

                            if (position == 1 && (application.getStatus().equals(CommonConstants.CONFIRMED) || application.getStatus().equals(CommonConstants.REJECTED) || application.getStatus().equals(CommonConstants.CANCELLED) || application.getStatus().equals(CommonConstants.APPROVED))) {
                                applicationList.add(application);
                            }
                        }

                        listAdapter.update(applicationList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getActivity(), R.string.RTO, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), R.string.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }
}