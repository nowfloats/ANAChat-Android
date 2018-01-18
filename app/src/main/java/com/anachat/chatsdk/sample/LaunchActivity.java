package com.anachat.chatsdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.anachat.chatsdk.AnaChatBuilder;
import com.anachat.chatsdk.AnaCore;
import com.anachat.chatsdk.LocationPickListener;
import com.anachat.chatsdk.internal.database.PreferencesManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;


/**
 * Created by lookup on 12/10/17.
 */

public class LaunchActivity extends AppCompatActivity implements LocationPickListener {
    public static final String BUSINESSID = "your__business_id";
    public static final String BASE_URL = "your_url";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        AnaCore.registerUser(this, "user_id", BUSINESSID, BASE_URL);
    }


    /**
     * starting bot onclick of button
     */
    public void startMainScreen(View view) {
        new AnaChatBuilder(this)
                .setBusinessId(BUSINESSID)
                .setBaseUrl(BASE_URL)
                .setThemeColor(R.color.colorPrimary)
                .setToolBarDescription("Talk to BOT - Available")
                .setToolBarTittle("ANA")
                .setToolBarLogo(R.drawable.ic_ana)
                .registerLocationSelectListener(this)
                .start();

        if (PreferencesManager.getsInstance(this).getUserName().isEmpty()) {
            Toast.makeText(this, "User not Registered", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Intent pickLocation(Activity activity) {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent placePickerIntent = builder.build(activity);
            placePickerIntent.putExtra("primary_color",
                    Color.parseColor(PreferencesManager.getsInstance(activity).getThemeColor()));
            placePickerIntent.putExtra("primary_color_dark",
                    ContextCompat.getColor(activity, R.color.gray_light));
            return placePickerIntent;
        } catch (GooglePlayServicesRepairableException |
                GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void sendLocation(Intent data) {
        Place place = PlacePicker.getPlace(this, data);
        LatLng latLng = place.getLatLng();
        AnaCore.sendLocation(latLng.latitude, latLng.longitude, this);
    }

}

