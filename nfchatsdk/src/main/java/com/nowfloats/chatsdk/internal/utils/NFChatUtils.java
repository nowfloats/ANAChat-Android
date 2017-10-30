package com.nowfloats.chatsdk.internal.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Created by lookup on 20/09/17.
 */

public class NFChatUtils {

    @SuppressLint("HardwareIds")
    public static String getUUID(Context context) {
        String deviceId = "";
        if ((context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == PackageManager.PERMISSION_GRANTED) && context
                .getSystemService(Context.TELEPHONY_SERVICE) != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                if (((TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE))
                        .getImei() != null) {
                    deviceId = context.getSystemService(TelephonyManager.class).getImei();
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.getSystemService(TelephonyManager.class).getDeviceId() != null) {
                    deviceId = context.getSystemService(TelephonyManager.class).getDeviceId();
                }
            }
        } else {
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
