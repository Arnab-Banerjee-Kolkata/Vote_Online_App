package com.example.voteonlinebruh.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.activities.ManageOptions;
import com.example.voteonlinebruh.activities.PrivateElectionManager;
import com.example.voteonlinebruh.activities.WaitScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PrivateAPICall {

  class myRunnable implements Runnable {
    Intent intent;
    Context context;

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

  public void accountLogin(
      final String email,
      final String password,
      final Context context,
      final PrivateElectionManager manager) {

    Response.Listener<String> listener =
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            JSONObject jsonResponse = null;
            try {
              Log.d("response", response);
              jsonResponse = new JSONObject(response);
              boolean success = jsonResponse.getBoolean("success");
              if (success) {
                final Intent intent = new Intent(context, ManageOptions.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("email", email);
                myRunnable thread = new myRunnable(intent, context);
                new Thread(thread).start();
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
    String url = context.getString(R.string.private_web_host) + "/AccountLogin.php";
    Map<String, String> params = new HashMap<>();
    params.put("postAuthKey", context.getString(R.string.private_auth_key));
    params.put("emailId", email);
    params.put("password", password);
    PostRequest postShowOptions = new PostRequest(context, url, params, listener, errorListener);
    RequestQueue queue = Volley.newRequestQueue(context);
    queue.add(postShowOptions);
  }
}
