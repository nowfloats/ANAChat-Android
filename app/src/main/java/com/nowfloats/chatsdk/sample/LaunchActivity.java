package com.nowfloats.chatsdk.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.nowfloats.chatsdk.NfChatBuilder;
import com.nowfloats.chatsdk.internal.database.PreferencesManager;

/**
 * Created by lookup on 12/10/17.
 */

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
    }

    public void startMainScreen(View view) {
        new NfChatBuilder(this)
                .setThemeColor(R.color.second_primary)
                .setToolBarDescription("Yoo intelligence")
                .setToolBarTittle("YooBOT")
                .setBussinessId("358987017890524")
                .setToolBarLogo(R.drawable.ic_ria_logo)
                .start();
    }
}
