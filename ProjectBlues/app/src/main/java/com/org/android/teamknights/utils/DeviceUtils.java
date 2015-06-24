package com.org.android.teamknights.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class contains utility methods that are specific to this device
 *
 * Created by fangb on 12/29/2014.
 */
public class DeviceUtils {

    /**
     * Returns true if the device currently has an internet connection
     * @param activity
     * @return
     */
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
