package com.e_schedule.e_schedule;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Kang Juy on 10/11/2016.
 */

public class CheckNetClass {

    public static Boolean checknetwork(Context mContext) {

        NetworkInfo info = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (info == null || !info.isConnected())
        {
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }

        return true;

    }
}