package com.anachat.chatsdk.uimodule.ui;

/**
 * Created by lookup on 09/10/17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;


import com.nowfloats.chatsdk.library.R;

import static com.anachat.chatsdk.uimodule.ui.MediaPreviewActivity.FILE_PATH;


public class VideoViewerActivity extends AppCompatActivity {
    VideoView vv;
    FloatingActionButton fab;
    TextView introText;

    public static Intent startIntent(Context context, String filePath) {
        Intent intent = new Intent(context, VideoViewerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FILE_PATH, filePath);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_viewer);

        final int mUIFlag = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        vv = findViewById(R.id.videoView);
        ProgressBar progressBar = findViewById(R.id.p_bar);
        introText = findViewById(R.id.intro_text);
        fab = findViewById(R.id.fab);

        String uri = getIntent().getExtras().getString(FILE_PATH);
        vv.setVideoURI(Uri.parse(uri));
        vv.setOnTouchListener((v, event) -> {
            vv.pause();
            progressBar.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
            return true;
        });
        fab.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            vv.start();
            introText.setVisibility(View.GONE);
            getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
            fab.setVisibility(View.GONE);
        });

        vv.setOnCompletionListener(mediaPlayer -> onBackPressed());

        vv.setOnPreparedListener(mp -> {
            progressBar.setVisibility(View.GONE);
            vv.start();
        });
    }

    @Override
    protected void onPause() {
        vv.stopPlayback();
        super.onPause();
    }

    @Override
    protected void onStop() {
        vv.stopPlayback();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        vv.stopPlayback();
        super.onDestroy();
    }
}
