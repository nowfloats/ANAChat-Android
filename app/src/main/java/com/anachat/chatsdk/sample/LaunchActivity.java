package com.anachat.chatsdk.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.anachat.chatsdk.AnaChatBuilder;
import com.anachat.chatsdk.internal.database.PreferencesManager;


/**
 * Created by lookup on 12/10/17.
 */

public class LaunchActivity extends AppCompatActivity {
    public static final String BUSINESSID = "358987017890524";
    public static final String BASE_URL = "https://chat-alpha.withfloats.com";
    private Long waitTime = 2000L;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            if (message.equals("now start")) {
                waitTime = 5000L;
//                startThread();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        if (PreferencesManager.getsInstance(this).getUserName().isEmpty()) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                    new IntentFilter("startBot"));
        }
//        else
//            startThread();
    }

//    public void startThread(View view) {
//        Handler handler
//                = new Handler();
//        handler.postDelayed(this::startMainScreen, waitTime);
//    }

    /**
     * starting bot onclick of button
     */
    public void startMainScreen(View view) {
        if (!isFinishing())
            new AnaChatBuilder(this)
                    .setBusinessId(BUSINESSID)
                    .setBaseUrl(BASE_URL)
                    .setThemeColor(R.color.colorPrimary)
                    .setToolBarDescription("Talk to BOT - Available")
                    .setToolBarTittle("ANA")
                    .setToolBarLogo(R.drawable.ic_placeholder)
                    .start();
//        if (!PreferencesManager.getsInstance(this).getUserName().isEmpty()) {
//            finish();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

}
