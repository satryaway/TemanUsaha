package com.samstudio.temanusaha.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.samstudio.temanusaha.AdministrationProcessActivity;
import com.samstudio.temanusaha.AppApprovedActivity;
import com.samstudio.temanusaha.AppRejectedActivity;
import com.samstudio.temanusaha.MeetUpProcessActivity;
import com.samstudio.temanusaha.R;
import com.samstudio.temanusaha.WaitingForApprovalActivity;
import com.samstudio.temanusaha.adapters.AppStatusListAdapter;
import com.samstudio.temanusaha.entities.Partner;
import com.samstudio.temanusaha.util.CommonConstants;
import com.samstudio.temanusaha.util.Seeder;

import java.util.ArrayList;
import java.util.List;

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
    private List<Partner> partnerList = new ArrayList<>();

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
        listAdapter = new AppStatusListAdapter(getActivity(), new ArrayList<Partner>());
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;

                switch (partnerList.get(position).getStatus()) {
                    case 1:
                        intent = new Intent(getActivity(), AdministrationProcessActivity.class);
                        intent.putExtra(CommonConstants.DATE, partnerList.get(position).getDate());
                        break;

                    case 2:
                        intent = new Intent(getActivity(), MeetUpProcessActivity.class);
                        intent.putExtra(CommonConstants.DATE, partnerList.get(position).getDate());
                        intent.putExtra(CommonConstants.PHONE_NUMBER, partnerList.get(position).getPhoneNumber());
                        break;

                    case 4:
                        intent = new Intent(getActivity(), AppApprovedActivity.class);
                        intent.putExtra(CommonConstants.DATE, partnerList.get(position).getDate());
                        break;

                    case 5:
                        intent = new Intent(getActivity(), AppRejectedActivity.class);
                        break;

                    default:
                        intent = new Intent(getActivity(), WaitingForApprovalActivity.class);
                        intent.putExtra(CommonConstants.DATE, partnerList.get(position).getDate());
                        break;
                }

                startActivity(intent);
            }
        });
    }

    private void setCallBack() {

    }

    private void getData() {
        partnerList = position == 0 ? Seeder.getPartners() : Seeder.getPartnersWithResult();
        listAdapter.update(partnerList);
    }
}