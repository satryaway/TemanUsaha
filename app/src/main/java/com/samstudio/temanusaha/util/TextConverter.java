package com.samstudio.temanusaha.util;

import android.content.Context;

import com.samstudio.temanusaha.R;
import com.samstudio.temanusaha.TemanUsahaApplication;

/**
 * Created by satryaway on 10/17/2015.
 * text converter
 */
public class TextConverter {

    public static String convertStatsCodeToString(int code) {
        Context context = TemanUsahaApplication.getInstance();
        String convertedCode = "";
        switch (code) {
            case 1 : convertedCode = context.getString(R.string.administration_process); break;
            case 2 : convertedCode = context.getString(R.string.meet_up_process); break;
            case 3 : convertedCode = context.getString(R.string.waiting_for_approval); break;
            case 4 : convertedCode = context.getString(R.string.approved); break;
            default: convertedCode = context.getString(R.string.rejected); break;
        }

        return convertedCode;
    }
}
