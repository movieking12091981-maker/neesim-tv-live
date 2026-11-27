package com.neesimtv.live;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
    private WebView webView;
    private FrameLayout fullScreenContainer;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(android.graphics.Color.rgb(42, 12, 75));
        getWindow().setNavigationBarColor(android.graphics.Color.rgb(42, 12, 75));
        fullScreenContainer = new FrameLayout(this);
        webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public void onShowCustomView(View view, CustomViewCallback callback) {
                customView = view; customViewCallback = callback;
                webView.setVisibility(View.GONE);
                fullScreenContainer.addView(view);
                getWindow().getDecorView().setSystemUiVisibility(5894 | 1024 | 512);
            }
            @Override public void onHideCustomView() {
                if (customView != null) {
                    fullScreenContainer.removeView(customView);
                    customView = null;
                    webView.setVisibility(View.VISIBLE);
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if (customViewCallback != null) customViewCallback.onCustomViewHidden();
                }
            }
        });
        fullScreenContainer.addView(webView);
        setContentView(fullScreenContainer);
        webView.loadUrl("https://www.youtube.com/embed/live_stream?channel=UCj3aX7jE7JzsYlAw8f1gdKg&autoplay=0&playsinline=1&rel=0");
    }

    @Override public void onBackPressed() {
        if (customView != null) {
            webView.getWebChromeClient().onHideCustomView();
        } else if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
