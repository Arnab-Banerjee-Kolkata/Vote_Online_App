package com.example.voteonlinebruh.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.example.voteonlinebruh.R;

import java.util.HashMap;
import java.util.Map;

class PostRequest extends StringRequest {
  private final Context mContext;

  public PostRequest(
      Context mContext,
      String url,
      Map<String, String> params,
      Response.Listener<String> listener,
      Response.ErrorListener errorListener) {
    super(Method.POST, url, params, listener, errorListener);
    this.mContext = mContext;
    int MY_SOCKET_TIMEOUT_MS = 60000;
    this.setRetryPolicy(
        new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
  }

  @Override
  public Map<String, String> getHeaders() {

    Map<String, String> headers = new HashMap<>();
    SharedPreferences sharedPreferences =
        mContext.getSharedPreferences("CookieDetails", Context.MODE_PRIVATE);
    String cookieStart = sharedPreferences.getString("cookieStart", "");
    headers.put("Cookie", cookieStart + mContext.getString(R.string.cookie_end));
    Log.d("REQUEST: ", "SENT");
    return headers;
  }
}
