package com.anachat.chatsdk.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.anachat.chatsdk.AnaChatBuilder;
import com.nowfloats.chatsdk.sample.R;

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
        new AnaChatBuilder(this)
                .setBusinessId("358987017890524")
                .setBaseUrl("https://chat-alpha.withfloats.com")
                .setThemeColor(R.color.second_primary)
                .setToolBarDescription("Yoo intelligence")
                .setToolBarTittle("YooBOT")
                .setToolBarLogo(R.drawable.ic_ria_logo)
                .start();
    }
}
