package com.anachat.chatsdk.uimodule.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anachat.chatsdk.internal.database.PreferencesManager;
import com.anachat.chatsdk.library.R;

/**
 * Created by lookup on 11/01/18.
 */

public class WebViewActivity extends AppCompatActivity {
    TextView actionBarTitle;
    Typeface calibri;
    ProgressBar progressBar;
    WebView webview;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        context = this;
        init();
        findViewById(R.id.rl_toolbar).setBackgroundColor
                (Color.parseColor(PreferencesManager.getsInstance(this).getThemeColor()));
        (findViewById(R.id.back_layout)).setOnClickListener(v -> onBackPressed());
//        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
        webview.getSettings().setSaveFormData(true);
        webview.getSettings().setGeolocationEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        if (getIntent().getExtras() != null) {
            String url = getIntent().getExtras().getString("URL");
            webview.loadUrl(url);
        }

        webview.setWebViewClient(new WebViewClient() {

            public void onReceivedError(WebView view,
                                        int errorCode, String description, String failingUrl) {
                Toast.makeText(context, "Error loading page.", Toast.LENGTH_SHORT).show();
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                Log.d("" + webview.getUrl(), ":: Progress: " + newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    void init() {
//        calibri = Typeface.createFromAsset(getAssets(), "fonts/calibri.ttf");
        actionBarTitle = findViewById(R.id.action_bar_title);
        webview = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progress_bar);
        actionBarTitle.setTypeface(calibri);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack())
            webview.goBack();
        else
            super.onBackPressed();
    }
}