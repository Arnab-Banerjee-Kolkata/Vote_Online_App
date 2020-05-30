package com.example.voteonlinebruh.apiCalls;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.voteonlinebruh.R;
import com.example.voteonlinebruh.activities.MainActivity;
import com.example.voteonlinebruh.activities.ResultList;
import com.example.voteonlinebruh.activities.OtpPage;
import com.example.voteonlinebruh.activities.ResultsDetailed;
import com.example.voteonlinebruh.activities.ResultsSimplified;
import com.example.voteonlinebruh.activities.Thanks;
import com.example.voteonlinebruh.activities.VotingPage;
import com.example.voteonlinebruh.activities.WaitScreen;
import com.example.voteonlinebruh.models.ConstituencyWiseResultList;
import com.example.voteonlinebruh.models.PartywiseResultList;
import com.example.voteonlinebruh.models.PublicCandidate;
import com.example.voteonlinebruh.models.ResultListItem;
import com.example.voteonlinebruh.models.StateListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ServerCall {
    class myRunnable implements Runnable {
        Intent intent;
        Context mContext;

        myRunnable(Intent intent, Context mContext) {
            this.intent = intent;
            this.mContext = mContext;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                WaitScreen.terminate = true;
                mContext.startActivity(intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void storeCookie(final Context mContext, WebView webView) {
        String url = "";
        final CookieManager cookieManager;
        url = mContext.getString(R.string.web_host) + "/Check.php";
        CookieSyncManager.createInstance(mContext);
        cookieManager = CookieManager.getInstance();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                cookieManager.setAcceptCookie(true);
                final String cookie = cookieManager.getCookie(url);
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("CookieDetails", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cookieStart", cookie);
                editor.apply();
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
        webView.clearCache(true);
        webView.clearHistory();
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
    }

    public void validateBoothOtp(final String boothId, final String otp, final Context mContext, final OtpPage otpPage) {
        final ArrayList<PublicCandidate> candidates = new ArrayList<PublicCandidate>();
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> params = new HashMap<>();
                JSONObject jsonResponse = null;

                try {
                    jsonResponse = new JSONObject(response);
                    boolean validAuth = jsonResponse.getBoolean("validAuth");
                    boolean validBooth = jsonResponse.getBoolean("validBooth");
                    boolean validOtp = jsonResponse.getBoolean("validOtp");
                    if (!validAuth) {
                        Toast.makeText(mContext, "Un-authourised access request", Toast.LENGTH_LONG).show();
                        WaitScreen.terminate = true;
                    } else if (!validBooth) {
                        Toast.makeText(mContext, "Invalid Booth ID !", Toast.LENGTH_LONG).show();
                        WaitScreen.terminate = true;
                    } else if (!validOtp) {
                        Toast.makeText(mContext, "Incorrect OTP !", Toast.LENGTH_LONG).show();
                        WaitScreen.terminate = true;
                    } else {
                        JSONObject parameters = jsonResponse.getJSONObject("sub");
                        boolean success = parameters.getBoolean("success");
                        if (!success) {
                            boolean validInternalAuth = parameters.getBoolean("validInternalAuth");
                            boolean validElection = parameters.getBoolean("validElection");
                            boolean validParentElection = parameters.getBoolean("validParentElection");
                            if (!validInternalAuth)
                                Toast.makeText(mContext, "Invalid Key", Toast.LENGTH_LONG).show();
                            if (!validElection)
                                Toast.makeText(mContext, "Invalid election ID !", Toast.LENGTH_LONG).show();
                            if (!validParentElection)
                                Toast.makeText(mContext, "You have selected the wrong election !", Toast.LENGTH_LONG).show();
                            WaitScreen.terminate = true;
                        } else {
                            JSONArray array2 = parameters.getJSONArray("candidates");
                            int len2 = array2.length();

                            for (int i = 0; i < len2; i++) {
                                JSONObject elections = array2.getJSONObject(i);
                                candidates.add(new PublicCandidate(elections.getString("id"),
                                        elections.getString("name"),
                                        elections.getString("party"),
                                        elections.getString("img"),
                                        elections.getString("symbol")));
                            }
                            candidates.add(new PublicCandidate("NOTA", "NONE OF THE ABOVE", "NOTA", "", "https://www.atheer.om/en/wp-content/uploads/sites/2/2017/12/12122017_044525_0-8.jpg"));
                            final Intent intent = new Intent(mContext, VotingPage.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("list", candidates);
                            intent.putExtra("boothId", boothId);
                            myRunnable thread = new myRunnable(intent, mContext);
                            new Thread(thread).start();
                            otpPage.finish();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("response error", response);
                    storeCookie(mContext, MainActivity.webView);
                    validateBoothOtp(boothId, otp, mContext, otpPage);
                    WaitScreen.terminate = true;
                    params.put("message", "Failed");
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                WaitScreen.terminate = true;
                Toast.makeText(mContext, "Error occured. Try again", Toast.LENGTH_LONG).show();
                storeCookie(mContext, MainActivity.webView);
                validateBoothOtp(boothId, otp, mContext, otpPage);
            }
        };


        String url = mContext.getString(R.string.web_host) + "/ValidateBoothOtp.php";
        Map<String, String> params = new HashMap<>();
        params.put("boothId", boothId);
        params.put("otp", otp);
        params.put("postAuthKey", mContext.getString(R.string.post_auth_key));

        PostRequest postValidateOtp = new PostRequest(mContext, url, params, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(postValidateOtp);
    }

    public void getPublicResultList(final Context mContext) {
        final ArrayList<ResultListItem> resultlist = new ArrayList<ResultListItem>();

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonResponse = null;

                try {
                    jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    boolean valid = jsonResponse.getBoolean("validAuth");
                    if (!success | !valid) {
                        Toast.makeText(mContext, "Server rejected request !", Toast.LENGTH_SHORT).show();
                        WaitScreen.terminate = true;
                    } else {
                        JSONArray array = jsonResponse.getJSONArray("elections");
                        int len = array.length();

                        for (int i = 0; i < len; i++) {
                            JSONObject elections = array.getJSONObject(i);
                            resultlist.add(new ResultListItem(elections.getInt("electionId"),
                                    elections.getInt("status"),
                                    elections.getInt("year"),
                                    elections.getString("type"),
                                    elections.getString("name"),
                                    elections.getString("stateName")));
                        }
                        final Intent intent = new Intent(mContext, ResultList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("list", resultlist);

                        myRunnable thread = new myRunnable(intent, mContext);
                        new Thread(thread).start();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("response error", response);
                    storeCookie(mContext, MainActivity.webView);
                    getPublicResultList(mContext);
                    WaitScreen.terminate = true;
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                WaitScreen.terminate = true;
                Toast.makeText(mContext, "Error Occured", Toast.LENGTH_LONG).show();
                storeCookie(mContext, MainActivity.webView);
                getPublicResultList(mContext);
            }
        };


        String url = mContext.getString(R.string.web_host) + "/ShowCompletedElectionList.php";
        Map<String, String> params = new HashMap<>();
        params.put("postAuthKey", mContext.getString(R.string.post_auth_key));


        PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(postShowOptions);
    }

    public void getOverallResult(final String type, final int electionId, final Context mContext, final ResultsSimplified resultsSimplified, final boolean release) {
        final ArrayList<PartywiseResultList> partyresultlist = new ArrayList<PartywiseResultList>();

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonResponse = null;

                try {
                    jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    boolean valid = jsonResponse.getBoolean("validAuth");
                    boolean validElection = jsonResponse.getBoolean("validElection");
                    boolean validType = jsonResponse.getBoolean("validType");

                    if (!success) {
                        if (!valid)
                            Toast.makeText(mContext, "Server rejected request !", Toast.LENGTH_SHORT).show();
                        else if (!validElection)
                            Toast.makeText(mContext, "Invalid election ID !", Toast.LENGTH_SHORT).show();
                        else if (!validType)
                            Toast.makeText(mContext, "Invalid election type !", Toast.LENGTH_SHORT).show();
                        WaitScreen.terminate = true;
                    } else {
                        int status = jsonResponse.getInt("status");
                        int totalSeats = jsonResponse.getInt("totalSeats");
                        JSONArray array = jsonResponse.getJSONArray("results");
                        int len = array.length();

                        for (int i = 0; i < len; i++) {
                            JSONObject elections = array.getJSONObject(i);
                            partyresultlist.add(new PartywiseResultList(elections.getString("partyName"),
                                    elections.getInt("seatsWon"),
                                    elections.getString("partySymbol")));
                        }
                        if (release) {
                            resultsSimplified.populate(partyresultlist);
                        } else {
                            final Intent intent = new Intent(mContext, ResultsSimplified.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("list", partyresultlist);
                            intent.putExtra("electionId", electionId);
                            intent.putExtra("type", type);
                            intent.putExtra("status", status);
                            intent.putExtra("totalSeats", totalSeats);
                            myRunnable thread = new myRunnable(intent, mContext);
                            new Thread(thread).start();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("response error", response);
                    storeCookie(mContext, MainActivity.webView);
                    getOverallResult(type, electionId, mContext, resultsSimplified, release);
                    WaitScreen.terminate = true;
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                WaitScreen.terminate = true;
                Toast.makeText(mContext, "Error Occured", Toast.LENGTH_LONG).show();
                storeCookie(mContext, MainActivity.webView);
                getOverallResult(type, electionId, mContext, resultsSimplified, release);
            }
        };


        String url = mContext.getString(R.string.web_host) + "/OverallElectionResult.php";
        Map<String, String> params = new HashMap<>();
        params.put("postAuthKey", mContext.getString(R.string.post_auth_key));
        params.put("electionId", Integer.toString(electionId));
        params.put("type", type);

        PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(postShowOptions);
    }

    //Overloaded method for statewise results
    public void getOverallResult(final String type, final int electionId, final String stateCode, final Context mContext, final ResultsDetailed resultsDetailed) {

        final ArrayList<PartywiseResultList> partyresultlist = new ArrayList<PartywiseResultList>();

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonResponse = null;

                try {
                    jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    boolean valid = jsonResponse.getBoolean("validAuth");
                    boolean validElection = jsonResponse.getBoolean("validElection");
                    boolean validState = jsonResponse.getBoolean("validState");
                    boolean validType = jsonResponse.getBoolean("validType");

                    if (!success) {
                        if (!valid)
                            Toast.makeText(mContext, "Server rejected request !", Toast.LENGTH_SHORT).show();
                        else if (!validState)
                            Toast.makeText(mContext, "Invalid state code !", Toast.LENGTH_SHORT).show();
                        else if (!validType)
                            Toast.makeText(mContext, "Invalid election type !", Toast.LENGTH_SHORT).show();
                        else if (!validElection)
                            resultsDetailed.release(null, null, 0, 0, "", false);
                    } else {
                        int status = jsonResponse.getInt("status");
                        int totalSeats = jsonResponse.getInt("totalSeats");
                        JSONArray array = jsonResponse.getJSONArray("results");
                        int len = array.length();
                        for (int i = 0; i < len; i++) {
                            JSONObject elections = array.getJSONObject(i);
                            partyresultlist.add(new PartywiseResultList(elections.getString("partyName"),
                                    elections.getInt("seatsWon"),
                                    elections.getString("partySymbol")));
                        }
                        getConstituencyResult(type, electionId, stateCode, mContext, resultsDetailed, partyresultlist, totalSeats);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("response error", response);
                    storeCookie(mContext, MainActivity.webView);
                    getOverallResult(type, electionId, stateCode, mContext, resultsDetailed);
                    //resultsDetailed.release(null, null, 0, 0, "", false);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(mContext, "Error Occured", Toast.LENGTH_LONG).show();
                storeCookie(mContext, MainActivity.webView);
                getOverallResult(type, electionId, stateCode, mContext, resultsDetailed);
                //resultsDetailed.release(null, null, 0, 0, "", false);
            }
        };


        String url = mContext.getString(R.string.web_host) + "/OverallElectionResult.php";
        Map<String, String> params = new HashMap<>();
        params.put("postAuthKey", mContext.getString(R.string.post_auth_key));
        params.put("electionId", Integer.toString(electionId));
        params.put("type", type);
        params.put("stateCode", stateCode);

        PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(postShowOptions);
    }

    private void getConstituencyResult(final String type, final int electionId, final String stateCode, final Context mContext, final ResultsDetailed resultsDetailed, final ArrayList<PartywiseResultList> list, final int totalSeats) {

        final ArrayList<ConstituencyWiseResultList> constresultlist = new ArrayList<ConstituencyWiseResultList>();

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonResponse = null;

                try {
                    jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    boolean valid = jsonResponse.getBoolean("validAuth");
                    boolean validElection = jsonResponse.getBoolean("validElection");
                    boolean validState = jsonResponse.getBoolean("validState");
                    boolean validType = jsonResponse.getBoolean("validType");
                    if (!success) {
                        if (!valid)
                            Toast.makeText(mContext, "Server rejected request !", Toast.LENGTH_SHORT).show();
                        else if (!validState)
                            Toast.makeText(mContext, "Invalid state code !", Toast.LENGTH_SHORT).show();
                        else if (!validType)
                            Toast.makeText(mContext, "Invalid election type !", Toast.LENGTH_SHORT).show();
                        else if (!validElection)
                            resultsDetailed.release(null, null, 0, 0, "", false);
                    } else {
                        int status = jsonResponse.getInt("status");
                        JSONArray array = jsonResponse.getJSONArray("results");
                        int len = array.length();

                        for (int i = 0; i < len; i++) {
                            JSONObject elections = array.getJSONObject(i);
                            constresultlist.add(new ConstituencyWiseResultList(elections.getString("constituencyName"),
                                    elections.getString("candidateName"),
                                    elections.getString("partyName"),
                                    elections.getString("partySymbol"),
                                    elections.getInt("voteCount")));
                        }
                        resultsDetailed.release(list, constresultlist, status, totalSeats, type, true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("response error", response);
                    storeCookie(mContext, MainActivity.webView);
                    getOverallResult(type, electionId, stateCode, mContext, resultsDetailed);
                    //resultsDetailed.release(null, null, 0, 0, "", false);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(mContext, "Error Occured", Toast.LENGTH_LONG).show();
                storeCookie(mContext, MainActivity.webView);
                getOverallResult(type, electionId, stateCode, mContext, resultsDetailed);
                //resultsDetailed.release(null, null, 0, 0, "", false);
            }
        };


        String url = mContext.getString(R.string.web_host) + "/ConstituencyWiseResult.php";
        Map<String, String> params = new HashMap<>();
        params.put("postAuthKey", mContext.getString(R.string.post_auth_key));
        params.put("electionId", Integer.toString(electionId));
        params.put("type", type);
        params.put("stateCode", stateCode);

        PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(postShowOptions);
    }

    public void getStatelist(final int electionId, final String type, final Context mContext) {
        final ArrayList<StateListItem> resultlist = new ArrayList<StateListItem>();

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonResponse = null;

                try {
                    jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    boolean valid = jsonResponse.getBoolean("validAuth");
                    if (!success | !valid) {
                        Toast.makeText(mContext, "Server rejected request !", Toast.LENGTH_SHORT).show();
                        WaitScreen.terminate = true;
                    } else {
                        JSONArray array = jsonResponse.getJSONArray("stateList");
                        int len = array.length();

                        for (int i = 0; i < len; i++) {
                            JSONObject elections = array.getJSONObject(i);
                            resultlist.add(new StateListItem(elections.getString("name"),
                                    elections.getString("code")));
                        }
                        final Intent intent = new Intent(mContext, ResultsDetailed.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("list", resultlist);
                        intent.putExtra("electionId", electionId);
                        intent.putExtra("type", type);
                        myRunnable thread = new myRunnable(intent, mContext);
                        new Thread(thread).start();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("response error", response);
                    storeCookie(mContext, MainActivity.webView);
                    getStatelist(electionId, type, mContext);
                    WaitScreen.terminate = true;
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                WaitScreen.terminate = true;
                Toast.makeText(mContext, "Error Occured", Toast.LENGTH_LONG).show();
                storeCookie(mContext, MainActivity.webView);
                getStatelist(electionId, type, mContext);
            }
        };


        String url = mContext.getString(R.string.web_host) + "/ShowStateList.php";
        Map<String, String> params = new HashMap<>();
        params.put("postAuthKey", mContext.getString(R.string.post_auth_key));

        PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(postShowOptions);
    }

    private static boolean keyFound;
    private static String setNo;
    private static HashMap<String, String> keySet;

    public void storeVote(final String boothId, final String candidateId, final Context mContext) {
        if (!keyFound) {
            Thanks.success = false;
            Thanks.threadStop = true;
        } else {
            StringBuilder enVote = new StringBuilder("");
            for (int i = 0; i < setNo.length(); i++)
                enVote.append(keySet.get(Character.toString(setNo.charAt(i))));
            for (int i = 0; i < candidateId.length(); i++)
                enVote.append(keySet.get(Character.toString(candidateId.charAt(i))));
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    JSONObject jsonResponse = null;

                    try {
                        Log.d("response", response);
                        jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            Thanks.success = true;
                            Thanks.threadStop = true;
                        } else {
                            boolean validAuth = jsonResponse.getBoolean("validAuth");
                            boolean validBooth = jsonResponse.getBoolean("validBooth");
                            boolean validIntegrity = jsonResponse.getBoolean("validIntegrity");
                            boolean validApproval = jsonResponse.getBoolean("validApproval");
                            boolean validGarbage = jsonResponse.getBoolean("validGarbage");
                            boolean deleteApproval = jsonResponse.getBoolean("deleteApproval");
                            Log.d("response", response);
                            Thanks.success = false;
                            Thanks.threadStop = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Thanks.success = false;
                        Thanks.threadStop = true;
                        Log.d("response error", response);
                        storeCookie(mContext, MainActivity.webView);
                        storeVote(boothId, candidateId, mContext);
                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(mContext, "Error Occured", Toast.LENGTH_LONG).show();
                    storeCookie(mContext, MainActivity.webView);
                    storeVote(boothId, candidateId, mContext);
                }
            };
            String url = mContext.getString(R.string.web_host) + "/StoreVote.php";
            Map<String, String> params = new HashMap<>();
            params.put("postAuthKey", mContext.getString(R.string.post_auth_key));
            params.put("boothId", boothId);
            params.put("enVote", enVote.toString());
            PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(mContext);
            queue.add(postShowOptions);
            Log.d("this was","called");
        }
    }

    public void getRandomKey(final String boothId, final Context mContext, final VotingPage votingPage) {
        keyFound = false;

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonResponse = null;

                try {
                    jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    boolean valid = jsonResponse.getBoolean("validAuth");
                    boolean validBooth = jsonResponse.getBoolean("validBooth");
                    if (!success | !valid) {
                        if (!validBooth)
                            Toast.makeText(mContext, "Invalid booth ID !", Toast.LENGTH_SHORT).show();
                    } else {
                        setNo = jsonResponse.getString("setNo");
                        keySet = new HashMap<>();
                        JSONObject array = jsonResponse.getJSONObject("keySet");
                        for (int i = 0; i < 36; i++) {
                            if (i < 26) {
                                String key = "" + (char) (65 + i);
                                keySet.put(key, array.getString(key));
                            } else {
                                String key = "" + (char) (22 + i);
                                keySet.put(key, array.getString(key));
                            }
                        }
                        keyFound = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("response error", response);
                    storeCookie(mContext, MainActivity.webView);
                    getRandomKey(boothId, mContext, votingPage);
                } finally {
                    votingPage.release();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(mContext, "Error Occured", Toast.LENGTH_LONG).show();
                storeCookie(mContext, MainActivity.webView);
                getRandomKey(boothId, mContext, votingPage);
            }
        };
        String url = mContext.getString(R.string.web_host) + "/GetRandomKey.php";
        Map<String, String> params = new HashMap<>();
        params.put("postAuthKey", mContext.getString(R.string.post_auth_key));
        params.put("boothId", boothId);
        PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(postShowOptions);
    }
}
