package com.example.voteonlinebruh.apiCalls;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;

public class VolleyGetRequest extends StringRequest {
    int MY_SOCKET_TIMEOUT_MS = 60000;

    public VolleyGetRequest(
            String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, null, listener, errorListener);

        this.setRetryPolicy(
                new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
