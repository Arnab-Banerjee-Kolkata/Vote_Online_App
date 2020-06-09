package com.example.voteonlinebruh.api;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.util.Map;

public class StringRequest extends com.android.volley.toolbox.StringRequest {

  private final Map<String, String> _params;

  public StringRequest(
      int method,
      String url,
      Map<String, String> params,
      Response.Listener<String> listener,
      Response.ErrorListener errorListener) {
    super(method, url, listener, errorListener);

    _params = params;
  }

  @Override
  protected Map<String, String> getParams() {
    return _params;
  }

  @Override
  protected Response<String> parseNetworkResponse(NetworkResponse response) {
    return super.parseNetworkResponse(response);
  }

  @Override
  public Map<String, String> getHeaders() throws AuthFailureError {
    Map<String, String> headers = super.getHeaders();
    return headers;
  }
}
