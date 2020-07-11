package com.example.voteonlinebruh.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.voteonlinebruh.BuildConfig;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.activities.MainActivity;
import com.example.voteonlinebruh.activities.ManageOptions;
import com.example.voteonlinebruh.activities.PrivateElectionManager;
import com.example.voteonlinebruh.activities.WaitScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

@SuppressWarnings("deprecation")
public class PrivateAPICall {

  private boolean accountLoginResponse = false;
  private int TIMES = 0;

  private String email;
  private String password;
  private Context context;
  private SharedPreferences.Editor editor;
  private PrivateElectionManager manager;

  static class myRunnable implements Runnable {
    final Intent intent;
    final Context context;

    myRunnable(Intent intent, Context context) {
      this.intent = intent;
      this.context = context;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(1000);
        WaitScreen.terminate = true;
        context.startActivity(intent);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  class RequestDelayRunnable implements Runnable {
    final int methodCode;

    RequestDelayRunnable(int methodCode) {
      this.methodCode = methodCode;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(1000);

        switch (methodCode) {
          case 1: // accountLogin
            PrivateAPICall.this.accountLogin(email, password, context, manager, editor);
            break;
        }

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @SuppressLint("SetJavaScriptEnabled")
  private void storeCookie(final Context mContext, WebView webView) {
    Log.d("COOKIE: ", "CALLED");
    TIMES++;
    String url;
    final CookieManager cookieManager;
    url = mContext.getString(R.string.private_web_host) + "/Check.php";
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
            SharedPreferences sharedPreferences =
                mContext.getSharedPreferences("CookieDetails", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cookieStart", cookie);
            editor.apply();
            Log.d("COOKIE", "RECEIEVED");
          }

          @Override
          public void onReceivedError(
              WebView view, WebResourceRequest request, WebResourceError error) {
            Log.d("Web Error: ", error.toString());
          }

          @Override
          public void onReceivedHttpError(
              WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            Log.d("Http error: ", errorResponse.toString());
          }
        });
    webView.setWebChromeClient(new WebChromeClient());
    webView.loadUrl(url);
    webView.clearCache(true);
    webView.clearHistory();
    cookieManager.removeAllCookie();
    cookieManager.removeSessionCookie();
  }

  public void accountLogin(
      final String email,
      final String password,
      final Context context,
      final PrivateElectionManager manager,
      final SharedPreferences.Editor editor) {

    this.email = email;
    this.password = password;
    this.context = context;
    this.manager = manager;
    this.editor = editor;

    Response.Listener<String> listener =
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            if (!accountLoginResponse) {
              JSONObject jsonResponse;
              try {
                Log.d("response", response);
                jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("success");
                if (success) {
                  manager.finish();
                  final Intent intent = new Intent(context, ManageOptions.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  intent.putExtra("email", email);
                  myRunnable thread = new myRunnable(intent, context);
                  new Thread(thread).start();
                  editor.putBoolean("loginStatus", true);
                  editor.putString("email", email);
                  editor.commit();
                } else {
                  boolean validAuth = jsonResponse.getBoolean("validAuth");
                  boolean validEmail = jsonResponse.getBoolean("validEmail");
                  boolean validPassword = jsonResponse.getBoolean("validPassword");
                  if (!validAuth)
                    Toast.makeText(context, "Server rejected request", Toast.LENGTH_LONG).show();
                  else if (!validEmail)
                    Toast.makeText(context, "Invalid Email ID", Toast.LENGTH_LONG).show();
                  else if (!validPassword)
                    Toast.makeText(context, "Password is incorrect !", Toast.LENGTH_LONG).show();
                  Log.d("response", response);
                }
                WaitScreen.terminate = true;
                accountLoginResponse = true;
                TIMES = 0;
              } catch (JSONException e) {
                e.printStackTrace();
                if (TIMES < 100 && !accountLoginResponse) {
                  PrivateAPICall.this.storeCookie(context, MainActivity.webView);
                  PrivateAPICall.RequestDelayRunnable requestDelayRunnable =
                      new PrivateAPICall.RequestDelayRunnable(1);
                  new Thread(requestDelayRunnable).start();
                } else if (TIMES >= 100 && !accountLoginResponse) {
                  WaitScreen.terminate = true;
                  Toast.makeText(context, "Request timed out", Toast.LENGTH_LONG).show();
                  Log.d("FAILED", "FAILED");
                }
              }
            }
          }
        };
    Response.ErrorListener errorListener =
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            if (TIMES < 100 && !accountLoginResponse) {
              PrivateAPICall.this.storeCookie(context, MainActivity.webView);
              PrivateAPICall.RequestDelayRunnable requestDelayRunnable =
                  new PrivateAPICall.RequestDelayRunnable(1);
              new Thread(requestDelayRunnable).start();
            } else if (TIMES >= 100 && !accountLoginResponse) {
              WaitScreen.terminate = true;
              Toast.makeText(context, "Request timed out", Toast.LENGTH_LONG).show();
              Log.d("FAILED", "FAILED");
            }
          }
        };
    String url = context.getString(R.string.private_web_host) + "/AccountLogin.php";
    Map<String, String> params = new HashMap<>();
    params.put("postAuthKey", BuildConfig.PRIVATE_AUTH_KEY);
    params.put("emailId", email);
    params.put("password", password);
    PostRequest postShowOptions = new PostRequest(context, url, params, listener, errorListener);
    RequestQueue queue = Volley.newRequestQueue(context);
    queue.add(postShowOptions);
  }
  public void register(
          final String email,
          final String password,
          final Context context,
          final PrivateElectionManager manager,
          final SharedPreferences.Editor editor) {

    Response.Listener<String> listener =
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                JSONObject jsonResponse;
                try {
                  Log.d("response", response);
                  jsonResponse = new JSONObject(response);
                  boolean success = jsonResponse.getBoolean("success");
                  if (success) {
                    manager.finish();
                    final Intent intent = new Intent(context, ManageOptions.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("email", email);
                    myRunnable thread = new myRunnable(intent, context);
                    new Thread(thread).start();
                    editor.putBoolean("loginStatus", true);
                    editor.putString("email",email);
                    editor.commit();
                  } else {
                    boolean validAuth = jsonResponse.getBoolean("validAuth");
                    boolean validEmail = jsonResponse.getBoolean("validEmail");
                    boolean validPassword = jsonResponse.getBoolean("validPassword");
                    if (!validAuth)
                      Toast.makeText(context, "Server rejected request", Toast.LENGTH_LONG).show();
                    else if (!validEmail)
                      Toast.makeText(context, "Invalid Email ID", Toast.LENGTH_LONG).show();
                    else if (!validPassword)
                      Toast.makeText(context, "Password is incorrect !", Toast.LENGTH_LONG).show();
                    Log.d("response", response);
                    WaitScreen.terminate = true;
                  }
                } catch (JSONException e) {
                  e.printStackTrace();
                  WaitScreen.terminate = true;
                  Log.d("FAILED", "FAILED");
                }
              }
            };
    Response.ErrorListener errorListener =
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                WaitScreen.terminate = true;
                Log.d("FAILED", "FAILED");
              }
            };
    String url = context.getString(R.string.private_web_host) + "/CreateAccount.php";
    Map<String, String> params = new HashMap<>();
    params.put("postAuthKey", BuildConfig.PRIVATE_AUTH_KEY);
    params.put("emailId", email);
    params.put("password", password);
    PostRequest postShowOptions = new PostRequest(context, url, params, listener, errorListener);
    RequestQueue queue = Volley.newRequestQueue(context);
    queue.add(postShowOptions);
  }
}
