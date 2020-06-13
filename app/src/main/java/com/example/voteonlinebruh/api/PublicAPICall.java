package com.example.voteonlinebruh.api;

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
import com.example.voteonlinebruh.activities.BoothList;
import com.example.voteonlinebruh.activities.ConstituencyDetailsActivity;
import com.example.voteonlinebruh.activities.MainActivity;
import com.example.voteonlinebruh.activities.PublicElectionEntryPoint;
import com.example.voteonlinebruh.activities.ResultList;
import com.example.voteonlinebruh.activities.OtpPage;
import com.example.voteonlinebruh.activities.ResultsDetailed;
import com.example.voteonlinebruh.activities.ResultsSimplified;
import com.example.voteonlinebruh.activities.Thanks;
import com.example.voteonlinebruh.activities.VotingPage;
import com.example.voteonlinebruh.activities.WaitScreen;
import com.example.voteonlinebruh.models.*;
import com.example.voteonlinebruh.utility.PostingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PublicAPICall {

  private int TIMES = 0;
  private boolean validateBoothOtpResponse,
      getPublicResultListResponse,
      getOverallResultResponse1,
      getOverallResultResponse2,
      getConstituencyResultResponse,
      getStateListResponse,
      storeVoteResponse,
      getRandomKeyResponse,
      getConstituencyDetailsResponse,
      getBoothCitiesResponse,
      getBoothLocationsResponse;
  private String boothId, otp, type, candidateId, place, constituencyName, stateCode;
  private int totalSeats, stateElectionId, tieCount, electionId;
  private boolean release, service;
  private ArrayList<PartywiseResultList> list;
  private Context mContext;
  private OtpPage otpPage;
  private ResultsSimplified resultSimplified;
  private ResultsDetailed resultDetailed;
  private VotingPage votingPage;
  private PublicElectionEntryPoint activity;

  public PublicAPICall() {
    TIMES = 0;
    validateBoothOtpResponse = false;
    getPublicResultListResponse = false;
    getOverallResultResponse1 = false;
    getOverallResultResponse2 = false;
    getConstituencyResultResponse = false;
    getStateListResponse = false;
    storeVoteResponse = false;
    getRandomKeyResponse = false;
    getConstituencyDetailsResponse = false;
    getBoothCitiesResponse = false;
    getBoothLocationsResponse = false;
  }

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

  class RequestDelayRunnable implements Runnable {
    int methodCode;

    RequestDelayRunnable(int methodCode) {
      this.methodCode = methodCode;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(1000);

        switch (methodCode) {
          case 1: // validateBoothOtp
            PublicAPICall.this.validateBoothOtp(boothId, otp, mContext, otpPage);
            break;
          case 2: // getPublicResultList
            PublicAPICall.this.getPublicResultList(mContext);
            break;
          case 3: // getOverallResult
            PublicAPICall.this.getOverallResult(
                type, electionId, mContext, resultSimplified, release);
            break;
          case 4: // getOverallResult2
            PublicAPICall.this.getOverallResult(
                type, electionId, stateCode, mContext, resultDetailed);
            break;
          case 5: // getConstituencyResult
            PublicAPICall.this.getConstituencyResult(
                type,
                electionId,
                stateCode,
                mContext,
                resultDetailed,
                list,
                totalSeats,
                tieCount,
                stateElectionId);
            break;
          case 6: // getStateList
            PublicAPICall.this.getStatelist(electionId, type, mContext);
            break;
          case 7: // storeVote
            PublicAPICall.this.storeVote(boothId, candidateId, mContext, service);
            break;
          case 8: // getRandomKey
            PublicAPICall.this.getRandomKey(boothId, mContext, votingPage);
            break;
          case 9:
            PublicAPICall.this.getConstituencyDetails(electionId, constituencyName, mContext);
            break;
          case 10:
            PublicAPICall.this.getBoothCities(mContext, activity);
            break;
          case 11:
            PublicAPICall.this.getBoothLocations(place, mContext);
        }

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void storeCookie(final Context mContext, WebView webView) {
    Log.d("COOKIE: ", "CALLED");
    TIMES++;
    String url = "";
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

  public void validateBoothOtp(
      final String boothId, final String otp, final Context mContext, final OtpPage otpPage) {
    this.boothId = boothId;
    this.otp = otp;
    this.mContext = mContext;
    this.otpPage = otpPage;

    final ArrayList<PublicCandidate> candidates = new ArrayList<PublicCandidate>();
    Response.Listener<String> listener =
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            Log.d("RESPONSE: ", "RECEIVED");
            if (!validateBoothOtpResponse) {
              Map<String, String> params = new HashMap<>();
              JSONObject jsonResponse = null;

              try {
                if (response.trim().length() == 0) throw new JSONException("ZERO LENGTH");
                jsonResponse = new JSONObject(response);
                boolean validAuth = jsonResponse.getBoolean("validAuth");
                boolean validBooth = jsonResponse.getBoolean("validBooth");
                boolean validOtp = jsonResponse.getBoolean("validOtp");
                if (!validAuth) {
                  Toast.makeText(mContext, "Un-authourised access request", Toast.LENGTH_LONG)
                      .show();
                  WaitScreen.terminate = true;
                } else if (!validBooth) {
                  Toast.makeText(mContext, "Invalid Booth ID ! ", Toast.LENGTH_LONG).show();
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
                      Toast.makeText(
                              mContext, "You have selected the wrong election !", Toast.LENGTH_LONG)
                          .show();
                    WaitScreen.terminate = true;
                  } else {
                    JSONArray array2 = parameters.getJSONArray("candidates");
                    int len2 = array2.length();

                    for (int i = 0; i < len2; i++) {
                      JSONObject elections = array2.getJSONObject(i);
                      candidates.add(
                          new PublicCandidate(
                              elections.getString("id"),
                              elections.getString("name"),
                              elections.getString("party"),
                              elections.getString("img"),
                              elections.getString("symbol")));
                    }
                    candidates.add(
                        new PublicCandidate(
                            "NOTA",
                            "NONE OF THE ABOVE",
                            "NOTA",
                            "",
                            "https://www.atheer.om/en/wp-content/uploads/sites/2/2017/12/12122017_044525_0-8.jpg"));
                    final Intent intent = new Intent(mContext, VotingPage.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("list", candidates);
                    intent.putExtra("boothId", boothId);
                    myRunnable thread = new myRunnable(intent, mContext);
                    new Thread(thread).start();
                    otpPage.finish();
                  }
                }
                validateBoothOtpResponse = true;
                TIMES = 0;
              } catch (Exception e) {
                e.printStackTrace();
                Log.d("RESPONSE EXCEPTION", response);
                // Toast.makeText(mContext, "COOKIE. Try again: TIMES=" + (TIMES++),
                // Toast.LENGTH_LONG).show();
                if (TIMES < 100 && !validateBoothOtpResponse) {
                  PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
                  RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(1);
                  new Thread(requestDelayRunnable).start();
                } else if (TIMES >= 100 && !validateBoothOtpResponse) {
                  WaitScreen.terminate = true;
                  Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
                  Log.d("FAILED", "FAILED");
                }
                // params.put("message", "Failed");
              }

            } else {
              Log.d("here", "here");
            }
          }
        };

    Response.ErrorListener errorListener =
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            Log.d("RESPONSE ERROR", error.toString());
            // Toast.makeText(mContext, "Error occured. Try again " + TIMES,
            // Toast.LENGTH_LONG).show();
            if (TIMES < 100 && !validateBoothOtpResponse) {
              PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
              RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(1);
              new Thread(requestDelayRunnable).start();
            } else if (TIMES >= 100 && !validateBoothOtpResponse) {
              WaitScreen.terminate = true;
              Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
              Log.d("FAILED", "FAILED");
            }
          }
        };

    String url = mContext.getString(R.string.web_host) + "/ValidateBoothOtp.php";
    Map<String, String> params = new HashMap<>();
    params.put("boothId", boothId);
    params.put("otp", otp);
    params.put("postAuthKey", BuildConfig.POST_AUTH_KEY);

    PostRequest postValidateOtp = new PostRequest(mContext, url, params, listener, errorListener);
    RequestQueue queue = Volley.newRequestQueue(mContext);
    queue.add(postValidateOtp);
  }

  public void getPublicResultList(final Context mContext) {
    this.mContext = mContext;

    final ArrayList<ResultListItem> resultlist = new ArrayList<ResultListItem>();

    Response.Listener<String> listener =
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {

            if (!getPublicResultListResponse) {

              JSONObject jsonResponse = null;

              try {
                jsonResponse = new JSONObject(response);
                getPublicResultListResponse = true;
                TIMES = 0;
                boolean success = jsonResponse.getBoolean("success");
                boolean valid = jsonResponse.getBoolean("validAuth");
                if (!success | !valid) {
                  Toast.makeText(mContext, "Server rejected request !", Toast.LENGTH_LONG).show();
                  WaitScreen.terminate = true;
                } else {
                  JSONArray array = jsonResponse.getJSONArray("elections");
                  int len = array.length();

                  for (int i = 0; i < len; i++) {
                    JSONObject elections = array.getJSONObject(i);
                    resultlist.add(
                        new ResultListItem(
                            elections.getInt("electionId"),
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
                if (TIMES < 100 && !getPublicResultListResponse) {
                  PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
                  RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(2);
                  new Thread(requestDelayRunnable).start();
                } else if (TIMES >= 100 && !getPublicResultListResponse) {
                  WaitScreen.terminate = true;
                  Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
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
            if (TIMES < 100 && !getPublicResultListResponse) {
              PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
              RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(2);
              new Thread(requestDelayRunnable).start();
            } else if (TIMES >= 100 && !getPublicResultListResponse) {
              WaitScreen.terminate = true;
              Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
              Log.d("FAILED", "FAILED");
            }
          }
        };

    String url = mContext.getString(R.string.web_host) + "/ShowCompletedElectionList.php";
    Map<String, String> params = new HashMap<>();
    params.put("postAuthKey", BuildConfig.POST_AUTH_KEY);

    PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
    RequestQueue queue = Volley.newRequestQueue(mContext);
    queue.add(postShowOptions);
  }

  public void getOverallResult(
      final String type,
      final int electionId,
      final Context mContext,
      final ResultsSimplified resultsSimplified,
      final boolean release) {
    this.type = type;
    this.electionId = electionId;
    this.mContext = mContext;
    this.resultSimplified = resultsSimplified;
    this.release = release;

    final ArrayList<PartywiseResultList> partyresultlist = new ArrayList<PartywiseResultList>();

    Response.Listener<String> listener =
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            if (!getOverallResultResponse1) {

              JSONObject jsonResponse = null;

              try {
                jsonResponse = new JSONObject(response);
                getOverallResultResponse1 = true;
                TIMES = 0;
                boolean success = jsonResponse.getBoolean("success");
                boolean valid = jsonResponse.getBoolean("validAuth");
                boolean validElection = jsonResponse.getBoolean("validElection");
                boolean validType = jsonResponse.getBoolean("validType");

                if (!success) {
                  if (!valid)
                    Toast.makeText(mContext, "Server rejected request !", Toast.LENGTH_LONG).show();
                  else if (!validElection)
                    Toast.makeText(mContext, "Invalid election ID !", Toast.LENGTH_LONG).show();
                  else if (!validType)
                    Toast.makeText(mContext, "Invalid election type !", Toast.LENGTH_LONG).show();
                  WaitScreen.terminate = true;
                } else {
                  int status = jsonResponse.getInt("status");
                  int totalSeats = jsonResponse.getInt("totalSeats");
                  JSONArray array = jsonResponse.getJSONArray("results");
                  int len = array.length();

                  for (int i = 0; i < len; i++) {
                    JSONObject elections = array.getJSONObject(i);
                    partyresultlist.add(
                        new PartywiseResultList(
                            elections.getString("partyName"),
                            elections.getInt("seatsWon"),
                            elections.getString("partySymbol")));
                  }
                  if (release) {
                    if (!resultsSimplified.isDestroyed())
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
                if (TIMES < 100 && !getOverallResultResponse1) {
                  PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
                  RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(3);
                  new Thread(requestDelayRunnable).start();
                } else if (TIMES >= 100 && !getOverallResultResponse1) {
                  WaitScreen.terminate = true;
                  Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
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
            if (TIMES < 100 && !getOverallResultResponse1) {
              PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
              RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(3);
              new Thread(requestDelayRunnable).start();
            } else if (TIMES >= 100 && !getOverallResultResponse1) {
              WaitScreen.terminate = true;
              Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
              Log.d("FAILED", "FAILED");
            }
          }
        };

    String url = mContext.getString(R.string.web_host) + "/OverallElectionResult.php";
    Map<String, String> params = new HashMap<>();
    params.put("postAuthKey", BuildConfig.POST_AUTH_KEY);
    params.put("electionId", Integer.toString(electionId));
    params.put("type", type);

    PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
    RequestQueue queue = Volley.newRequestQueue(mContext);
    queue.add(postShowOptions);
  }

  // Overloaded method for statewise results
  public void getOverallResult(
      final String type,
      final int electionId,
      final String stateCode,
      final Context mContext,
      final ResultsDetailed resultsDetailed) {
    this.type = type;
    this.electionId = electionId;
    this.stateCode = stateCode;
    this.mContext = mContext;
    this.resultDetailed = resultsDetailed;

    final ArrayList<PartywiseResultList> partyresultlist = new ArrayList<PartywiseResultList>();

    Response.Listener<String> listener =
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            if (!getOverallResultResponse2) {

              JSONObject jsonResponse = null;

              try {
                jsonResponse = new JSONObject(response);
                getOverallResultResponse2 = true;
                TIMES = 0;
                boolean success = jsonResponse.getBoolean("success");
                boolean valid = jsonResponse.getBoolean("validAuth");
                boolean validElection = jsonResponse.getBoolean("validElection");
                boolean validState = jsonResponse.getBoolean("validState");
                boolean validType = jsonResponse.getBoolean("validType");

                if (!success) {
                  if (!valid)
                    Toast.makeText(mContext, "Server rejected request !", Toast.LENGTH_LONG).show();
                  else if (!validState)
                    Toast.makeText(mContext, "Invalid state code !", Toast.LENGTH_LONG).show();
                  else if (!validType)
                    Toast.makeText(mContext, "Invalid election type !", Toast.LENGTH_LONG).show();
                  else if (!validElection)
                    resultsDetailed.release(null, null, 0, 0, tieCount, stateElectionId, "", false);
                } else {
                  int status = jsonResponse.getInt("status");
                  int totalSeats = jsonResponse.getInt("totalSeats");
                  int stateElectionId = jsonResponse.getInt("stateElectionId");
                  int tieCount = jsonResponse.getInt("tieCount");
                  JSONArray array = jsonResponse.getJSONArray("results");
                  int len = array.length();
                  for (int i = 0; i < len; i++) {
                    JSONObject elections = array.getJSONObject(i);
                    partyresultlist.add(
                        new PartywiseResultList(
                            elections.getString("partyName"),
                            elections.getInt("seatsWon"),
                            elections.getString("partySymbol")));
                  }
                  getConstituencyResult(
                      type,
                      electionId,
                      stateCode,
                      mContext,
                      resultsDetailed,
                      partyresultlist,
                      totalSeats,
                      tieCount,
                      stateElectionId);
                }
              } catch (JSONException e) {
                e.printStackTrace();
                Log.d("response error", response);
                if (TIMES < 100 && !getOverallResultResponse2) {
                  PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
                  RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(4);
                  new Thread(requestDelayRunnable).start();
                } else if (TIMES >= 100 && !getOverallResultResponse2) {
                  Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
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
            if (TIMES < 100 && !getOverallResultResponse2) {
              PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
              RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(4);
              new Thread(requestDelayRunnable).start();
            } else if (TIMES >= 100 && !getOverallResultResponse2) {
              Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
              Log.d("FAILED", "FAILED");
            }
          }
        };

    String url = mContext.getString(R.string.web_host) + "/OverallElectionResult.php";
    Map<String, String> params = new HashMap<>();
    params.put("postAuthKey", BuildConfig.POST_AUTH_KEY);
    params.put("electionId", Integer.toString(electionId));
    params.put("type", type);
    params.put("stateCode", stateCode);

    PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
    RequestQueue queue = Volley.newRequestQueue(mContext);
    queue.add(postShowOptions);
  }

  private void getConstituencyResult(
      final String type,
      final int electionId,
      final String stateCode,
      final Context mContext,
      final ResultsDetailed resultsDetailed,
      final ArrayList<PartywiseResultList> list,
      final int totalSeats,
      final int tieCount,
      final int stateElectionId) {
    this.type = type;
    this.electionId = electionId;
    this.stateCode = stateCode;
    this.mContext = mContext;
    this.resultDetailed = resultsDetailed;
    this.list = list;
    this.totalSeats = totalSeats;
    this.tieCount = tieCount;
    this.stateElectionId = stateElectionId;

    final ArrayList<ConstituencyWiseResultList> constresultlist =
        new ArrayList<ConstituencyWiseResultList>();

    Response.Listener<String> listener =
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
            if (!getConstituencyResultResponse) {

              JSONObject jsonResponse = null;

              try {
                jsonResponse = new JSONObject(response);
                getConstituencyResultResponse = true;
                TIMES = 0;
                boolean success = jsonResponse.getBoolean("success");
                boolean valid = jsonResponse.getBoolean("validAuth");
                boolean validElection = jsonResponse.getBoolean("validElection");
                boolean validState = jsonResponse.getBoolean("validState");
                boolean validType = jsonResponse.getBoolean("validType");
                if (!success) {
                  if (!valid)
                    Toast.makeText(mContext, "Server rejected request !", Toast.LENGTH_LONG).show();
                  else if (!validState)
                    Toast.makeText(mContext, "Invalid state code !", Toast.LENGTH_LONG).show();
                  else if (!validType)
                    Toast.makeText(mContext, "Invalid election type !", Toast.LENGTH_LONG).show();
                  else if (!validElection)
                    resultsDetailed.release(null, null, 0, 0, tieCount, stateElectionId, "", false);
                } else {
                  int status = jsonResponse.getInt("status");
                  JSONArray array = jsonResponse.getJSONArray("results");
                  int len = array.length();

                  for (int i = 0; i < len; i++) {
                    JSONObject elections = array.getJSONObject(i);
                    constresultlist.add(
                        new ConstituencyWiseResultList(
                            elections.getString("constituencyName"),
                            elections.getString("candidateName"),
                            elections.getString("partyName"),
                            elections.getString("partySymbol"),
                            elections.getString("candidateImage"),
                            elections.getInt("voteCount")));
                  }
                  if (!resultsDetailed.isDestroyed())
                    resultsDetailed.release(
                        list,
                        constresultlist,
                        status,
                        totalSeats,
                        tieCount,
                        stateElectionId,
                        type,
                        true);
                }
              } catch (JSONException e) {
                e.printStackTrace();
                Log.d("response error", response);
                if (TIMES < 100 && !getConstituencyResultResponse) {
                  PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
                  RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(5);
                  new Thread(requestDelayRunnable).start();
                } else if (TIMES >= 100 && !getConstituencyResultResponse) {
                  Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
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
            if (TIMES < 100 && !getConstituencyResultResponse) {
              PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
              RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(5);
              new Thread(requestDelayRunnable).start();
            } else if (TIMES >= 100 && !getConstituencyResultResponse) {
              Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
              Log.d("FAILED", "FAILED");
            }
          }
        };

    String url = mContext.getString(R.string.web_host) + "/ConstituencyWiseResult.php";
    Map<String, String> params = new HashMap<>();
    params.put("postAuthKey", BuildConfig.POST_AUTH_KEY);
    params.put("electionId", Integer.toString(electionId));
    params.put("type", type);
    params.put("stateCode", stateCode);

    PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
    RequestQueue queue = Volley.newRequestQueue(mContext);
    queue.add(postShowOptions);
  }

  public void getStatelist(final int electionId, final String type, final Context mContext) {
    this.electionId = electionId;
    this.type = type;
    this.mContext = mContext;

    final ArrayList<StateListItem> resultlist = new ArrayList<StateListItem>();

    Response.Listener<String> listener =
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {

            if (!getStateListResponse) {
              JSONObject jsonResponse = null;

              try {
                jsonResponse = new JSONObject(response);
                getStateListResponse = true;
                TIMES = 0;
                boolean success = jsonResponse.getBoolean("success");
                boolean valid = jsonResponse.getBoolean("validAuth");
                if (!success | !valid) {
                  Toast.makeText(mContext, "Server rejected request !", Toast.LENGTH_LONG).show();
                  WaitScreen.terminate = true;
                } else {
                  JSONArray array = jsonResponse.getJSONArray("stateList");
                  int len = array.length();

                  for (int i = 0; i < len; i++) {
                    JSONObject elections = array.getJSONObject(i);
                    resultlist.add(
                        new StateListItem(
                            elections.getString("name"), elections.getString("code")));
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
                if (TIMES < 100 && !getStateListResponse) {
                  PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
                  RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(6);
                  new Thread(requestDelayRunnable).start();
                } else if (TIMES >= 100 && !getStateListResponse) {
                  WaitScreen.terminate = true;
                  Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
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
            if (TIMES < 100 && !getStateListResponse) {
              PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
              RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(6);
              new Thread(requestDelayRunnable).start();
            } else if (TIMES >= 100 && !getStateListResponse) {
              WaitScreen.terminate = true;
              Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
              Log.d("FAILED", "FAILED");
            }
          }
        };

    String url = mContext.getString(R.string.web_host) + "/ShowStateList.php";
    Map<String, String> params = new HashMap<>();
    params.put("postAuthKey", BuildConfig.POST_AUTH_KEY);

    PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
    RequestQueue queue = Volley.newRequestQueue(mContext);
    queue.add(postShowOptions);
  }

  public void getConstituencyDetails(
      final int electionId, final String constituencyName, final Context mContext) {
    this.electionId = electionId;
    this.mContext = mContext;
    this.constituencyName = constituencyName;
    final ArrayList<ConstituencyDetailResult> detailResults = new ArrayList<>();
    Response.Listener<String> listener =
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {

            if (!getConstituencyDetailsResponse) {
              JSONObject jsonResponse = null;
              try {
                jsonResponse = new JSONObject(response);
                getConstituencyDetailsResponse = true;
                TIMES = 0;
                boolean success = jsonResponse.getBoolean("success");
                boolean valid = jsonResponse.getBoolean("validAuth");
                boolean validElection = jsonResponse.getBoolean("validElection");
                boolean validConstituency = jsonResponse.getBoolean("validConstituency");
                if (!success || !valid) {
                  if (!validElection)
                    Toast.makeText(mContext, "Invalid election ID !", Toast.LENGTH_LONG).show();
                  else if (!validConstituency)
                    Toast.makeText(mContext, "Invalid constituency !", Toast.LENGTH_LONG).show();
                  WaitScreen.terminate = true;
                } else {
                  JSONArray array = jsonResponse.getJSONArray("detailResult");
                  int len = array.length();

                  for (int i = 0; i < len; i++) {
                    JSONObject result = array.getJSONObject(i);
                    detailResults.add(
                        new ConstituencyDetailResult(
                            result.getString("name"),
                            result.getString("image"),
                            result.getString("partyName"),
                            result.getString("partySymbol"),
                            result.getInt("noOfVotes")));
                  }
                  final Intent intent = new Intent(mContext, ConstituencyDetailsActivity.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  intent.putExtra("list", detailResults);
                  myRunnable thread = new myRunnable(intent, mContext);
                  new Thread(thread).start();
                }
              } catch (JSONException e) {
                e.printStackTrace();
                if (TIMES < 100 && !getConstituencyDetailsResponse) {
                  PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
                  RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(9);
                  new Thread(requestDelayRunnable).start();
                } else if (TIMES >= 100 && !getConstituencyDetailsResponse) {
                  Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
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
            if (TIMES < 100 && !getRandomKeyResponse) {
              PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
              RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(9);
              new Thread(requestDelayRunnable).start();
            } else if (TIMES >= 100 && !getConstituencyDetailsResponse) {
              Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
            }
          }
        };
    String url = mContext.getString(R.string.web_host) + "/ConstituencyResultDetails.php";
    Map<String, String> params = new HashMap<>();
    params.put("postAuthKey", BuildConfig.POST_AUTH_KEY);
    params.put("stateElectionId", Integer.toString(electionId));
    params.put("constituencyName", constituencyName);
    PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
    RequestQueue queue = Volley.newRequestQueue(mContext);
    queue.add(postShowOptions);
  }

  public void getBoothCities(final Context mContext, final PublicElectionEntryPoint activity) {
    this.mContext = mContext;
    this.activity = activity;
    final HashMap<String, String> cities = new HashMap<>();
    Response.Listener<String> listener =
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {

            if (!getBoothCitiesResponse) {
              JSONObject jsonResponse = null;
              try {
                jsonResponse = new JSONObject(response);
                getBoothCitiesResponse = true;
                TIMES = 0;
                boolean success = jsonResponse.getBoolean("success");
                boolean valid = jsonResponse.getBoolean("validAuth");
                if (!success || !valid) {
                  Toast.makeText(mContext, "Server rejected request !", Toast.LENGTH_LONG).show();
                  activity.release(null);
                } else {
                  JSONArray array = jsonResponse.getJSONArray("listOfPlaces");
                  int len = array.length();
                  for (int i = 0; i < len; i++) {
                    JSONObject result = array.getJSONObject(i);
                    cities.put(result.getString("place"), result.getString("state"));
                  }
                  activity.release(cities);
                }
              } catch (JSONException e) {
                e.printStackTrace();
                if (TIMES < 100 && !getBoothCitiesResponse) {
                  PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
                  RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(10);
                  new Thread(requestDelayRunnable).start();
                } else if (TIMES >= 100 && !getBoothCitiesResponse) {
                  Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
                  activity.release(null);
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
            if (TIMES < 100 && !getBoothCitiesResponse) {
              PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
              RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(10);
              new Thread(requestDelayRunnable).start();
            } else if (TIMES >= 100 && !getBoothCitiesResponse) {
              Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
              activity.release(null);
            }
          }
        };
    String url = mContext.getString(R.string.web_host) + "/BoothPlaces.php";
    Map<String, String> params = new HashMap<>();
    params.put("postAuthKey", BuildConfig.POST_AUTH_KEY);
    PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
    RequestQueue queue = Volley.newRequestQueue(mContext);
    queue.add(postShowOptions);
  }

  private static boolean keyFound;
  private static String setNo;
  private static HashMap<String, String> keySet;

  public void storeVote(
      final String boothId,
      final String candidateId,
      final Context mContext,
      final boolean service) {
    this.boothId = boothId;
    this.candidateId = candidateId;
    this.mContext = mContext;
    this.service = service;

    if (!keyFound) {
      if (service) {
        Intent intent = new Intent(mContext, PostingService.class);
        intent.putExtra("status", true);
        intent.putExtra("action", PostingService.ACTION_STOP_SERVICE);
        mContext.startService(intent);
      } else Thanks.instance.release(false);
      Log.d("Key found", "false");
    } else {
      StringBuilder enVote = new StringBuilder("");
      for (int i = 0; i < setNo.length(); i++)
        enVote.append(keySet.get(Character.toString(setNo.charAt(i))));
      for (int i = 0; i < candidateId.length(); i++)
        enVote.append(keySet.get(Character.toString(candidateId.charAt(i))));
      Response.Listener<String> listener =
          new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              if (!storeVoteResponse) {
                JSONObject jsonResponse = null;

                try {
                  Log.d("response", response);
                  jsonResponse = new JSONObject(response);
                  storeVoteResponse = true;
                  TIMES = 0;
                  boolean success = jsonResponse.getBoolean("success");
                  if (success) {
                    if (service) {
                      Intent intent = new Intent(mContext, PostingService.class);
                      intent.putExtra("status", true);
                      intent.putExtra("action", PostingService.ACTION_STOP_SERVICE);
                      mContext.startService(intent);
                    } else Thanks.instance.release(true);
                  } else {
                    boolean validAuth = jsonResponse.getBoolean("validAuth");
                    boolean validBooth = jsonResponse.getBoolean("validBooth");
                    boolean validIntegrity = jsonResponse.getBoolean("validIntegrity");
                    boolean validApproval = jsonResponse.getBoolean("validApproval");
                    boolean validGarbage = jsonResponse.getBoolean("validGarbage");
                    boolean deleteApproval = jsonResponse.getBoolean("deleteApproval");
                    Log.d("response", response);
                    if (service) {
                      Intent intent = new Intent(mContext, PostingService.class);
                      intent.putExtra("status", false);
                      intent.putExtra("action", PostingService.ACTION_STOP_SERVICE);
                      mContext.startService(intent);
                    } else Thanks.instance.release(false);
                  }
                } catch (JSONException e) {
                  e.printStackTrace();

                  if (TIMES < 100 && !storeVoteResponse) {
                    PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
                    RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(7);
                    new Thread(requestDelayRunnable).start();
                  } else if (TIMES >= 100 && !storeVoteResponse) {
                    Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
                    if (service) {
                      Intent intent = new Intent(mContext, PostingService.class);
                      intent.putExtra("status", false);
                      intent.putExtra("action", PostingService.ACTION_STOP_SERVICE);
                      mContext.startService(intent);
                    } else Thanks.instance.release(false);
                    ;
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
              if (TIMES < 100 && !storeVoteResponse) {
                PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
                RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(7);
                new Thread(requestDelayRunnable).start();
              } else if (TIMES >= 100 && !storeVoteResponse) {
                Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
                if (service) {
                  Intent intent = new Intent(mContext, PostingService.class);
                  intent.putExtra("status", false);
                  intent.putExtra("action", PostingService.ACTION_STOP_SERVICE);
                  mContext.startService(intent);
                } else Thanks.instance.release(false);
                Log.d("FAILED", "FAILED");
              }
            }
          };
      String url = mContext.getString(R.string.web_host) + "/StoreVote.php";
      Map<String, String> params = new HashMap<>();
      params.put("postAuthKey", BuildConfig.POST_AUTH_KEY);
      params.put("boothId", boothId);
      params.put("enVote", enVote.toString());
      PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
      RequestQueue queue = Volley.newRequestQueue(mContext);
      queue.add(postShowOptions);
      Log.d("this was", "called");
    }
  }

  public void getRandomKey(
      final String boothId, final Context mContext, final VotingPage votingPage) {
    this.boothId = boothId;
    this.mContext = mContext;
    this.votingPage = votingPage;

    keyFound = false;

    Response.Listener<String> listener =
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {

            if (!getRandomKeyResponse) {
              JSONObject jsonResponse = null;

              try {
                jsonResponse = new JSONObject(response);
                getRandomKeyResponse = true;
                TIMES = 0;
                boolean success = jsonResponse.getBoolean("success");
                boolean valid = jsonResponse.getBoolean("validAuth");
                boolean validBooth = jsonResponse.getBoolean("validBooth");
                if (!success || !valid) {
                  if (!validBooth)
                    Toast.makeText(mContext, "Invalid booth ID !", Toast.LENGTH_LONG).show();
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
                votingPage.release();
              } catch (JSONException e) {
                e.printStackTrace();
                if (TIMES < 100 && !getRandomKeyResponse) {
                  PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
                  RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(8);
                  new Thread(requestDelayRunnable).start();
                } else if (TIMES >= 100 && !getRandomKeyResponse) {
                  Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
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
            if (TIMES < 100 && !getRandomKeyResponse) {
              PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
              RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(8);
              new Thread(requestDelayRunnable).start();
            } else if (TIMES >= 100 && !getRandomKeyResponse) {
              Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
            }
          }
        };
    String url = mContext.getString(R.string.web_host) + "/GetRandomKey.php";
    Map<String, String> params = new HashMap<>();
    params.put("postAuthKey", BuildConfig.POST_AUTH_KEY);
    params.put("boothId", boothId);
    PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
    RequestQueue queue = Volley.newRequestQueue(mContext);
    queue.add(postShowOptions);
  }

  public void getBoothLocations(final String place, final Context mContext) {
    this.place = place;
    this.mContext = mContext;
    final ArrayList<BoothDetailItem> cityDetails = new ArrayList<>();
    Response.Listener<String> listener =
        new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {

            if (!getBoothLocationsResponse) {
              JSONObject jsonResponse = null;
              try {
                jsonResponse = new JSONObject(response);
                getBoothCitiesResponse = true;
                TIMES = 0;
                boolean success = jsonResponse.getBoolean("success");
                boolean valid = jsonResponse.getBoolean("validAuth");
                boolean validPlace = jsonResponse.getBoolean("validPlace");
                if (!success) {
                  if (!valid)
                    Toast.makeText(mContext, "Server rejected request !", Toast.LENGTH_LONG).show();
                  else if (!validPlace)
                    Toast.makeText(mContext, "Invalid City !", Toast.LENGTH_LONG).show();
                  WaitScreen.terminate = true;
                } else {
                  JSONArray array = jsonResponse.getJSONArray("allBoothsInPlace");
                  int len = array.length();
                  for (int i = 0; i < len; i++) {
                    JSONObject result = array.getJSONObject(i);
                    cityDetails.add(
                        new BoothDetailItem(
                            result.getString("area"),
                            result.getString("address"),
                            result.getString("landmark"),
                            result.getString("mapLink"),
                            result.getString("coordinates")));
                  }
                  final Intent intent = new Intent(mContext, BoothList.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  intent.putExtra("list", cityDetails);
                  intent.putExtra("city", place);
                  myRunnable thread = new myRunnable(intent, mContext);
                  new Thread(thread).start();
                }
              } catch (JSONException e) {
                e.printStackTrace();
                if (TIMES < 100 && !getBoothLocationsResponse) {
                  PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
                  RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(11);
                  new Thread(requestDelayRunnable).start();
                } else if (TIMES >= 100 && !getBoothLocationsResponse) {
                  Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
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
            if (TIMES < 100 && !getBoothLocationsResponse) {
              PublicAPICall.this.storeCookie(mContext, MainActivity.webView);
              RequestDelayRunnable requestDelayRunnable = new RequestDelayRunnable(11);
              new Thread(requestDelayRunnable).start();
            } else if (TIMES >= 100 && !getBoothLocationsResponse) {
              Toast.makeText(mContext, "Request timed out", Toast.LENGTH_LONG).show();
            }
          }
        };
    String url = mContext.getString(R.string.web_host) + "/BoothAddress.php";
    Map<String, String> params = new HashMap<>();
    params.put("postAuthKey", BuildConfig.POST_AUTH_KEY);
    params.put("place", place);
    PostRequest postShowOptions = new PostRequest(mContext, url, params, listener, errorListener);
    RequestQueue queue = Volley.newRequestQueue(mContext);
    queue.add(postShowOptions);
  }
}
