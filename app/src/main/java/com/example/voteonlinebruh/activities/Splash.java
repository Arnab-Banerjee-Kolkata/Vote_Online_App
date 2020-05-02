package com.example.voteonlinebruh.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.voteonlinebruh.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class Splash extends AppCompatActivity {
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "sessionid";

    private static Splash _instance;
    private RequestQueue _requestQueue;
    private SharedPreferences _preferences;

    Handler handler = new Handler();
    Context mContext;
    RelativeLayout waitRel;
    WebView webView;
    String url = "";
    String cookie = "";
    CookieManager cookieManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences pref = getSharedPreferences("Voter.Online.Theme", MODE_PRIVATE);
        if (pref.getBoolean("themeLight", true))
            setTheme(R.style.AppTheme_Light);
        else
            setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_splash);

        mContext = getApplicationContext();

        waitRel = findViewById(R.id.waitRel);

        webView = (WebView) findViewById(R.id.wv1);

        url = mContext.getString(R.string.web_host) + "/Check.php";

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (; ; ) {
                        if (waitRel.getVisibility() == View.GONE)
                            break;
                    }
                    Thread.sleep(2000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        _instance = this;
        _preferences = getDefaultSharedPreferences(this);

        _requestQueue = Volley.newRequestQueue(this);

        storeCookie();
        String electionId = "1";
        String electionName = "LOK SABHA";
        SharedPreferences sharedPreferences = getSharedPreferences("ElectionDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("electionId", electionId);
        editor.putString("electionName", electionName);
        editor.apply();
    }

    void storeCookie() {
        CookieSyncManager.createInstance(mContext);
        cookieManager = CookieManager.getInstance();
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                cookieManager.setAcceptCookie(true);
                cookie = cookieManager.getCookie(url);

                //System.out.println(cookie);

                SharedPreferences sharedPreferences = getSharedPreferences("CookieDetails", MODE_PRIVATE);
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
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
