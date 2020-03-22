package com.example.voteonlinebruh;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerCall
{
    void validateDetails(String aadhaarNo, final String boothId, final Context mContext, final LoginPage loginPage)
    {
        Response.Listener<String> listener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                Map<String,String> params=new HashMap<>();
                JSONObject jsonResponse= null;
                response="{"+response+"}";
                WaitScreen.terminate=true;

                try {
                    jsonResponse = new JSONObject(response.substring(1,response.length()-1));
                    boolean success=jsonResponse.getBoolean("success");
                    if(!success)
                    {
                        boolean validAuth=jsonResponse.getBoolean("validAuth");
                        boolean validBooth=jsonResponse.getBoolean("validBooth");
                        boolean validAadhaar=jsonResponse.getBoolean("validAadhaar");
                        if(!validAuth)
                            Toast.makeText(mContext,"Un-authourised access request", Toast.LENGTH_SHORT).show();
                        else if(!validBooth)
                            Toast.makeText(mContext,"Enter correct booth Id",Toast.LENGTH_SHORT).show();
                        else if(!validAadhaar)
                            Toast.makeText(mContext,"Invalid/Un-registered aadhaar number",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent = new Intent(mContext, VotingInstructions.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        loginPage.finish();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,"Something went wrong",Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext,response,Toast.LENGTH_LONG).show();
                    System.out.println(response);
                    params.put("message","Failed");
                }
            }
        };

        Response.ErrorListener errorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                WaitScreen.terminate=true;
                Toast.makeText(mContext, "Error occured. Try again", Toast.LENGTH_LONG).show();
                System.out.println(error.toString());
                //Toast.makeText(mContext,error.toString(),Toast.LENGTH_LONG).show();
            }
        };


        String url=mContext.getString(R.string.web_host)+"/ValidateCredentials.php";
        Map<String,String> params=new HashMap<>();
        params.put("aadhaarNo", aadhaarNo);
        params.put("boothId", boothId);
        params.put("postAuthKey", mContext.getString(R.string.post_auth_key));


        PostRequest postValidateDetails=new PostRequest(mContext, url, params,listener,errorListener);
        RequestQueue queue=Volley.newRequestQueue(mContext);
        queue.add(postValidateDetails);

    }

    void sendVoterOtp(String aadhaarNo, final Context mContext, final VotingInstructions votingInstructions)
    {
        Response.Listener<String> listener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                Map<String,String> params=new HashMap<>();
                JSONObject jsonResponse= null;

                WaitScreen.terminate=true;

                try {
                    jsonResponse = new JSONObject(response);
                    boolean success=jsonResponse.getBoolean("success");
                    if(success)
                    {
                        Intent intent=new Intent(mContext,OtpPage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        votingInstructions.finish();
                    }
                    else
                    {
                        Toast.makeText(mContext, "Could not send otp",Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                     Toast.makeText(mContext,"Something went wrong",Toast.LENGTH_SHORT).show();
                    System.out.println(response);
                    params.put("message","Failed");
                }
            }
        };

        Response.ErrorListener errorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                WaitScreen.terminate=true;
                Toast.makeText(mContext, "Error occured. Try again", Toast.LENGTH_LONG).show();
            }
        };

        String url=mContext.getString(R.string.web_host)+"/TestSms.php";
        Map<String, String> params=new HashMap<>();
        params.put("aadhaarNo", aadhaarNo);
        params.put("smsAuthKey",mContext.getString(R.string.sms_auth_key));


        PostRequest postRequest=new PostRequest(mContext, url,params,listener,errorListener);
        RequestQueue queue= Volley.newRequestQueue(mContext);
        queue.add(postRequest);
    }

    void validateOtp(String aadhaarNo, String boothId, String otp, final Context mContext, final OtpPage otpPage)
    {
        Response.Listener<String> listener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                Map<String,String> params=new HashMap<>();
                JSONObject jsonResponse= null;

                WaitScreen.terminate=true;

                try {
                    jsonResponse = new JSONObject(response);
                    boolean success=jsonResponse.getBoolean("success");
                    if(!success)
                    {
                        boolean validAuth=jsonResponse.getBoolean("validAuth");
                        boolean validApproval=jsonResponse.getBoolean("validApproval");
                        if(!validAuth)
                            Toast.makeText(mContext,"Un-authourised access request", Toast.LENGTH_LONG).show();
                        else if(!validApproval)
                            Toast.makeText(mContext, "Vote not approved at RPD", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Intent intent = new Intent(mContext, VotingPage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        otpPage.finish();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,"Something went wrong",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(mContext,response,Toast.LENGTH_LONG).show();
                    params.put("message","Failed");
                }

            }
        };

        Response.ErrorListener errorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                WaitScreen.terminate=true;
                Toast.makeText(mContext,"Error occured. Try again", Toast.LENGTH_LONG).show();

            }
        };


        String url=mContext.getString(R.string.web_host)+"/ValidateOtp.php";
        Map<String,String> params=new HashMap<>();
        params.put("category", "booth");
        params.put("aadhaarNo",aadhaarNo);
        params.put("boothId", boothId);
        params.put("otp",otp);
        params.put("postAuthKey", mContext.getString(R.string.post_auth_key));


        PostRequest postValidateOtp=new PostRequest(mContext, url, params,listener,errorListener);
        RequestQueue queue=Volley.newRequestQueue(mContext);
        queue.add(postValidateOtp);
    }

    void getPublicCandidateList(String aadhaarNo, String electionId, String electionName, final Context mContext,
                                                      final VotingPage votingPage)
    {
        final ArrayList<PublicCandidate> publicCandidates=new ArrayList<>();

        Response.Listener<String> listener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                WaitScreen.terminate=true;

                String responseArray=response.substring(0,response.lastIndexOf("{"));
                response=response.substring(response.lastIndexOf("{"));

                JSONObject jsonResponse= null;

                WaitScreen.terminate=true;

                try {
                    jsonResponse = new JSONObject(response);
                    boolean success=jsonResponse.getBoolean("success");
                    if(!success)
                    {
                        boolean validAuth=jsonResponse.getBoolean("validAuth");
                        boolean validElection=jsonResponse.getBoolean("validElection");
                        boolean validAadhaar=jsonResponse.getBoolean("validAadhaar");

                        if(!validAuth)
                            Toast.makeText(mContext,"Un-authourised access request", Toast.LENGTH_SHORT).show();
                        else if(!validElection)
                            Toast.makeText(mContext,"Election details not found", Toast.LENGTH_SHORT).show();
                        else if(!validAadhaar)
                            Toast.makeText(mContext,"Un-registered/ Invalid aadhaar", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        JSONArray array = new JSONArray(responseArray);
                        int len=array.length();

                        for(int i=0;i<len;i++)
                        {
                            JSONObject candidate=array.getJSONObject(i);
                            publicCandidates.add(new PublicCandidate(candidate.getString("candidateName"), candidate.getString("partyName"),
                                    candidate.getString("symbol")));
                        }
                        votingPage.showList(publicCandidates);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(mContext,"Something went wrong",Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext,response,Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Error Occured",Toast.LENGTH_LONG).show();
            }
        };


        String url=mContext.getString(R.string.web_host)+"/ShowOptions.php";
        Map<String,String> params=new HashMap<>();

        params.put("aadhaarNo",aadhaarNo);
        params.put("electionId",electionId);
        params.put("electionName",electionName);
        params.put("postAuthKey", mContext.getString(R.string.post_auth_key));


        PostRequest postShowOptions=new PostRequest(mContext, url, params,listener,errorListener);
        RequestQueue queue=Volley.newRequestQueue(mContext);
        queue.add(postShowOptions);
    }
}
