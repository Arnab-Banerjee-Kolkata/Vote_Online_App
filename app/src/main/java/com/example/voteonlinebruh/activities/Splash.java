package com.example.voteonlinebruh.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.example.voteonlinebruh.R;

@SuppressWarnings("deprecation")
public class Splash extends AppCompatActivity {

  private final Handler handler = new Handler();
    private RelativeLayout waitRel;

    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final SharedPreferences pref = getSharedPreferences("Voter.Online.Theme", MODE_PRIVATE);
    if (pref.getBoolean("themeLight", true)) setTheme(R.style.AppTheme_Light);
    else setTheme(R.style.AppTheme_Dark);
    setContentView(R.layout.activity_splash);

        Context mContext = getApplicationContext();

    waitRel = findViewById(R.id.waitRel);

        WebView webView = findViewById(R.id.wv1);

    Thread thread =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                try {
                  for (; ; ) {
                    if (waitRel.getVisibility() == View.GONE) break;
                  }
                  Thread.sleep(2000);
                  handler.post(
                      new Runnable() {
                        @Override
                        public void run() {
                          Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                          startActivity(intent);
                          overridePendingTransition(
                              android.R.anim.fade_in, android.R.anim.fade_out);
                          finish();
                        }
                      });
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            });
    thread.start();
    storeCookie(mContext, webView, waitRel);
  }

  @SuppressLint("SetJavaScriptEnabled")
  private static void storeCookie(
          final Context mContext, WebView webView, final RelativeLayout waitRel) {
    String url;
    final CookieManager cookieManager;
    url = mContext.getString(R.string.web_host) + "/Check.php";

    CookieSyncManager.createInstance(mContext);
    cookieManager = CookieManager.getInstance();
    webView.getSettings().setJavaScriptEnabled(true);

    webView.setWebViewClient(
        new WebViewClient() {

          @Override
          public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            cookieManager.setAcceptCookie(true);
            final String cookie = cookieManager.getCookie(url);

            // System.out.println(cookie);

            SharedPreferences sharedPreferences =
                mContext.getSharedPreferences("CookieDetails", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cookieStart", cookie);
            editor.apply();

            waitRel.setVisibility(View.GONE);
          }
        });

    webView.setWebChromeClient(new WebChromeClient());
    webView.loadUrl(url);

    webView.clearCache(true);
    webView.clearHistory();

    cookieManager.removeAllCookie();
    cookieManager.removeSessionCookie();
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    View decorView = getWindow().getDecorView();
    decorView.setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
  }

  @Override
  public void onBackPressed() {}
}
