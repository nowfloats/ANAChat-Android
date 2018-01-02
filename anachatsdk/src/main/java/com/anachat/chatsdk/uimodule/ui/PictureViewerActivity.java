package com.anachat.chatsdk.uimodule.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.anachat.chatsdk.library.R;
import com.anachat.chatsdk.uimodule.utils.TouchImageView;

import java.io.File;

import static com.anachat.chatsdk.uimodule.ui.MediaPreviewActivity.FILE_PATH;

/**
 * Created by lookup on 09/10/17.
 */

public class PictureViewerActivity extends AppCompatActivity {
    TouchImageView imageView;


    public static Intent startIntent(Context context, String filePath) {
        Intent intent = new Intent(context, PictureViewerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FILE_PATH, filePath);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_viewer);
        imageView = findViewById(R.id.image);
        String filePath = getIntent().getExtras().getString(FILE_PATH);
        if (filePath != null) {
            if (!filePath.startsWith("http")) {
                Uri uri = Uri.fromFile(new File(filePath));
                Glide.with(PictureViewerActivity.this)
                        .load(uri)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_placeholder)
                                .centerCrop()
                                .dontTransform())
                        .into(imageView);
            } else {
                Glide.with(PictureViewerActivity.this)
                        .load(filePath)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_placeholder)
                                .centerCrop()
                                .dontTransform())
                        .into(imageView);
            }
        }
    }
}
