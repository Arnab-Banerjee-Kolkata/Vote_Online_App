package com.example.voteonlinebruh.activities;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.voteonlinebruh.R;

public class VotingInstructions extends AppCompatActivity implements View.OnClickListener {

    Button proceed, demo;
    String aadhaarNo = "";
    Context mContext;
    int themeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.TM.getThemeId() == R.style.AppTheme_Light) {
            setTheme(R.style.dTheme_Light);
            themeId = R.style.ConfirmTheme_Light;
        } else {
            setTheme(R.style.dTheme_Dark);
            themeId = R.style.ConfirmTheme_Dark;
        }
        setContentView(R.layout.activity_voting_instructions);
        proceed = findViewById(R.id.proceed);
        demo = findViewById(R.id.demo);
        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demo.setEnabled(false);
                Intent intent = new Intent(mContext, Demo.class);
                startActivity(intent);

            }
        });

        mContext = getApplicationContext();

        proceed.setOnClickListener(this);

        SharedPreferences voterDetails = getSharedPreferences("VoterDetails", MODE_PRIVATE);
        aadhaarNo = voterDetails.getString("aadhaarNo", "");

    }

    @Override
    public void finish() {
        super.finish();

    }

    @Override
    public void onClick(View v) {
        if (v.equals(proceed)) {

            v.setEnabled(false);

            //UN-COMMENT FOR SENDING SMS

            /*ServerCall serverCall=new ServerCall();
            serverCall.sendVoterOtp(aadhaarNo, mContext, VotingInstructions.this);



            //ACTUAL WAIT SCREEN CALL

            WaitScreen.terminate=false;
            Intent intent = new Intent(getApplicationContext(), WaitScreen.class);
            intent.putExtra("LABEL","Sending OTP");
            startActivity(intent);*/


            //COMMENT FOR SENDING SMS

            Intent intent = new Intent(mContext, OtpPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

            VotingInstructions.this.finish();


        }

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, themeId).setTitle("Confirm Log Out")
                .setMessage("\nYou cannot go back ! You will be logged out.\nAre you sure you want to log out ?\n")
                .setPositiveButton("Yes, Logout !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton("No !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onResume() {
        demo.setEnabled(true);
        proceed.setEnabled(true);
        super.onResume();
    }

    void logout() {
        super.onBackPressed();
    }
}
