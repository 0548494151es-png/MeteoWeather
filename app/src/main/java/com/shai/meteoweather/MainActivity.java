package com.shai.meteoweather;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

    // כתובת התחזית – אפשר להחליף כאן בכתובת אחרת
    private static final String URL = "https://www.meteotech.co.il/tahazit_new.aspx?lang=he";

    private WebView web;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        FrameLayout root = new FrameLayout(this);
        web = new WebView(this);
        bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        bar.setIndeterminate(true);
        root.addView(web, new FrameLayout.LayoutParams(-1, -1));
        root.addView(bar, new FrameLayout.LayoutParams(-1, 8));
        setContentView(root);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setBuiltInZoomControls(true);
        s.setDisplayZoomControls(false);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);

        web.setWebViewClient(new WebViewClient() {
            @Override public void onPageStarted(WebView v, String u, Bitmap f) { bar.setVisibility(View.VISIBLE); }
            @Override public void onPageFinished(WebView v, String u) { bar.setVisibility(View.GONE); }
            @Override public void onReceivedError(WebView v, WebResourceRequest r, WebResourceError e) {
                if (r.isForMainFrame()) {
                    String html = "<html dir='rtl'><body style='font-family:sans-serif;text-align:center;padding:48px'>"
                        + "<h2>אין חיבור לאינטרנט</h2><p>לא ניתן לטעון את התחזית.</p>"
                        + "<button style='font-size:18px;padding:12px 24px' onclick=\"location.href='" + URL + "'\">נסה שוב</button>"
                        + "</body></html>";
                    v.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
                }
            }
        });
        web.loadUrl(URL);
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) web.goBack(); else super.onBackPressed();
    }
}
