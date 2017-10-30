package com.nowfloats.chatsdk.uimodule.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

/**
 * Created by lookup on 30/08/17.
 */

public class InputIntents {
    public static final int REQUEST_TAKE_GALLERY_VIDEO = 101;
    public static final int REQUEST_TAKE_GALLERY_IMAGE = 102;
    public static final int REQUEST_TAKE_FILE = 103;

    public static void pickLocation(Context context, Activity activity) {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            throw new IllegalStateException("please take permission first");
        } else {
//            try {
//                LocationServices.getFusedLocationProviderClient(activity).getLastLocation()
//                        .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
//                            @Override
//                            public void onSuccess(Location location) {
//                                // Got last known location. In some rare situations this can be null.
//                                if (location != null) {
//                                }
//                            }
//                        });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }

    }

    public static void pickPhoneNumberFromContact(Context context) {

    }

    public static void pickCurrentTime(Context context) {

    }

    public static Intent pickImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
    }

    public static Intent pickVideoFromGallery() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
    }

    public static Intent pickFileFromGallery() {
        Intent intent = new Intent();
        intent.setType("file/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
    }

    public static Intent getBrowserIntent(String url) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }
}
