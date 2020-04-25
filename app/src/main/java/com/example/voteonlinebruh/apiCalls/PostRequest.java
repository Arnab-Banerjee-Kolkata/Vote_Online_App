package com.example.voteonlinebruh.apiCalls;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.apiCalls.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PostRequest extends StringRequest
{
    int MY_SOCKET_TIMEOUT_MS=60000;
    Context mContext;

    public PostRequest(Context mContext, String url, Map<String, String> params, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, params, listener, errorListener);

        this.mContext=mContext;

        this.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {


        Map<String,String> headers=new HashMap<>();

        SharedPreferences sharedPreferences=mContext.getSharedPreferences("CookieDetails", Context.MODE_PRIVATE);
        String cookieStart=sharedPreferences.getString("cookieStart","");

        headers.put("Cookie",cookieStart+mContext.getString(R.string.cookie_end));


        return headers;
    }

}
