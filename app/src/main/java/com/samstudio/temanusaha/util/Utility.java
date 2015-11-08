package com.samstudio.temanusaha.util;

import com.samstudio.temanusaha.entities.Partner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by satryaway on 9/21/2015.
 */
public class Utility {
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static List<Partner> parsePartner(JSONObject response) {
        List<Partner> partnerList = new ArrayList<>();
        try {
            JSONArray jsonArray = response.getJSONArray(CommonConstants.RETURN_DATA);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Partner partner = new Partner();
                partner.setId(Integer.valueOf(object.getString(CommonConstants.ID)));
                partner.setFirstName(object.getString(CommonConstants.FIRST_NAME));
                partner.setLastName(object.getString(CommonConstants.LAST_NAME));
                partner.setCompany(object.getString(CommonConstants.COMPANY));
                partner.setLat(Double.valueOf(object.getString(CommonConstants.LATITUDE)));
                partner.setLng(Double.valueOf(object.getString(CommonConstants.LONGITUDE)));
                partnerList.add(partner);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return partnerList;
    }
}
